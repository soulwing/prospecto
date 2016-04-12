/*
 * File created on Mar 22, 2016
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
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.EnumSet;

import org.junit.Test;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.association.AssociationDescriptor;

/**
 * Unit tests for {@link ArrayAccessor}.
 *
 * @author Carl Harris
 */
public class ArrayAccessorTest {

  private MockAccessor delegate = new MockAccessor();
  private ArrayAccessor accessor = new ArrayAccessor(delegate, MockObject.class);


  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddIndexValueBelowRange() throws Exception {
    delegate.array = new MockObject[0];
    accessor.begin(delegate.array);
    accessor.add(null, -1, new MockObject());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddIndexValueAboveRange() throws Exception {
    delegate.array = new MockObject[0];
    accessor.begin(delegate.array);
    accessor.add(null, 1, new MockObject());
  }

  @Test
  public void testAddIndexValueEmptyArray() throws Exception {
    final MockObject value = new MockObject();
    delegate.array = new MockObject[0];
    accessor.begin(delegate.array);
    accessor.add(null, 0, value);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value)));
  }

  @Test
  public void testAddIndexValueStartOfArray() throws Exception {
    final MockObject value0 = new MockObject();
    final MockObject value1 = new MockObject();
    delegate.array = new MockObject[] { value1 };
    accessor.begin(delegate.array);
    accessor.add(null, 0, value0);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value0, value1)));
  }

  @Test
  public void testAddIndexValueEndOfArray() throws Exception {
    final MockObject value0 = new MockObject();
    final MockObject value1 = new MockObject();
    delegate.array = new MockObject[] { value0 };
    accessor.begin(delegate.array);
    accessor.add(null, 1, value1);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value0, value1)));
  }

  @Test
  public void testAddIndexValueMiddleOfArray() throws Exception {
    final MockObject value0 = new MockObject();
    final MockObject value1 = new MockObject();
    final MockObject value2 = new MockObject();
    delegate.array = new MockObject[] { value0, value2 };
    accessor.begin(delegate.array);
    accessor.add(null, 1, value1);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value0, value1, value2)));
  }

  @Test
  public void testAddValues() throws Exception {
    final MockObject value0 = new MockObject();
    final MockObject value1 = new MockObject();
    delegate.array = new MockObject[0];
    accessor.begin(delegate.array);
    accessor.add(null, value0);
    accessor.add(null, value1);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value0, value1)));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveIndexBelowRange() throws Exception {
    delegate.array = new MockObject[0];
    accessor.begin(delegate.array);
    accessor.remove(null, -1);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveIndexAboveRange() throws Exception {
    delegate.array = new MockObject[0];
    accessor.begin(delegate.array);
    accessor.remove(null, 0);
  }

  @Test
  public void testRemoveIndexLastElement() throws Exception {
    final MockObject value0 = new MockObject();
    delegate.array = new MockObject[] { value0 };
    accessor.begin(delegate.array);
    accessor.remove(null, 0);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(emptyArray()));
  }

  @Test
  public void testRemoveIndexStartOfArray() throws Exception {
    final MockObject value0 = new MockObject();
    final MockObject value1 = new MockObject();
    delegate.array = new MockObject[] { value0, value1 };
    accessor.begin(delegate.array);
    accessor.remove(null, 0);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value1)));
  }

  @Test
  public void testRemoveIndexEndOfArray() throws Exception {
    final MockObject value0 = new MockObject();
    final MockObject value1 = new MockObject();
    delegate.array = new MockObject[] { value0, value1 };
    accessor.begin(delegate.array);
    accessor.remove(null, 1);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value0)));
  }

  @Test
  public void testRemoveIndexMiddleOfArray() throws Exception {
    final MockObject value0 = new MockObject();
    final MockObject value1 = new MockObject();
    final MockObject value2 = new MockObject();
    delegate.array = new MockObject[] { value0, value1, value2 };
    accessor.begin(delegate.array);
    accessor.remove(null, 1);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(value0, value2)));
  }

  @Test
  public void testRemoveMockObject() throws Exception {
    final MockObject value = new MockObject();
    delegate.array = new MockObject[] { value };
    accessor.begin(delegate.array);
    accessor.remove(null, value);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(emptyArray()));
  }

  @Test
  public void testGet() throws Exception {
    final MockObject value = new MockObject();
    delegate.array = new MockObject[] { value };
    assertThat(accessor.get(null, 0), is(sameInstance((Object) value)));
  }

  @Test
  public void testSet() throws Exception {
    final MockObject value = new MockObject();
    final MockObject otherValue = new MockObject();
    delegate.array = new MockObject[] { value };
    accessor.begin(delegate.array);
    accessor.set(null, 0, otherValue);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(arrayContaining(otherValue)));
  }

  @Test
  public void testSetWithoutTransaction() throws Exception {
    final MockObject value = new MockObject();
    final MockObject otherValue = new MockObject();
    delegate.array = new MockObject[] { value };
    accessor.set(null, 0, otherValue);
    assertThat(delegate.array, is(arrayContaining(otherValue)));
  }

  @Test
  public void testClear() throws Exception {
    final MockObject value = new MockObject();
    delegate.array = new MockObject[] { value };
    accessor.begin(delegate.array);
    accessor.clear(null);
    accessor.end(delegate.array);
    assertThat(delegate.array, is(emptyArray()));
  }

  private static class MockObject {}

  private static class MockAccessor implements Accessor {
    private MockObject[] array;
    private boolean writable = true;
    private boolean readable = true;

    @Override
    public Class<?> getModelType() {
      return null;
    }

    @Override
    public String getName() {
      return null;
    }

    @Override
    public AccessType getAccessType() {
      return null;
    }

    @Override
    public EnumSet<AccessMode> getSupportedModes() {
      return null;
    }

    @Override
    public boolean canRead() {
      return readable;
    }

    @Override
    public boolean canWrite() {
      return writable;
    }

    @Override
    public Class<?> getDataType() {
      return MockObject[].class;
    }

    @Override
    public Accessor forSubtype(Class<?> subtype) throws Exception {
      return null;
    }

    @Override
    public Object get(Object source) throws Exception {
      if (!readable) throw new UnsupportedOperationException();
      return array;
    }

    @Override
    public void set(Object target, Object value) throws Exception {
      if (!writable) throw new UnsupportedOperationException();
      array = (MockObject[]) value;
    }

    @Override
    public boolean isSameAssociate(Object owner, ViewEntity associateEntity)
        throws Exception {
      return false;
    }

    @Override
    public MockObject newAssociate(Object owner, ViewEntity associateEntity)
        throws Exception {
      return null;
    }

    @Override
    public boolean supports(AssociationDescriptor descriptor) {
      return false;
    }
  }

}
