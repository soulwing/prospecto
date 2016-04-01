/*
 * File created on Apr 1, 2016
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;

import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.runtime.util.SimpleLinkedList;

/**
 * A {@link ValueTypeConverterService} backed by a {@link LinkedList}.
 *
 * @author Carl Harris
 */
public class LinkedListValueTypeConverterService
    extends SimpleLinkedList<ValueTypeConverter<?>>
    implements ValueTypeConverterService {

  @Override
  public Object toViewValue(Object model, ViewNode node) throws Exception {
    if (model == null) return null;

    final ValueTypeConverter<?> localConverter = node.get(ValueTypeConverter.class);
    if (localConverter != null) {
      return localConverter.toValue(model);
    }

    final ValueTypeConverter<?> converter = findConverter(model.getClass());
    if (converter != null) {
      return converter.toValue(model);
    }

    return model;
  }

  @Override
  public Object toModelValue(Class<?> type, Object value, ViewNode node)
      throws Exception {
    if (value == null) return null;

    final ValueTypeConverter<?> localConverter = node.get(ValueTypeConverter.class);
    if (localConverter != null) {
      return localConverter.toObject(coerce(value, localConverter.getViewType()));
    }

    final ValueTypeConverter<?> converter = findConverter(type);
    if (converter != null) {
      return converter.toObject(coerce(value, converter.getViewType()));
    }

    return coerce(value, type);
  }

  /**
   * Coerces a view value to a model value type.
   * @param value view value
   * @param type model value type
   * @return coerced value
   * @throws IllegalArgumentException if the value cannot be coerced
   */
  private static Object coerce(Object value, Class<?> type) throws Exception {
    assert value != null;
    if (type.isInstance(value)) return value;
    if (boolean.class.isAssignableFrom(type) && value instanceof Boolean) {
      return value;
    }
    if (Enum.class.isAssignableFrom(type) && value instanceof String) {
      final Method method = type.getMethod("valueOf", String.class);
      return method.invoke(type, value);
    }
    if (Date.class.isAssignableFrom(type) && value instanceof Number) {
      final Constructor<Date> constructor = Date.class.getConstructor(long.class);
      return constructor.newInstance(((Number) value).longValue());
    }
    if ((Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type))
        && value instanceof Number) {
      return ((Number) value).byteValue();
    }
    if ((Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type))
        && value instanceof Number) {
      return ((Number) value).shortValue();
    }
    if ((Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type))
        && value instanceof Number) {
      return ((Number) value).intValue();
    }
    if ((Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type))
        && value instanceof Number) {
      return ((Number) value).longValue();
    }
    if (BigInteger.class.isAssignableFrom(type) && value instanceof Number) {
      return BigInteger.valueOf(((Number) value).longValue());
    }
    if ((Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type))
        && value instanceof Number) {
      return ((Number) value).floatValue();
    }
    if ((Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type))
        && value instanceof Number) {
      return ((Number) value).doubleValue();
    }
    if (BigDecimal.class.isAssignableFrom(type) && value instanceof Number) {
      return new BigDecimal(((Number) value).doubleValue());
    }

    Object result = coerceUsingValueOf(type, value);
    if (result != null) return result;

    result = coerceUsingConstructor(type, value);
    if (result != null) return result;

    throw new IllegalArgumentException("cannot coerce value of type "
        + value.getClass().getName() + " to type " + type.getName());
  }

  private static Object coerceUsingConstructor(Class<?> type, Object value) {
    Class<?> valueType = value.getClass();
    while (valueType != null) {
      try {
        final Constructor constructor = type.getConstructor(valueType);
        return constructor.newInstance(value);
      }
      catch (InstantiationException
          | NoSuchMethodException
          | InvocationTargetException
          | IllegalAccessException ex) {
        valueType = valueType.getSuperclass();
      }
    }
    return null;
  }

  private static Object coerceUsingValueOf(Class<?> type, Object value) {
    Class<?> valueType = value.getClass();
    while (valueType != null) {
      try {
        final Method valueOfMethod = type.getMethod("valueOf", valueType);
        if (type.isAssignableFrom(valueOfMethod.getReturnType())) {
          return valueOfMethod.invoke(type, value);
        }
      }
      catch (IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException ex) {
        assert true;
      }
      valueType = valueType.getSuperclass();
    }
    return null;
  }

  private ValueTypeConverter<?> findConverter(Class<?> type) {
    for (final ValueTypeConverter<?> converter : toList()) {
      if (converter.supports(type)) {
        return converter;
      }
    }
    return null;
  }

}
