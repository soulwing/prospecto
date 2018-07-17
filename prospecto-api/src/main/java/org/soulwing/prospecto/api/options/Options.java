/*
 * File created on Apr 3, 2016
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
package org.soulwing.prospecto.api.options;

import java.util.Map;

/**
 * A map-like API for accessing context options.
 *
 * @author Carl Harris
 */
public interface Options {

  /**
   * Gets the value of the named option.
   * @param name option name
   * @return value or {@code null} if the option is not set
   */
  Object get(String name);

  /**
   * Gets the value of the named option.
   * @param name option name
   * @param defaultValue default value
   * @return option value or {@code defaultValue} if the option is not set
   */
  Object get(String name, Object defaultValue);

  /**
   * Puts (sets) the value of the named option.
   * @param name option name
   * @param value the value to put
   */
  void put(String name, Object value);

  /**
   * Removes the value of the named options.
   * @param name option name
   * @return value of option that was removed
   */
  Object remove(String name);

  /**
   * Tests whether a two-state (boolean) option is set to {@code true}.
   * @param name option name
   * @return {@code true} if the named option is set to {@code true}
   * @throws ClassCastException if the option cannot be coerced to a boolean
   */
  boolean isEnabled(String name);

  /**
   * Coerces this options collection to a map.
   * <p>
   * The returned map can be manipulated to modify this options collection.
   * @return map
   */
  Map<String, Object> toMap();

}
