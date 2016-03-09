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

import org.soulwing.prospecto.api.ViewException;

/**
 * An accessor for a property of a model.
 *
 * @author Carl Harris
 */
public interface Accessor {

  /**
   * Gets the declared data type of the property.
   * @return data type
   */
  Class<?> getDataType();

  /**
   * Gets the property value.
   * @param source model object from which the property value is to be extracted
   * @return property value
   */
  Object get(Object source) throws Exception;

  /**
   * Sets the property value.
   * @param source model object into which the property value is to be injected
   * @param value property value to inject
   */
  void set(Object source, Object value) throws Exception;

}
