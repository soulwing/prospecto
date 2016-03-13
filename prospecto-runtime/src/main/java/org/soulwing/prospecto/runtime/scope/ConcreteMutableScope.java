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
package org.soulwing.prospecto.runtime.scope;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.soulwing.prospecto.api.MutableScope;

/**
 * A simple {@link MutableScope}.
 *
 * @author Carl Harris
 */
public class ConcreteMutableScope implements MutableScope {

  private final Map<String, Object> nameMap = new HashMap<>();

  private final Map<Object, Object> contentMap = new HashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> type) {
    Object result = null;
    for (Object obj : contentMap.values()) {
      if (type.isAssignableFrom(obj.getClass())) {
        if (result == null) {
          result = obj;
        }
        else {
          throw new IllegalStateException(
              "expected no more than one instance of type "
                  + type.getName());
        }
      }
    }
    return (T) result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(String name, Class<T> type) {
    Object value = nameMap.get(name);
    if (value == null) return null;
    if (!type.isAssignableFrom(value.getClass())) {
      throw new ClassCastException("found object of type "
          + value.getClass().getName()
          + "; expected " + type.getName());
    }
    return (T) value;
  }

  @Override
  public void put(Object obj) {
    contentMap.put(obj, obj);
  }

  @Override
  public Object put(String name, Object obj) {
    Object previousObj = nameMap.put(name, obj);
    if (previousObj != null) {
      contentMap.remove(previousObj);
    }
    put(obj);
    return previousObj;
  }

  @Override
  public void putAll(Iterable<?> objs) {
    for (final Object obj : objs) {
      put(obj);
    }
  }

  @Override
  public void putAll(Map<String, ?> objs) {
    for (final Map.Entry<String, ?> entry : objs.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  public boolean remove(Object obj) {
    final boolean removed = contentMap.remove(obj) != null;
    Iterator<Map.Entry<String, Object>> i = nameMap.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry<String, Object> entry = i.next();
      if (entry.getValue() == obj) {
        i.remove();
      }
    }
    return removed;
  }

}
