/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.servicebroker.test.zookeeper;

import org.apache.commons.codec.binary.Base64;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.Hashing;

public class ZooKeeperTestOperations {

  private static final String FORMAT = "%s:%s";

  private ZooKeeperTestOperations() {}

  public static void createSecuredNode(ZooKeeperCredentials credentials, String path) throws Exception {
    CuratorFramework tempClient = getNewClient(credentials.getConnectionString());

    byte[] authDigest = Hashing.sha1()
        .hashBytes(String.format(FORMAT, credentials.getUsername(), credentials.getPassword()).getBytes()).asBytes();
    String authEncoded = new String(Base64.encodeBase64(authDigest));
    ImmutableList<ACL> acl = ImmutableList.of(
        new ACL(ZooDefs.Perms.ALL, new Id("digest", String.format(FORMAT, credentials.getUsername(), authEncoded))));

    tempClient.create().creatingParentsIfNeeded().withACL(acl).forPath(path);

    tempClient.close();
  }

  public static CuratorFramework getNewClient(String connectionString) {
    CuratorFramework tempClient =
        CuratorFrameworkFactory.builder().connectString(connectionString).retryPolicy(new RetryOneTime(100)).build();
    tempClient.start();
    return tempClient;
  }

  public static CuratorFramework getNewAuthorizedClient(ZooKeeperCredentials credentials) {
    CuratorFramework tempClient = CuratorFrameworkFactory.builder().connectString(credentials.getConnectionString())
        .retryPolicy(new RetryOneTime(100)).authorization("digest",
            String.format(FORMAT  , credentials.getUsername(), credentials.getPassword()).getBytes())
        .build();
    tempClient.start();
    return tempClient;
  }
}
