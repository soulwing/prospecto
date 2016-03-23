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

import java.util.Iterator;

/**
 * An accessor for a multi-valued property such as an array or a collection.
 *
 * @author Carl Harris
 */
public interface MultiValuedAccessor {

  /**
   * Determines whether the accessor can be read.
   * @return {@code true} if the accessor can be read
   */
  boolean canRead();

  /**
   * Determines whether the accessor can be written.
   * @return {@code true} if the accessor can be written
   */
  boolean canWrite();

  /**
   * Gets an iterator for the multi-valued property.
   * @param source source object that contains the multi-valued property
   * @return iterator or {@code null} if the subject property is null
   * @throws Exception
   */
  Iterator<Object> iterator(Object source) throws Exception;

  /**
   * Adds an element to the multi-valued property.
   * @param target target object that contains the multi-valued property
   * @param value the value to add
   * @throws Exception
   */
  void add(Object target, Object value) throws Exception;

  /**
   * Removes an element to the multi-valued property.
   * @param target target object that contains the multi-valued property
   * @param value the value to remove
   * @throws Exception
   */
  void remove(Object target, Object value) throws Exception;

}
