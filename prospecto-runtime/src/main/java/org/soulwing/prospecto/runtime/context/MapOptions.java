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
package org.soulwing.prospecto.runtime.context;

import java.util.HashMap;
import java.util.Map;

import org.soulwing.prospecto.api.Options;

/**
 * An {@link Options} collection implemented on top of a {@link Map}.
 *
 * @author Carl Harris
 */
class MapOptions implements Options {

  private final Map<String, Object> map = new HashMap<>();

  @Override
  public Object get(String name) {
    return map.get(name);
  }

  @Override
  public void put(String name, Object value) {
    map.put(name, value);
  }

  @Override
  public boolean isEnabled(String name) {
    Object value = map.get(name);
    if (value == null) return false;
    if (value instanceof String) {
      value = Boolean.valueOf((String) value);
    }
    if (!(value instanceof Boolean)) {
      throw new ClassCastException("option `" + name + "` is of type "
          + value.getClass().getSimpleName() + ", not Boolean");
    }
    return (Boolean) value;
  }

  @Override
  public Map<String, Object> toMap() {
    return map;
  }

}
