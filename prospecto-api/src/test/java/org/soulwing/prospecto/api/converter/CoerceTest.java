/*
 * File created on Apr 5, 2016
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
package org.soulwing.prospecto.api.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

/**
 * Unit tests for type coercion.
 *
 * @author Carl Harris
 */
public class CoerceTest {

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
  public void testCoerceBooleanToBooleanPrimitive() throws Exception {
    coerceAndValidate(boolean.class, Boolean.TRUE, true);
  }

  @Test
  public void testCoerceStringToBooleanPrimitive() throws Exception {
    coerceAndValidate(boolean.class, Boolean.TRUE.toString(), true);
  }

  @Test
  public void testCoerceStringToBoolean() throws Exception {
    coerceAndValidate(Boolean.class, Boolean.TRUE.toString(), Boolean.TRUE);
  }

  @Test
  public void testCoerceNumberToDate() throws Exception {
    final Date date = new Date();
    coerceAndValidate(Date.class, date.getTime(), date);
  }

  @Test
  public void testCoerceStringToDate() throws Exception {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    coerceAndValidate(Date.class,
        DatatypeConverter.printDateTime(calendar), calendar.getTime());
  }

  @Test
  public void testCoerceNumberToCalendar() throws Exception {
    final Calendar calendar = Calendar.getInstance();
    coerceAndValidate(Calendar.class, calendar.getTimeInMillis(), calendar);
  }

  @Test
  public void testCoerceStringToCalendar() throws Exception {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    final Calendar actual = Coerce.toValueOfType(Calendar.class,
        DatatypeConverter.printDateTime(calendar));
    assertThat(actual.getTime(), is(equalTo(calendar.getTime())));
  }

  @Test
  public void testCoerceNumberToByte() throws Exception {
    coerceAndValidate(Byte.class, -1L, (byte) -1);
  }

  @Test
  public void testCoerceStringToByte() throws Exception {
    coerceAndValidate(Byte.class, "-1", (byte) -1);
  }

  @Test
  public void testCoerceNumberToBytePrimitive() throws Exception {
    coerceAndValidate(byte.class, -1L, (byte) -1);
  }

  @Test
  public void testCoerceStringToBytePrimitive() throws Exception {
    coerceAndValidate(byte.class, "-1", (byte) -1);
  }

  @Test
  public void testCoerceNumberToShort() throws Exception {
    coerceAndValidate(Short.class, -1L, (short) -1);
  }

  @Test
  public void testCoerceStringToShort() throws Exception {
    coerceAndValidate(Short.class, "-1", (short) -1);
  }

  @Test
  public void testCoerceNumberToShortPrimitive() throws Exception {
    coerceAndValidate(short.class, -1L, (short) -1);
  }

  @Test
  public void testCoerceStringToShortPrimitive() throws Exception {
    coerceAndValidate(short.class, "-1", (short) -1);
  }

  @Test
  public void testCoerceNumberToInteger() throws Exception {
    coerceAndValidate(Integer.class, -1L, -1);
  }

  @Test
  public void testCoerceStringToInteger() throws Exception {
    coerceAndValidate(Integer.class, "-1", -1);
  }

  @Test
  public void testCoerceNumberToIntegerPrimitive() throws Exception {
    coerceAndValidate(int.class, -1L, -1);
  }

  @Test
  public void testCoerceStringToIntegerPrimitive() throws Exception {
    coerceAndValidate(int.class, "-1", -1);
  }

  @Test
  public void testCoerceNumberToLong() throws Exception {
    coerceAndValidate(Long.class, -1L, -1L);
  }

  @Test
  public void testCoerceStringToLong() throws Exception {
    coerceAndValidate(Long.class, "-1", -1L);
  }

  @Test
  public void testCoerceNumberToLongPrimitive() throws Exception {
    coerceAndValidate(long.class, -1L, -1L);
  }

