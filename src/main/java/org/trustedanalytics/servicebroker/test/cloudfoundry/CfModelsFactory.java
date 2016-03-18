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
package org.trustedanalytics.servicebroker.test.cloudfoundry;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceBinding;

public final class CfModelsFactory {

  private CfModelsFactory() {
  }

  public static ServiceInstance getServiceInstance() {
    String instanceId = randomId();
    return getServiceInstance(instanceId);
  }

  private static String randomId() {
    return UUID.randomUUID().toString();
  }

  public static ServiceInstance getServiceInstance(String instanceId) {
    String planId = randomId();
    return getServiceInstance(instanceId, planId);
  }

  public static ServiceInstance getServiceInstance(String instanceId, String planId) {
    String organizationId = randomId();
    String spaceId = randomId();
    return getServiceInstance(
        new CreateServiceInstanceRequest(getServiceDefinition().getId(), planId, organizationId, spaceId), instanceId);
  }

  public static ServiceInstance getServiceInstance(CreateServiceInstanceRequest createRequest, String instanceId) {
    return new ServiceInstance(createRequest.withServiceInstanceId(instanceId));

  }

  private static ServiceDefinition getServiceDefinition() {
    return new ServiceDefinition("def", "name", "desc", true, Collections.emptyList());
  }


  public static ServiceInstanceBinding getServiceBinding() {
    String instanceId = randomId();
    return getServiceBinding(instanceId);
  }

  public static ServiceInstanceBinding getServiceBinding(String instanceId) {
    return new ServiceInstanceBinding(randomId(), instanceId, null, "url", randomId());
  }

  public static ServiceInstanceBinding getServiceBinding(ServiceInstanceBinding serviceBinding,
      Map<String, Object> credentials) {
    return new ServiceInstanceBinding(serviceBinding.getId(), serviceBinding.getServiceInstanceId(), credentials,
        serviceBinding.getSyslogDrainUrl(), serviceBinding.getAppGuid());
  }

  public static ServiceInstanceBinding getServiceBinding(CreateServiceInstanceBindingRequest request,
      Map<String, Object> credentials) {
    return new ServiceInstanceBinding(request.getBindingId(), request.getServiceInstanceId(), credentials, null,
        request.getAppGuid());
  }

  public static CreateServiceInstanceRequest getCreateInstanceRequest(ServiceInstance instance) {
    return new CreateServiceInstanceRequest(getServiceDefinition().getId(), instance.getPlanId(),
        instance.getOrganizationGuid(), instance.getSpaceGuid()).withServiceInstanceId(instance.getServiceInstanceId())
            .withServiceDefinition(getServiceDefinition());
  }

  public static CreateServiceInstanceBindingRequest getCreateBindingRequest() {
    String instanceId = randomId();
    return getCreateBindingRequest(instanceId);
  }

  public static CreateServiceInstanceBindingRequest getCreateBindingRequest(String instanceId) {
    String planId = randomId();
    return getCreateBindingRequest(instanceId, planId);
  }

  public static CreateServiceInstanceBindingRequest getCreateBindingRequest(String instanceId, String planId) {
    String appId = randomId();
    return new CreateServiceInstanceBindingRequest(getServiceDefinition().getId(), planId, appId, null)
        .withServiceInstanceId(instanceId);
  }

  public static DeleteServiceInstanceRequest getDeleteInstanceRequest(ServiceInstance instance) {
    return new DeleteServiceInstanceRequest(instance.getServiceInstanceId(), instance.getServiceDefinitionId(),
        instance.getPlanId());
  }

  public static DeleteServiceInstanceBindingRequest getDeleteBindingRequest(String serviceInstanceId,
      CreateServiceInstanceBindingRequest bindReq) {
    return new DeleteServiceInstanceBindingRequest(bindReq.getBindingId(), getServiceInstance(serviceInstanceId),
        bindReq.getServiceDefinitionId(), bindReq.getPlanId());
  }
}
