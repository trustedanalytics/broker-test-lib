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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class DeepEqualityMatcherTest {

  @Test
  public void matches_nullAndNull_returnsTrue() throws Exception {
    TestPojo pojo1 = null;
    TestPojo pojo2 = null;

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(true));
  }

  @Test
  public void matches_nullAndNotNull_returnsFalse() throws Exception {
    TestPojo pojo1 = null;
    TestPojo pojo2 = new TestPojo(null, null);

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_notNullAndNull_returnsTruee() throws Exception {
    TestPojo pojo1 = new TestPojo(null, null);
    TestPojo pojo2 = null;

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_nullFieldsAndNullFields_returnsTrue() throws Exception {
    TestPojo pojo1 = new TestPojo(null, null);
    TestPojo pojo2 = new TestPojo(null, null);

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(true));
  }

  @Test
  public void matches_differentClass_returnsFalse() throws Exception {
    TestPojo pojo1 = new TestPojo(null, null);
    String pojo2 = "pojo2";

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_nullAndNotNullInFirstField_returnsFalse() throws Exception {
    TestPojo pojo1 = new TestPojo(null, null);
    TestPojo pojo2 = new TestPojo("", null);

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_notNullAndNullInFirstField_returnsFalse() throws Exception {
    TestPojo pojo1 = new TestPojo("", null);
    TestPojo pojo2 = new TestPojo(null, null);

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_equalNotNullInFirstField_returnsTrue() throws Exception {
    TestPojo pojo1 = new TestPojo("abc", null);
    TestPojo pojo2 = new TestPojo("abc", null);

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(true));
  }

  @Test
  public void matches_unequalNotNullInFirstField_returnsFalse() throws Exception {
    TestPojo pojo1 = new TestPojo("abc", null);
    TestPojo pojo2 = new TestPojo("ABC", null);

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_nullAndNotNullInSecondField_returnsFalse() throws Exception {
    TestPojo pojo1 = new TestPojo("abc", null);
    TestPojo pojo2 = new TestPojo("abc", ImmutableMap.of());

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_notNullAndNullInSecondField_returnsFalse() throws Exception {
    TestPojo pojo1 = new TestPojo("abc", ImmutableMap.of());
    TestPojo pojo2 = new TestPojo("abc", null);

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  @Test
  public void matches_equalNotNullInSecondField_returnsTrue() throws Exception {
    TestPojo pojo1 = new TestPojo("abc", ImmutableMap.of("a", "aaa"));
    TestPojo pojo2 = new TestPojo("abc", ImmutableMap.of("a", "aaa"));

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(true));
  }

  @Test
  public void matches_unequalNotNullInSecondField_returnsFalse() throws Exception {
    TestPojo pojo1 = new TestPojo("abc", ImmutableMap.of("a", "aaa"));
    TestPojo pojo2 = new TestPojo("abc", ImmutableMap.of("a", "AAA"));

    boolean matches = invokeMatcher(pojo1, pojo2);

    assertThat(matches, is(false));
  }

  private boolean invokeMatcher(TestPojo pojo1, Object pojo2) {
    CfModelsAssert.DeepEqualityMatcher matcher =
        new CfModelsAssert.DeepEqualityMatcher<>(pojo1, Arrays.asList(TestPojo::getProperty1, TestPojo::getProperty2));
    return matcher.matches(pojo2);
  }

  private class TestPojo {
    private final String property1;
    private final Map<String, Object> property2;

    public TestPojo(String property1, Map<String, Object> property2) {
      this.property1 = property1;
      this.property2 = property2;
    }

    public String getProperty1() {
      return property1;
    }

    public Map<String, Object> getProperty2() {
      return property2;
    }
  }
}