  @Test
  public void testCoerceStringToLongPrimitive() throws Exception {
    coerceAndValidate(long.class, "-1", -1L);
  }

  @Test
  public void testCoerceNumberToFloat() throws Exception {
    coerceAndValidate(Float.class, -1L, -1.0f);
  }

  @Test
  public void testCoerceStringToFloat() throws Exception {
    coerceAndValidate(Float.class, "-1.0", -1.0f);
  }

  @Test
  public void testCoerceNumberToFloatPrimitive() throws Exception {
    coerceAndValidate(float.class, -1L, -1.0f);
  }

  @Test
  public void testCoerceStringToFloatPrimitive() throws Exception {
    coerceAndValidate(float.class, "-1.0", -1.0f);
  }

  @Test
  public void testCoerceNumberToDouble() throws Exception {
    coerceAndValidate(Double.class, -1L, -1.0);
  }

  @Test
  public void testCoerceStringToDouble() throws Exception {
    coerceAndValidate(Double.class, "-1.0", -1.0);
  }

  @Test
  public void testCoerceNumberToDoublePrimitive() throws Exception {
    coerceAndValidate(double.class, -1L, -1.0);
  }

  @Test
  public void testCoerceStringToDoublePrimitive() throws Exception {
    coerceAndValidate(double.class, "-1.0", -1.0);
  }

  @Test
  public void testCoerceNumberToBigInteger() throws Exception {
    coerceAndValidate(BigInteger.class, -1L, BigInteger.ONE.negate());
  }

  @Test
  public void testCoerceStringToBigInteger() throws Exception {
    coerceAndValidate(BigInteger.class, "-1", BigInteger.ONE.negate());
  }

  @Test
  public void testCoerceIntegralNumberToBigDecimal() throws Exception {
    coerceAndValidate(BigDecimal.class, -1L, BigDecimal.ONE.negate());
  }

  @Test
  public void testCoerceFloatingPointNumberToBigDecimal() throws Exception {
    coerceAndValidate(BigDecimal.class, -1.0, BigDecimal.valueOf(1.0).negate());
  }

  @Test
  public void testCoerceStringToBigDecimal() throws Exception {
    coerceAndValidate(BigDecimal.class, "-1.0", BigDecimal.valueOf(1.0).negate());
  }

  @Test
  public void testCoerceStringUUID() throws Exception {
    final UUID uuid = UUID.randomUUID();
    coerceAndValidate(String.class, uuid, uuid.toString());
  }

  @Test
  public void testCoerceObjectToValueTypeUsingValueOf() throws Exception {
    final MockValueTypeWithValueOf value =
        new MockValueTypeWithValueOf("value");
    coerceAndValidate(MockValueTypeWithValueOf.class, "value", value);
  }

  @Test
  public void testCoerceObjectToValueTypeUsingConstructor() throws Exception {
    final MockValueTypeWithConstructor value =
        new MockValueTypeWithConstructor("value");
    coerceAndValidate(MockValueTypeWithConstructor.class, "value", value);
  }

  @SuppressWarnings("unchecked")
  private <T> void coerceAndValidate(Class<T> type, Object value, T expected)
      throws Exception {
    final T actual = Coerce.toValueOfType(type, value);
    assertThat(actual, is(instanceOf(type)));
    assertThat(actual, is(equalTo(expected)));
  }

  enum MockEnum {
    VALUE
  }

  static class MockValueTypeWithConstructor {
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
      return obj == this
          || obj instanceof MockValueTypeWithConstructor
          && Objects.equals(this.value,
          ((MockValueTypeWithConstructor) obj).value);
    }
  }

  static class MockValueTypeWithValueOf {
    private final Object value;

    @SuppressWarnings("unused")
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
      return obj == this
          || obj instanceof MockValueTypeWithValueOf
          && Objects.equals(this.value,
          ((MockValueTypeWithValueOf) obj).value);
    }
  }

}
