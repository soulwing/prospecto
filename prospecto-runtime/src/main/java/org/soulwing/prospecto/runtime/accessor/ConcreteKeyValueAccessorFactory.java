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

import java.util.Map;

import org.soulwing.prospecto.api.ViewTemplateException;

/**
 * A {@link KeyValueAccessorFactory} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteKeyValueAccessorFactory
    implements KeyValueAccessorFactory {

  public static final ConcreteKeyValueAccessorFactory INSTANCE =
      new ConcreteKeyValueAccessorFactory();

  private ConcreteKeyValueAccessorFactory() {}

  @Override
  public KeyValueAccessor newAccessor(Accessor accessor,
      Class<?> keyType, Class<?> componentType) {
    final Class<?> dataType = accessor.getDataType();
    if (Map.class.isAssignableFrom(dataType)) {
      return new MapAccessor(accessor, keyType, componentType);
    }
    throw new ViewTemplateException("expected a map");
  }

}
