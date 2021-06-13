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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Optional;

import org.junit.Test;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplateException;

/**
 * Unit tests for {@link ReflectionAccessorBuilder}.
 *
 * @author Carl Harris
 */
public class ReflectionAccessorBuilderTest {

  private static final Object PRIVATE_FIELD_VALUE = new Object();
  private static final Object PUBLIC_FIELD_VALUE = new Object();
  private static final Object PUBLIC_METHOD_VALUE = new Object();
  private static final Object PRIVATE_METHOD_VALUE = new Object();

  private static final String OPTIONAL_VALUE = "optionalValue";

  private ReflectionAccessorBuilder accessorBuilder =
      new ReflectionAccessorBuilder(MockModel.class);

  private MockModel model = new MockModel();

  @Test
  public void testPrivateFieldAccessor() throws Exception {
    final Accessor accessor = accessorBuilder
        .propertyName("privateField")
        .accessType(AccessType.FIELD)
        .build();
    assertThat(accessor.get(model), is(sameInstance(PRIVATE_FIELD_VALUE)));
  }

  @Test
  public void testPublicFieldAccessor() throws Exception {
    final Accessor accessor = accessorBuilder
        .propertyName("publicField")
        .accessType(AccessType.FIELD)
        .build();
    assertThat(accessor.get(model), is(sameInstance(PUBLIC_FIELD_VALUE)));
  }

  @Test
  public void testProtectedFieldInSuperAccessor() throws Exception {
    final Accessor accessor = accessorBuilder
        .propertyName("privateFieldInSuper")
        .accessType(AccessType.FIELD)
        .build();
    assertThat(accessor.get(model), is(sameInstance(PRIVATE_FIELD_VALUE)));
  }

  @Test
  public void testPublicFieldInSuperAccessor() throws Exception {
    final Accessor accessor = accessorBuilder
        .propertyName("publicFieldInSuper")
        .accessType(AccessType.FIELD)
        .build();
    assertThat(accessor.get(model), is(sameInstance(PUBLIC_FIELD_VALUE)));
  }

  @Test
  public void testPublicMethodAccessor() throws Exception {
    final Accessor accessor = accessorBuilder
        .propertyName("publicMethod")
        .accessType(AccessType.PROPERTY)
        .build();

    assertThat(accessor.get(model), is(sameInstance(PUBLIC_METHOD_VALUE)));
  }

  @Test(expected = ViewTemplateException.class)
  public void testPrivateMethodAccessor() throws Exception {
    // this isn't supported but could be; need to do our own accessor method
    // introspection. Does anyone care?
    accessorBuilder
        .propertyName("privateMethod")
        .accessType(AccessType.PROPERTY)
        .build();
  }

  @SuppressWarnings("unused")
  abstract class MockSuperModel {

    private Object privateFieldInSuper = PRIVATE_FIELD_VALUE;

    public Object publicFieldInSuper = PUBLIC_FIELD_VALUE;

  }

  @SuppressWarnings("unused")
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

  @Test
  public void testPublicInterfaceMethod() throws Exception {
    final ReflectionAccessorBuilder accessorBuilder =
        new ReflectionAccessorBuilder(IMockModel.class);
    final IMockModel model = new MockModelImpl();

    final Accessor accessor = accessorBuilder
        .propertyName("publicMethod")
        .accessType(AccessType.PROPERTY)
        .build();

    assertThat(accessor.get(model), is(sameInstance(PUBLIC_METHOD_VALUE)));
  }

  @Test
  public void testPublicExtendedInterfaceMethod() throws Exception {
    final ReflectionAccessorBuilder accessorBuilder =
        new ReflectionAccessorBuilder(IMockSubModel.class);
    final IMockSubModel model = new MockSubModelImpl();

    final Accessor accessor = accessorBuilder
        .propertyName("publicMethod")
        .accessType(AccessType.PROPERTY)
        .build();

    assertThat(accessor.get(model), is(sameInstance(PUBLIC_METHOD_VALUE)));

    final Object other = new Object();
    accessor.set(model, other);
    assertThat(accessor.get(model), is(sameInstance(other)));
  }

  public interface IMockModel {
    Object getPublicMethod();
  }

  public interface IMockSubModel extends IMockModel {
    void setPublicMethod(Object value);
  }

  public class MockModelImpl implements IMockModel {


    @Override
    public Object getPublicMethod() {
      return PUBLIC_METHOD_VALUE;
    }

  }

  public class MockSubModelImpl implements IMockSubModel {

    private Object value = PUBLIC_METHOD_VALUE;

    @Override
    public Object getPublicMethod() {
      return value;
    }

    @Override
    public void setPublicMethod(Object value) {
      this.value = value;
    }

  }

  @Test
  public void testPublicExtendedInterfaceMethodWithOptional() throws Exception {
    final ReflectionAccessorBuilder accessorBuilder =
        new ReflectionAccessorBuilder(IMockModelWithOptional.class);
    final IMockModelWithOptional model = new MockModelWithOptional();

    final Accessor accessor = accessorBuilder
        .propertyName("publicMethod")
        .accessType(AccessType.PROPERTY)
        .build();

    assertThat(accessor.get(model), is(sameInstance(OPTIONAL_VALUE)));
  }

  @Test
  public void testPublicSubInterfaceMethodWithOptional() throws Exception {
    final ReflectionAccessorBuilder accessorBuilder =
        new ReflectionAccessorBuilder(IMockSubModelWithOptional.class);
    final IMockSubModelWithOptional model = new MockModelWithOptional();

    final Accessor accessor = accessorBuilder
        .propertyName("publicMethod")
        .accessType(AccessType.PROPERTY)
        .build();

    assertThat(accessor.get(model), is(sameInstance(OPTIONAL_VALUE)));
    accessor.set(model, "other");
    assertThat(accessor.get(model), is(sameInstance("other")));
    accessor.set(model, null);
    assertThat(accessor.get(model), is(nullValue()));
  }

  public interface IMockModelWithOptional {
    Optional<String> getPublicMethod();
  }

  public interface IMockSubModelWithOptional extends IMockModelWithOptional {
    void setPublicMethod(String value);
  }

  public class MockModelWithOptional implements IMockSubModelWithOptional {

    private String publicValue = OPTIONAL_VALUE;

    @Override
    public Optional<String> getPublicMethod() {
      return Optional.ofNullable(publicValue);
    }

    public void setPublicMethod(String value) {
      this.publicValue = value;
    }
  }

}
