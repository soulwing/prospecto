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
package org.soulwing.prospecto.runtime.builder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.json.JsonValue;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ValueNode;

/**
 * A configurator for a view node.
 *
 * TODO: I hate this abstraction and its relationship to ConcreteViewTemplateBuilder.
 * @author Carl Harris
 */
class ViewNodeConfigurator {

  private final AbstractViewNode target;
  private final Class<?> declaringClass;

  private String name;
  private AccessType accessType = AccessType.PROPERTY;
  private ValueTypeConverter<?> converter;

  public ViewNodeConfigurator(AbstractViewNode target, Class<?> declaringClass,
      String defaultName) {
    this.target = target;
    this.name = defaultName;
    this.declaringClass = declaringClass;
  }

  public void setSource(String name) {
    this.name = name;
  }

  public void setAccessType(AccessType accessType) {
    this.accessType = accessType;
  }

  public void setConverter(Class<? extends ValueTypeConverter> converterClass,
      Object... configuration) {
    if (configuration.length % 2 != 0) {
      throw new IllegalArgumentException("configuration must be name-value pairs");
    }
    final Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < configuration.length / 2; i++) {
      map.put(configuration[2*i].toString(), configuration[2*i + 1]);
    }
    setConverter(converterClass, map);
  }

  public void setConverter(Class<? extends ValueTypeConverter> converterClass,
      Map<String, Object> configuration) {
    try {
      final ValueTypeConverter<?> converter = converterClass.newInstance();
      for (final String key : configuration.keySet()) {
        final PropertyDescriptor descriptor = new PropertyDescriptor(key,
            converterClass);
        descriptor.getWriteMethod().invoke(converter, configuration.get(key));
      }
      for (Method method : converterClass.getMethods()) {
        if (method.getParameterTypes().length == 0
            && method.getAnnotation(PostConstruct.class) != null) {
          method.invoke(converter);
        }
      }
      setConverter(converter);
    }
    catch (IntrospectionException | InstantiationException
        | InvocationTargetException | IllegalAccessException ex) {
      throw new ViewTemplateException(ex);
    }
  }

  public void setConverter(ValueTypeConverter<?> converter) {
    this.converter = converter;
  }

  public void putAttribute(Object value) {
    target.put(value);
  }

  public void putAttribute(String name, Object value) {
    target.put(name, value);
  }

  public Accessor configure() {
    try {
      final Accessor accessor = newAccessor(declaringClass);
      target.setAccessor(accessor);
      if (target instanceof ValueNode) {
        ((ValueNode) target).setConverter(converter);
      }
      return accessor;
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  protected Accessor newAccessor(Class<?> declaringClass)
      throws NoSuchFieldException, NoSuchMethodException, IntrospectionException,
      InstantiationException, IllegalAccessException {
    final Accessor accessor = AccessorFactory.accessor(declaringClass,
        name, accessType);
    return accessor;
  }

}
