/*
 * File created on Mar 16, 2016
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.Test;
import org.soulwing.prospecto.api.AccessType;

/**
 * Unit tests for {@link ReflectionAccessorFactory}.
 *
 * @author Carl Harris
 */
public class ReflectionAccessorFactoryTest {

  private static final Object PRIVATE_FIELD_VALUE = new Object();
  private static final Object PUBLIC_FIELD_VALUE = new Object();
  private static final Object PUBLIC_METHOD_VALUE = new Object();
  private static final Object PRIVATE_METHOD_VALUE = new Object();

  private ReflectionAccessorFactory accessorFactory =
      new ReflectionAccessorFactory();

  private MockModel model = new MockModel();

  @Test
  public void testPrivateFieldAccessor() throws Exception {
    final Accessor accessor = accessorFactory.newAccessor(MockModel.class,
        "privateField", AccessType.FIELD);
    assertThat(accessor.get(model), is(sameInstance(PRIVATE_FIELD_VALUE)));
  }

  @Test
  public void testPublicFieldAccessor() throws Exception {
    final Accessor accessor = accessorFactory.newAccessor(MockModel.class,
        "publicField", AccessType.FIELD);
    assertThat(accessor.get(model), is(sameInstance(PUBLIC_FIELD_VALUE)));
  }

  @Test(expected = NoSuchFieldException.class)
  public void testProtectedFieldInSuperAccessor() throws Exception {
    // this isn't supported, but it could be; just need to go up the class
    // ancestors. Does anyone care?
    final Accessor accessor = accessorFactory.newAccessor(MockModel.class,
        "protectedFieldInSuper", AccessType.FIELD);
  }

  @Test(expected = NoSuchFieldException.class)
  public void testPublicFieldInSuperAccessor() throws Exception {
    // this isn't supported, but it could be; just need to go up the class
    // ancestors. Does anyone care?
    final Accessor accessor = accessorFactory.newAccessor(MockModel.class,
        "publicFieldInSuper", AccessType.FIELD);
  }

  @Test
  public void testPublicMethodAccessor() throws Exception {
    final Accessor accessor = accessorFactory.newAccessor(MockModel.class,
        "publicMethod", AccessType.PROPERTY);
    assertThat(accessor.get(model), is(sameInstance(PUBLIC_METHOD_VALUE)));
  }

  @Test(expected = NoSuchMethodException.class)
  public void testPrivateMethodAccessor() throws Exception {
    // this isn't supported but could be; need to do our own accessor method
    // introspection. Does anyone care?
    final Accessor accessor = accessorFactory.newAccessor(MockModel.class,
        "privateMethod", AccessType.PROPERTY);
    assertThat(accessor.get(model), is(sameInstance(PRIVATE_METHOD_VALUE)));
  }

  abstract class MockSuperModel {

    protected Object protectedFieldInSuper = new Object();

    public Object publicFieldInSuper = new Object();


  }

  public class MockModel extends MockSuperModel {

    private Object privateField = PRIVATE_FIELD_VALUE;
    public Object publicField = PUBLIC_FIELD_VALUE;

    Object getPrivateMethod() {
      return PRIVATE_METHOD_VALUE;
    }

    public Object getPublicMethod() {
      return PUBLIC_METHOD_VALUE;
    }

    public void setPublicMethod(Object value) { }

  }

}
