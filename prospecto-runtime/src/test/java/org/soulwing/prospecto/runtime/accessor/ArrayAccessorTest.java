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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.EnumSet;

import org.junit.Test;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;

/**
 * Unit tests for {@link ArrayAccessor}.
 *
 * @author Carl Harris
 */
public class ArrayAccessorTest {

  private MockAccessor delegate = new MockAccessor();
  private ArrayAccessor accessor = new ArrayAccessor(delegate);

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testAddIndexValueBelowRange() throws Exception {
    delegate.array = new Object[0];
    accessor.add(null, -1, new Object());
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testAddIndexValueAboveRange() throws Exception {
    delegate.array = new Object[0];
    accessor.add(null, 1, new Object());
  }

  @Test
  public void testAddIndexValueEmptyArray() throws Exception {
    final Object value = new Object();
    delegate.array = new Object[0];
    accessor.add(null, 0, value);
    assertThat(delegate.array, is(equalTo(new Object[] { value })));
  }

  @Test
  public void testAddIndexValueStartOfArray() throws Exception {
    final Object value0 = new Object();
    final Object value1 = new Object();
    delegate.array = new Object[] { value1 };
    accessor.add(null, 0, value0);
    assertThat(delegate.array, is(equalTo(new Object[] { value0, value1 })));
  }

  @Test
  public void testAddIndexValueEndOfArray() throws Exception {
    final Object value0 = new Object();
    final Object value1 = new Object();
    delegate.array = new Object[] { value0 };
    accessor.add(null, 1, value1);
    assertThat(delegate.array, is(equalTo(new Object[] { value0, value1 })));
  }

  @Test
  public void testAddIndexValueMiddleOfArray() throws Exception {
    final Object value0 = new Object();
    final Object value1 = new Object();
    final Object value2 = new Object();
    delegate.array = new Object[] { value0, value2 };
    accessor.add(null, 1, value1);
    assertThat(delegate.array, is(equalTo(new Object[] { value0, value1, value2 })));
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testRemoveIndexBelowRange() throws Exception {
    delegate.array = new Object[0];
    accessor.remove(null, -1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testRemoveIndexAboveRange() throws Exception {
    delegate.array = new Object[0];
    accessor.remove(null, 0);
  }

  @Test
  public void testRemoveIndexLastElement() throws Exception {
    final Object value0 = new Object();
    delegate.array = new Object[] { value0 };
    accessor.remove(null, 0);
    assertThat(delegate.array, is(equalTo(new Object[0])));
  }

  @Test
  public void testRemoveIndexStartOfArray() throws Exception {
    final Object value0 = new Object();
    final Object value1 = new Object();
    delegate.array = new Object[] { value0, value1 };
    accessor.remove(null, 0);
    assertThat(delegate.array, is(equalTo(new Object[] { value1 })));
  }

  @Test
  public void testRemoveIndexEndOfArray() throws Exception {
    final Object value0 = new Object();
    final Object value1 = new Object();
    delegate.array = new Object[] { value0, value1 };
    accessor.remove(null, 1);
    assertThat(delegate.array, is(equalTo(new Object[] { value0 })));
  }

  @Test
  public void testRemoveIndexMiddleOfArray() throws Exception {
    final Object value0 = new Object();
    final Object value1 = new Object();
    final Object value2 = new Object();
    delegate.array = new Object[] { value0, value1, value2 };
    accessor.remove(null, 1);
    assertThat(delegate.array, is(equalTo(new Object[] { value0, value2 })));
  }


  static class MockAccessor implements Accessor {
    private Object[] array;
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
    public EnumSet<AccessMode> getAccessModes() {
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
      return null;
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
      array = (Object[]) value;
    }

  }

}
