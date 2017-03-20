/*
 * File created on Mar 26, 2016
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
package org.soulwing.prospecto.runtime.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A {@link InjectableViewEntity} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteInjectableViewEntity implements InjectableViewEntity {

  private static class InjectableValue {
    final Object value;
    final Injector injector;

    InjectableValue(Object value, Injector injector) {
      this.value = value;
      this.injector = injector;
    }
  }

  private final Class<?> type;
  private final Map<String, InjectableValue> map = new LinkedHashMap<>();

  public ConcreteInjectableViewEntity(Class<?> type) {
    this.type = type;
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  @Override
  public Set<String> nameSet() {
    return map.keySet();
  }

  @Override
  public ViewEntity navigateTo(String path) {
    if (path == null || path.isEmpty()) {
      throw new IllegalArgumentException("path must be non-empty");
    }

    return doNavigateTo("", path, this);
  }

  private ViewEntity doNavigateTo(String head, String tail,
      ViewEntity viewEntity) {
    if (tail.isEmpty()) return viewEntity;

    final int index = tail.indexOf('.');
    final String node = node(tail, index);
    final Object obj = viewEntity.get(node);

    if (obj == null) {
      throw new IllegalArgumentException("'" + head + "' not found");
    }

    if (!(obj instanceof ViewEntity)) {
      throw new IllegalArgumentException("'" + head + "' not a view entity");
    }

    return doNavigateTo(head(head, node), tail(tail, index), (ViewEntity) obj);
  }

  private String node(String path, int index) {
    if (index == -1) return path;
    return path.substring(0, index);
  }

  private String head(String head, String node) {
    if (head.isEmpty()) return node;
    return head + "." + node;
  }

  private String tail(String path, int index) {
    if (index == -1) return "";
    return path.substring(index + 1);
  }

  @Override
  public Object get(String name) {
    final InjectableValue injectableValue = map.get(name);
    if (injectableValue == null) return null;
    return injectableValue.value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(String name, Class<T> type) {
    final Object value = get(name);
    if (value == null) return null;
    if (!type.isInstance(value)) {
      throw new ClassCastException("value " + value + " of type "
          + value.getClass().getName() + " is not an instance of "
          + type.getName());
    }
    return (T) value;
  }

  @Override
  public void put(String name, Object value) {
    final InjectableValue injectableValue = map.get(name);
    if (injectableValue == null) {
      throw new IllegalArgumentException("entity has no property named '"
          + name + "'");
    }
    put(name, value, injectableValue.injector);
  }

  @Override
  public void put(String name, Object value, Injector injector) {
    map.put(name, new InjectableValue(value, injector));
  }

  @Override
  public void remove(String name) {
    map.remove(name);
  }

  @Override
  public void inject(Object target) throws ViewApplicatorException {
    try {
      for (String name : map.keySet()) {
        final InjectableValue injectableValue = map.get(name);
        // only inject simple values, not composed objects
        if (injectableValue.injector instanceof ValueInjector) {
          ((ValueInjector) injectableValue.injector)
              .inject(target, injectableValue.value);
        }
      }
    }
    catch (Exception ex) {
      throw new ViewApplicatorException(ex);
    }
  }

  @Override
  public void inject(Object target, ScopedViewContext context) throws Exception {
    for (String name : map.keySet()) {
      final InjectableValue injectableValue = map.get(name);
      injectableValue.injector.inject(target, injectableValue.value, context);
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("{ ");
    sb.append("type=").append(type.getSimpleName()).append(" ");
    for (String name : map.keySet()) {
      sb.append(name).append("=");
      final Object value = map.get(name).value;
      if (value instanceof Enum) {
        sb.append(((Enum) value).name());
      }
      else if (value instanceof String
          || value instanceof Date
          || value instanceof Calendar) {
        sb.append('"').append(value).append('"');
      }
      else {
        sb.append(value);
      }
      sb.append(" ");
    }
    sb.append("}");
    return sb.toString();
  }

}
