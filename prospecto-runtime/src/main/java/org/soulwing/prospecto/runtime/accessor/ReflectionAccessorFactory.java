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
import java.lang.reflect.Method;
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;

/**
 * An {@link AccessorFactory} that produces accessors using the Reflection API.
 *
 * @author Carl Harris
 */
public class ReflectionAccessorFactory implements AccessorFactory {

  @Override
  public Accessor newAccessor(Class<?> declaringClass, String name,
      AccessType accessType) throws Exception {
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

  private static Accessor field(Class<?> declaringClass, String name)
      throws NoSuchFieldException {
    return new FieldAccessor(declaringClass.getDeclaredField(name));
  }

  private static Accessor property(Class<?> declaringClass, String name)
      throws NoSuchMethodException, IntrospectionException {
    final EnumSet<AccessMode> accessModes = EnumSet.noneOf(AccessMode.class);

    for (final PropertyDescriptor descriptor :
        Introspector.getBeanInfo(declaringClass).getPropertyDescriptors()) {
      if (descriptor.getName().equals(name)) {
        Method readMethod = descriptor.getReadMethod();
        if (readMethod != null) {
          accessModes.add(AccessMode.READ);
        }

        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod != null) {
          accessModes.add(AccessMode.WRITE);
        }

        return new PropertyAccessor(readMethod, writeMethod, accessModes);
      }
    }
    throw new NoSuchMethodException(declaringClass.getName()
        + " has no property named '" + name + "'");
  }


}
