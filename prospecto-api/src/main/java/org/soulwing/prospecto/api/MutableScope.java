/*
 * File created on Mar 13, 2016
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
package org.soulwing.prospecto.api;

import java.util.Map;

/**
 * A mutable {@code Scope}.
 *
 * @author Carl Harris
 */
public interface MutableScope extends Scope {

  /**
   * Puts an object into this scope.
   * @param obj the object to put
   */
  void put(Object obj);

  /**
   * Puts a named object into this scope.
   * @param name name to assign
   * @param obj the object to put
   * @return the object that was replaced by {@code obj} in this scope
   */
  Object put(String name, Object obj);

  /**
   * Puts a collection of objects into this scope.
   * @param objs the objects to put
   */
  void putAll(Iterable<?> objs);

  /**
   * Puts a collection of named objects into this scope.
   * @param objs map of named objects to put
   */
  void putAll(Map<String, ?> objs);

  /**
   * Removes an object from this scope.
   * @param obj the object to remove
   * @return {@code true} if an object matching the identity of {@code obj}
   *   was removed
   */
  boolean remove(Object obj);

}
