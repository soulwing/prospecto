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

/**
 * An object that acts as a container for other objects and supports retrieval
 * by type and by name.
 *
 * @author Carl Harris
 */
public interface Scope {

  /**
   * Gets a singleton object of the given type.
   * @param type the type of object to retrieve
   * @return singleton instance of the given type or {@code null} if there
   *    is no object of the given type
   * @throws IllegalStateException if there is more than one object of the
   *    given type
   */
  <T> T get(Class<T> type);

  /**
   * Gets a named object of the given type.
   * @param name name of the object to retrieve
   * @param type expected type of the named object
   * @return instance of the given type that corresponds to the given name
   *    or {@code null} if no such instance exists
   * @throws ClassCastException if the object with the given name does not
   *    have the expected type
   */
  <T> T get(String name, Class<T> type);

}
