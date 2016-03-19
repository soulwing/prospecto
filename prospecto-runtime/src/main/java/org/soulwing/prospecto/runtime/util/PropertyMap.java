/*
 * File created on Mar 19, 2016
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
package org.soulwing.prospecto.runtime.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for a {@link Map} that provides a {@link java.util.Properties}-like
 * API.
 *
 * @author Carl Harris
 */
public class PropertyMap {

  private final Map<String, Object> resolvedProperties = new HashMap<>();

  private final Map<String, Object> properties;

  /**
   * Constructs a new instance that wraps the given map.
   * @param properties properties map
   */
  public PropertyMap(Map<String, Object> properties) {
    this.properties = properties;
  }

  /**
   * Gets the value of associated with the given key.
   * @param key the subject key
   * @param defaultValue default value to return if no value is associated with
   *    {@code key}
   * @return associated value or default
   */
  public Object get(String key, Object defaultValue) {
    Object value = resolvedProperties.get(key);
    if (value == null) {
      value = properties.get(key);
      if (value == null) {
        value = defaultValue;
      }
      resolvedProperties.put(key, value);
    }
    return value;
  }

  /**
   * Gets the string value associated with the given key.
   * @param key the subject key
   * @param defaultValue default value to return if no value is associated with
   *    {@code key}
   * @return associated value or default
   */
  public String getString(String key, String defaultValue) {
    Object value = get(key, defaultValue);
    if (value == null) return null;
    return value.toString();
  }

}
