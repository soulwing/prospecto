/*
 * File created on Jun 13, 2021
 *
 * Copyright (c) 2021 Carl Harris, Jr
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Optional;

import org.soulwing.prospecto.api.AccessMode;

/**
 * A property access that handles an optional model property.
 * <p>
 * Optional properties have a getter that returns {@code Optional<T>} and a
 * setter that takes a single argument of type {@code T}. These are handled
 * as a special case in {@link ReflectionAccessorFactory} which returns an
 * instance of this type to allow the optional return value of the getter to
 * be handled transparently.
 *
 * @author Carl Harris
 */
class OptionalPropertyAccessor extends PropertyAccessor {

  private final boolean optionalSetterArg;

  OptionalPropertyAccessor(Class<?> modelType,
      String name, Method getter, Method setter,
      EnumSet<AccessMode> supportedModes) {
    super(modelType, name, getter, setter, supportedModes);
    this.optionalSetterArg = setter != null
        && Optional.class.isAssignableFrom(setter.getParameterTypes()[0]);
  }

  @Override
  protected Object onGet(Object source) throws IllegalAccessException,
      InvocationTargetException {
    return ((Optional<?>) super.onGet(source)).orElse(null);
  }

  @Override
  protected void onSet(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (optionalSetterArg) {
      value = Optional.ofNullable(value);
    }
    super.onSet(target, value);
  }

}
