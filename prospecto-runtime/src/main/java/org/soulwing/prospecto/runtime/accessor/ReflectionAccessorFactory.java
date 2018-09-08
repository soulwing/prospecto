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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;

/**
 * An {@link AccessorFactory} that produces accessors using the Reflection API.
 *
 * @author Carl Harris
 */
class ReflectionAccessorFactory {


  static Accessor field(Class<?> ownerClass, String name)
      throws NoSuchFieldException {
    return new FieldAccessor(ownerClass, name,
        findField(ownerClass, name));
  }

  private static Field findField(Class<?> ownerClass, String name)
      throws NoSuchFieldException {
    try {
      return ownerClass.getDeclaredField(name);
    }
    catch (NoSuchFieldException ex) {
      if (ownerClass.getSuperclass() != null) {
        return findField(ownerClass.getSuperclass(), name);
      }
      throw ex;
    }
  }

  static Accessor property(Class<?> declaringClass, String name)
      throws NoSuchMethodException, IntrospectionException {
    final EnumSet<AccessMode> supportedModes = EnumSet.noneOf(AccessMode.class);

    Method readMethod = null;
    Method writeMethod = null;

    final PropertyDescriptor descriptor =
        findDescriptor(declaringClass, name, AccessMode.READ);

    if (descriptor != null) {
      readMethod = descriptor.getReadMethod();
      writeMethod = descriptor.getWriteMethod();
    }

    if (writeMethod == null) {
      final PropertyDescriptor writeDescriptor =
          findDescriptor(declaringClass, name, AccessMode.WRITE);
      if (writeDescriptor != null) {
        writeMethod = writeDescriptor.getWriteMethod();
      }
    }

    if (readMethod == null && writeMethod == null) {
      throw new NoSuchMethodException(declaringClass.getName()
          + " has no property named '" + name + "'");
    }
    if (readMethod != null) {
      supportedModes.add(AccessMode.READ);
    }
    if (writeMethod != null) {
      supportedModes.add(AccessMode.WRITE);
    }

    return new PropertyAccessor(declaringClass, name, readMethod,
        writeMethod, supportedModes);
  }

  private static PropertyDescriptor findDescriptor(Class<?> type, String name,
      AccessMode accessMode)
      throws IntrospectionException {
    final PropertyDescriptor[] descriptors = Introspector.getBeanInfo(type)
        .getPropertyDescriptors();
    for (final PropertyDescriptor descriptor : descriptors) {
      if (descriptor.getName().equals(name)
          && satisfiesAccessMode(descriptor, accessMode)) {
        return descriptor;
      }
    }

    if (type.isInterface()) {
      for (final Class<?> interfaceType : type.getInterfaces()) {
        PropertyDescriptor descriptor =
            findDescriptor(interfaceType, name, AccessMode.READ);
        if (descriptor != null) return descriptor;
      }
    }

    return null;
  }

  private static boolean satisfiesAccessMode(PropertyDescriptor descriptor,
      AccessMode accessMode) {
    switch (accessMode) {
      case READ:
        return descriptor.getReadMethod() != null;
      case WRITE:
        return descriptor.getWriteMethod() != null;
      default:
        throw new AssertionError("unrecognized " + AccessMode.class.getSimpleName());
    }
  }


}
