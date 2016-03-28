/*
 * File created on Mar 26, 2016
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
package org.soulwing.prospecto.runtime.accessor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

import org.junit.Test;
import org.soulwing.prospecto.api.AccessMode;

/**
 * Unit tests for {@link AbstractAccessor}.
 *
 * @author Carl Harris
 */
public class AbstractAccessorTest {

  @Test
  public void testGetAccessModesWhenSupportsReadAndWrite() throws Exception {
    final MockAccessor accessor = new MockAccessor(EnumSet.allOf(AccessMode.class));

    accessor.setAccessModes(EnumSet.noneOf(AccessMode.class));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.of(AccessMode.READ));
    assertThat(accessor.getAccessModes(), contains(AccessMode.READ));

    accessor.setAccessModes(EnumSet.of(AccessMode.WRITE));
    assertThat(accessor.getAccessModes(), contains(AccessMode.WRITE));

    accessor.setAccessModes(EnumSet.allOf(AccessMode.class));
    assertThat(accessor.getAccessModes(),
        containsInAnyOrder(AccessMode.READ, AccessMode.WRITE));
  }

  @Test
  public void testGetAccessModesWhenSupportsRead() throws Exception {
    final MockAccessor accessor = new MockAccessor(EnumSet.of(AccessMode.READ));

    accessor.setAccessModes(EnumSet.noneOf(AccessMode.class));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.of(AccessMode.READ));
    assertThat(accessor.getAccessModes(), contains(AccessMode.READ));

    accessor.setAccessModes(EnumSet.of(AccessMode.WRITE));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.allOf(AccessMode.class));
    assertThat(accessor.getAccessModes(), contains(AccessMode.READ));
  }

  @Test
  public void testGetAccessModesWhenSupportsWrite() throws Exception {
    final MockAccessor accessor = new MockAccessor(EnumSet.of(AccessMode.WRITE));

    accessor.setAccessModes(EnumSet.noneOf(AccessMode.class));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.of(AccessMode.READ));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.of(AccessMode.WRITE));
    assertThat(accessor.getAccessModes(), contains(AccessMode.WRITE));

    accessor.setAccessModes(EnumSet.allOf(AccessMode.class));
    assertThat(accessor.getAccessModes(), contains(AccessMode.WRITE));
  }

  @Test
  public void testGetAccessModesWhenSupportsNone() throws Exception {
    final MockAccessor accessor = new MockAccessor(EnumSet.noneOf(AccessMode.class));

    accessor.setAccessModes(EnumSet.noneOf(AccessMode.class));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.of(AccessMode.READ));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.of(AccessMode.WRITE));
    assertThat(accessor.getAccessModes(), is(empty()));

    accessor.setAccessModes(EnumSet.allOf(AccessMode.class));
    assertThat(accessor.getAccessModes(), is(empty()));
  }

  static class MockAccessor extends AbstractAccessor {

    protected MockAccessor(EnumSet<AccessMode> supportedAccessModes) {
      super(null, null, supportedAccessModes);
    }

    @Override
    protected Accessor newAccessor(Class<?> type, String name) throws Exception {
      return null;
    }

    @Override
    protected Object onGet(Object source) throws IllegalAccessException,
        InvocationTargetException {
      return null;
    }

    @Override
    protected void onSet(Object target, Object value)
        throws IllegalAccessException, InvocationTargetException {
    }

    @Override
    public Class<?> getDataType() {
      return null;
    }
  }
}
