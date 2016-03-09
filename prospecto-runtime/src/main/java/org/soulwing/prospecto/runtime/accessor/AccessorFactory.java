/*
 * File created on Mar 9, 2016
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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ValueConverter;

/**
 * Static factory methods that produce accessors.
 *
 * @author Carl Harris
 */
public class AccessorFactory {

  public static Accessor accessor(Class<?> declaringClass, String name,
      AccessType accessType) throws NoSuchFieldException, NoSuchMethodException,
      IntrospectionException {
    switch (accessType) {
      case FIELD:
        return field(declaringClass, name);
      case PROPERTY:
        return property(declaringClass, name);
      default:
        throw new IllegalArgumentException("unrecognized access type: "
            + accessType.name());
    }
  }

  public static Accessor field(Class<?> declaringClass, String name)
      throws NoSuchFieldException {
    return new FieldAccessor(declaringClass.getDeclaredField(name));
  }

  public static Accessor property(Class<?> declaringClass, String name)
      throws NoSuchMethodException, IntrospectionException {
    for (final PropertyDescriptor descriptor :
        Introspector.getBeanInfo(declaringClass).getPropertyDescriptors()) {
      if (descriptor.getName().equals(name)) {
        return new PropertyAccessor(descriptor.getReadMethod(),
            descriptor.getWriteMethod());
      }
    }
    throw new IllegalArgumentException(declaringClass.getName()
        + " has no property named '" + name + "'");
  }

  public static Accessor converter(Accessor accessor,
      Class<? extends ValueConverter> converterClass)
      throws InstantiationException, IllegalAccessException {
    final ValueConverter converter = converterClass.newInstance();
    return new ValueConvertingAccessor(accessor, converter);
  }

  public static MultiValuedAccessor multiValue(Accessor accessor) {
    if (Collection.class.isAssignableFrom(accessor.getDataType())) {
      return new CollectionAccessor(accessor);
    }
    if (Object[].class.isAssignableFrom(accessor.getDataType())) {
      return new ArrayAccessor(accessor);
    }
    throw new IllegalArgumentException("expected an array or a collection");
  }

}
