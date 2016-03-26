/*
 * File created on Mar 15, 2016
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
package org.soulwing.prospecto.runtime.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@link ConverterSupport}.
 *
 * @author Carl Harris
 */
public class ConverterSupportTest {

  private static final Object MODEL_VALUE = new Object();

  private static final Object VIEW_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private ViewNode node;

  @Mock
  private ValueTypeConverter<Object> converter;

  @Test
  public void testToViewValueWithLocalConverter() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(ValueTypeConverter.class);
        will(returnValue(converter));
        oneOf(converter).toValue(MODEL_VALUE);
        will(returnValue(VIEW_VALUE));
      }
    });

    assertThat(ConverterSupport.toViewValue(MODEL_VALUE, node, viewContext),
        is(equalTo(VIEW_VALUE)));
  }

  @Test
  public void testToViewValueWithContextConverter() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(ValueTypeConverter.class);
        will(returnValue(null));
        oneOf(viewContext).getValueTypeConverters();
        will(returnValue(Collections.singletonList(converter)));
        oneOf(converter).supports(Object.class);
        will(returnValue(true));
        oneOf(converter).toValue(MODEL_VALUE);
        will(returnValue(VIEW_VALUE));
      }
    });

    assertThat(ConverterSupport.toViewValue(MODEL_VALUE, node, viewContext),
        is(equalTo(VIEW_VALUE)));
  }

  @Test
  public void testCoerceToSameType() throws Exception {
    final Object value = new Object();
    coerceAndValidate(Object.class, value, value);
  }

  @Test
  public void testCoerceStringToEnum() throws Exception {
    coerceAndValidate(MockEnum.class, MockEnum.VALUE.name(), MockEnum.VALUE);
  }

  @Test
  public void testCoerceNumberToDate() throws Exception {
    final Date date = new Date();
    coerceAndValidate(Date.class, date.getTime(), date);
  }

  @Test
  public void testCoerceNumberToByte() throws Exception {
    coerceAndValidate(Byte.class, -1L, (byte) -1);
  }

  @Test
  public void testCoerceNumberToBytePrimitive() throws Exception {
    coerceAndValidate(byte.class, -1L, (byte) -1);
  }

  @Test
  public void testCoerceNumberToShort() throws Exception {
    coerceAndValidate(Short.class, -1L, (short) -1);
  }

  @Test
  public void testCoerceNumberToShortPrimitive() throws Exception {
    coerceAndValidate(short.class, -1L, (short) -1);
  }

  @Test
  public void testCoerceNumberToInteger() throws Exception {
    coerceAndValidate(Integer.class, -1L, -1);
  }

  @Test
  public void testCoerceNumberToIntegerPrimitive() throws Exception {
    coerceAndValidate(int.class, -1L, -1);
  }

  @Test
  public void testCoerceNumberToLong() throws Exception {
    coerceAndValidate(Long.class, -1L, -1L);
  }

  @Test
  public void testCoerceNumberToLongPrimitive() throws Exception {
    coerceAndValidate(long.class, -1L, -1L);
  }

  @Test
  public void testCoerceNumberToFloat() throws Exception {
    coerceAndValidate(Float.class, -1L, -1.0f);
  }

  @Test
  public void testCoerceNumberToFloatPrimitive() throws Exception {
    coerceAndValidate(float.class, -1L, -1.0f);
  }

  @Test
  public void testCoerceNumberToDouble() throws Exception {
    coerceAndValidate(Double.class, -1L, -1.0);
  }

  @Test
  public void testCoerceNumberToDoublePrimitive() throws Exception {
    coerceAndValidate(double.class, -1L, -1.0);
  }

  @Test
  public void testCoerceNumberToBigInteger() throws Exception {
    coerceAndValidate(BigInteger.class, -1L, BigInteger.ONE.negate());
  }

  @Test
  public void testCoerceNumberToBigDecimal() throws Exception {
    coerceAndValidate(BigDecimal.class, -1L, BigDecimal.ONE.negate());
  }

  @Test
  public void testCoerceBoolean() throws Exception {
    coerceAndValidate(Boolean.class, true, true);
  }

  @Test
  public void testCoerceObjectToValueTypeUsingValueOf() throws Exception {
    final MockValueTypeWithValueOf value =
        new MockValueTypeWithValueOf("value");
    coerceAndValidate(MockValueTypeWithValueOf.class, "value", value);
  }

  @Test
  public void testCoerceObjectToValueTypeUsingConstructor() throws Exception {
    final MockValueTypeWithValueOf value =
        new MockValueTypeWithValueOf("value");
    coerceAndValidate(MockValueTypeWithValueOf.class, "value", value);
  }

  @SuppressWarnings("unchecked")
  private <T> void coerceAndValidate(Class<T> type, Object value, T expected)
      throws Exception {
    context.checking(converterExpectations());
    final Object actual = ConverterSupport.toModelValue(type, value, node,
        viewContext);
    assertThat(actual, is(instanceOf(type)));
    assertThat((T) actual, is(equalTo(expected)));
  }

  private Expectations converterExpectations() {
    return new Expectations() {
      {
        oneOf(node).get(ValueTypeConverter.class);
        will(returnValue(null));
        oneOf(viewContext).getValueTypeConverters();
        will(returnValue(Collections.emptyList()));
      }
    };
  }

  enum MockEnum {
    VALUE
  }

  public static class MockValueTypeWithConstructor {
    private final Object value;

    public MockValueTypeWithConstructor(Object value) {
      this.value = value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) return true;
      if (!(obj instanceof MockValueTypeWithConstructor)) return false;
      return Objects.equals(this.value,
          ((MockValueTypeWithConstructor) obj).value);
    }
  }

  public static class MockValueTypeWithValueOf {
    private final Object value;

    public static MockValueTypeWithValueOf valueOf(Object value) {
      return new MockValueTypeWithValueOf(value);
    }

    private MockValueTypeWithValueOf(Object value) {
      this.value = value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) return true;
      if (!(obj instanceof MockValueTypeWithValueOf)) return false;
      return Objects.equals(this.value,
          ((MockValueTypeWithValueOf) obj).value);
    }
  }

}
