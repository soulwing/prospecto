/*
 * File created on Mar 31, 2016
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

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;

/**
 * An accessor for a root object.
 *
 * @author Carl Harris
 */
public class RootAccessor extends AbstractAccessor {

  public static final Accessor INSTANCE = new RootAccessor();

  private RootAccessor() {
    super(Object.class, null, null,
        EnumSet.allOf(AccessMode.class),
        EnumSet.of(AccessMode.READ));
  }

  @Override
  protected Accessor newAccessor(Class<?> type, String name)
      throws Exception {
    return this;
  }

  @Override
  protected Object onGet(Object source)
      throws IllegalAccessException, InvocationTargetException {
    return source;
  }

  @Override
  protected void onSet(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Class<?> getDataType() {
    return Object.class;
  }

}
