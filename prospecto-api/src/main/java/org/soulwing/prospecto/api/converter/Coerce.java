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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

/**
 * A type coercion utility.
 *
 * @author Carl Harris
 */
public class Coerce {

  /**
   * Coerces a view value to a model value type.
   * @param type model value type
   * @param value view value
   * @return coerced value
   * @throws IllegalArgumentException if the value cannot be coerced
   */
  @SuppressWarnings("unchecked")
  public static <T> T toValueOfType(Class<T> type, Object value) {
    if (value == null) return null;
    if (type == null) throw new NullPointerException("type is required");
    if (type.isInstance(value)) return (T) value;

    if (String.class.equals(type)) {
      return (T) value.toString();
    }

    if (boolean.class.isAssignableFrom(type)) {
      if (value instanceof Boolean) {
        return (T) value;
      }
      if (value instanceof String) {
        return (T) Boolean.valueOf((String) value);
      }
    }
    if (Enum.class.isAssignableFrom(type) && value instanceof String) {
      return coerceUsingValueOf(type, value);
    }
    if ((Integer.class.equals(type) || int.class.equals(type))) {
      if (value instanceof Number) {
        return (T) Integer.valueOf(((Number) value).intValue());
      }
      if (value instanceof String) {
        return (T) Integer.valueOf((String) value);
      }
    }
    if ((Long.class.equals(type) || long.class.equals(type))) {
      if (value instanceof Number) {
        return (T) Long.valueOf(((Number) value).longValue());
      }
      if (value instanceof String) {
        return (T) Long.valueOf((String) value);
      }
    }
    if ((Byte.class.equals(type) || byte.class.equals(type))) {
      if (value instanceof Number) {
        return (T) Byte.valueOf(((Number) value).byteValue());
      }
      if (value instanceof String) {
        return (T) Byte.valueOf((String) value);
      }
    }
    if ((Short.class.equals(type) || short.class.equals(type))) {
      if (value instanceof Number) {
        return (T) Short.valueOf(((Number) value).shortValue());
      }
      if (value instanceof String) {
        return (T) Short.valueOf((String) value);
      }
    }
    if (BigInteger.class.equals(type) && value instanceof Number) {
      return (T) BigInteger.valueOf(((Number) value).longValue());
    }
    if ((Double.class.equals(type) || double.class.equals(type))) {
      if (value instanceof Number) {
        return (T) Double.valueOf(((Number) value).doubleValue());
      }
      if (value instanceof String) {
        return (T) Double.valueOf((String) value);
      }
    }
    if ((Float.class.equals(type) || float.class.equals(type))) {
      if (value instanceof Number) {
        return (T) Float.valueOf(((Number) value).floatValue());
      }
      if (value instanceof String) {
        return (T) Float.valueOf((String) value);
      }
    }
    if (BigDecimal.class.equals(type)
        && (value instanceof Double || value instanceof Float)) {
      return (T) BigDecimal.valueOf(((Number) value).doubleValue());
    }
    if (BigDecimal.class.equals(type) && value instanceof Number) {
      return (T) BigDecimal.valueOf(((Number) value).longValue());
    }
    if (Date.class.isAssignableFrom(type)) {
      if (value instanceof Number) {
        return coerceUsingConstructor(type, ((Number) value).longValue());
      }
      if (value instanceof String) {
        return (T) DatatypeConverter.parseDateTime((String) value).getTime();
      }
    }
    if (Calendar.class.isAssignableFrom(type)) {
      if (value instanceof Number) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((Number) value).longValue());
        return (T) calendar;
      }
      if (value instanceof String) {
        return (T)  DatatypeConverter.parseDateTime((String) value);
      }
    }
    if (UUID.class.equals(type) && value instanceof String) {
      return (T) UUID.fromString((String) value);
    }

    T result = coerceUsingValueOf(type, value);
    if (result != null) return result;

    result = coerceUsingConstructor(type, value);
    if (result != null) return result;

    throw new IllegalArgumentException("cannot coerce value of type "
        + value.getClass().getName() + " to type " + type.getName());
  }

  private static <T> T coerceUsingConstructor(Class<T> type, Object value) {
    Class<?> valueType = value.getClass();
    while (valueType != null) {
      try {
        final Constructor<T> constructor = type.getConstructor(valueType);
        return constructor.newInstance(value);
      }
      catch (InstantiationException
          | NoSuchMethodException
          | InvocationTargetException
          | IllegalAccessException ex) {
        valueType = nextType(valueType);
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private static <T> T coerceUsingValueOf(Class<T> type, Object value) {
    Class<?> valueType = value.getClass();
    while (valueType != null) {
      try {
        final Method valueOfMethod = type.getMethod("valueOf", valueType);
        if (type.isAssignableFrom(valueOfMethod.getReturnType())) {
          return (T) valueOfMethod.invoke(type, value);
        }
      }
      catch (IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException ex) {
        assert true;
      }
      valueType = nextType(valueType);
    }
    return null;
  }

  private static Class<?> nextType(Class<?> type) {
    if (type.isPrimitive()) {
      return null;
    }
    if (String.class.equals(type)) {
      return type.getSuperclass();
    }
    if (Boolean.class.equals(type)) {
      return boolean.class;
    }
    if (Integer.class.equals(type)) {
      return int.class;
    }
    if (Long.class.equals(type)) {
      return long.class;
    }
    if (Double.class.equals(type)) {
      return double.class;
    }
    if (Float.class.equals(type)) {
      return float.class;
    }
    if (Short.class.equals(type)) {
      return short.class;
    }
    if (Byte.class.equals(type)) {
      return byte.class;
    }
    return type.getSuperclass();
  }

}
