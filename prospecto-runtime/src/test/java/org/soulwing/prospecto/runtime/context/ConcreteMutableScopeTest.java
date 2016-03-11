/*
 * File created on Mar 11, 2016
 *
 * Copyright (c) 2016 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.prospecto.runtime.context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for {@link ConcreteMutableScope}.
 *
 * @author Carl Harris
 */
public class ConcreteMutableScopeTest {

  private ConcreteMutableScope scope = new ConcreteMutableScope();

  @Test
  public void testPutAndGetByType() throws Exception {
    final String obj = "object";
    assertThat(scope.get(String.class), is(nullValue()));
    scope.put(obj);
    assertThat(scope.get(String.class), is(sameInstance(obj)));
  }

  @Test
  public void testPutAndGetByName() throws Exception {
    final String name = "name";
    final String obj = "object";
    assertThat(scope.get(name, String.class), is(nullValue()));
    assertThat(scope.get(String.class), is(nullValue()));
    scope.put(name, obj);
    assertThat(scope.get(name, String.class), is(sameInstance(obj)));
    assertThat(scope.get(String.class), is(sameInstance(obj)));
  }

  @Test
  public void testPutAndGetMultipleInstancesSameType() throws Exception {
    final String name0 = "name0";
    final String name1 = "name1";

    final String obj0 = "object0";
    final String obj1 = "object1";

    scope.put(name0, obj0);
    scope.put(name1, obj1);
    assertThat(scope.get(name0, String.class), is(sameInstance(obj0)));
    assertThat(scope.get(name1, String.class), is(sameInstance(obj1)));

    try {
      scope.get(String.class);
    }
    catch (IllegalStateException ex) {
      assertTrue(true);
    }
  }

  @Test
  public void testPutAllIterable() throws Exception {
    scope.putAll(Arrays.asList("string", 1, true));
    assertThat(scope.get(String.class), is(not(nullValue())));
    assertThat(scope.get(Integer.class), is(not(nullValue())));
    assertThat(scope.get(Boolean.class), is(not(nullValue())));
  }

  @Test
  public void testPutAllMap() throws Exception {
    Map<String, Object> map = new HashMap<>();
    map.put("name0", "string");
    map.put("name1", 1);
    map.put("name2", true);
    scope.putAll(map);
    assertThat(scope.get(String.class), is(not(nullValue())));
    assertThat(scope.get("name0", String.class), is(not(nullValue())));
    assertThat(scope.get(Integer.class), is(not(nullValue())));
    assertThat(scope.get("name1", Integer.class), is(not(nullValue())));
    assertThat(scope.get(Boolean.class), is(not(nullValue())));
    assertThat(scope.get("name2", Boolean.class), is(not(nullValue())));
  }

  @Test
  public void testRemove() throws Exception {
    final String name0 = "name0";
    final String obj0 = "object0";
    final Integer obj1 = -1;

    scope.put(name0, obj0);
    scope.put(obj1);

    assertThat(scope.get(name0, String.class), is(sameInstance(obj0)));
    assertThat(scope.get(String.class), is(sameInstance(obj0)));
    assertThat(scope.get(Integer.class), is(sameInstance(obj1)));

    assertThat(scope.remove(obj0), is(true));
    assertThat(scope.get(name0, String.class), is(nullValue()));
    assertThat(scope.get(String.class), is(nullValue()));

    assertThat(scope.remove(obj1), is(true));
    assertThat(scope.get(String.class), is(nullValue()));
    assertThat(scope.get(Integer.class), is(nullValue()));
  }

}
