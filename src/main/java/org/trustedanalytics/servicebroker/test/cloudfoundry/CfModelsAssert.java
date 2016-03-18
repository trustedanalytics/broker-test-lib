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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceBinding;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.google.common.annotations.VisibleForTesting;

public class CfModelsAssert {

  private CfModelsAssert() {
  }

  private static final List<Function<ServiceInstance, Object>> SERVICE_INSTANCE_PROPERTIES = Arrays.asList(
      ServiceInstance::getDashboardUrl, ServiceInstance::getOrganizationGuid, ServiceInstance::getPlanId,
      ServiceInstance::getServiceDefinitionId, ServiceInstance::getServiceInstanceId, ServiceInstance::getSpaceGuid);
  private static List<Function<ServiceInstanceBinding, Object>> SERVICE_BINDING_PROPERTIES = Arrays.asList(
      ServiceInstanceBinding::getAppGuid, ServiceInstanceBinding::getCredentials, ServiceInstanceBinding::getId,
      ServiceInstanceBinding::getServiceInstanceId, ServiceInstanceBinding::getSyslogDrainUrl);

  public static Matcher<ServiceInstance> deeplyEqualTo(ServiceInstance expectedInstance) {
    return new DeepEqualityMatcher<>(expectedInstance, SERVICE_INSTANCE_PROPERTIES);
  }

  public static Matcher<ServiceInstanceBinding> deeplyEqualTo(ServiceInstanceBinding expectedBinding) {
    return new DeepEqualityMatcher<>(expectedBinding, SERVICE_BINDING_PROPERTIES);
  }

  @VisibleForTesting
  static class DeepEqualityMatcher<T> extends BaseMatcher<T> {
    private final T expectedResult;
    private final List<Function<T, Object>> properties;

    public DeepEqualityMatcher(T expectedResult, List<Function<T, Object>> properties) {
      this.expectedResult = expectedResult;
      this.properties = properties;
    }

    @Override
    public boolean matches(Object item) {
      if (item == expectedResult) {
        return true;
      }
      if (item == null || expectedResult == null) {
        return false;
      }
      if (item.getClass() != expectedResult.getClass()) {
        return false;
      }
      T actualResult = (T) item;
      return equality(actualResult, expectedResult, properties);
    }

    private static <T> boolean equality(T firstObject, T secondObject, List<Function<T, Object>> properties) {
      for (Function<T, Object> property : properties) {
        Object firstValue = property.apply(firstObject);
        Object secondValue = property.apply(secondObject);
        if (firstValue != secondValue && (firstValue == null || !firstValue.equals(secondValue))) {
          return false;
        }
      }
      return true;
    }

    @Override
    public void describeTo(Description description) {
    }
  }
}
