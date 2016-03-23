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

import java.lang.reflect.Field;
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;

/**
 * An accessor that directly accesses fields via the Reflection API.
 *
 * @author Carl Harris
 */
class FieldAccessor extends AbstractAccessor {


  private final Field field;

  public FieldAccessor(String name, Field field) {
    super(name, EnumSet.allOf(AccessMode.class));
    this.field = field;
    field.setAccessible(true);
  }

  @Override
  public Class<?> getDataType() {
    return field.getType();
  }

  @Override
  protected Accessor newAccessor(Class<?> type, String name) throws Exception {
    return ReflectionAccessorFactory.field(type, name);
  }

  @Override
  public Object get(Object source) throws IllegalAccessException {
    return field.get(source);
  }

  @Override
  public void set(Object target, Object value) throws IllegalAccessException {
    field.set(target, value);
  }

}
