/*
 * File created on Mar 14, 2016
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
package org.soulwing.prospecto.runtime.injector;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.soulwing.prospecto.api.ViewTemplateException;

/**
 * A {@link BeanFactory} that uses JDK-provided introspection facilities.
 *
 * @author Carl Harris
 */
public class JdkBeanFactory implements BeanFactory {

  @Override
  public <T> T construct(Class<T> beanClass, Object... properties)
      throws Exception {
    if (properties.length % 2 != 0) {
      throw new IllegalArgumentException("properties must be name-value pairs");
    }
    final Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < properties.length / 2; i++) {
      map.put(properties[2*i].toString(), properties[2*i + 1]);
    }
    return construct(beanClass, map);
  }

  @Override
  public <T> T construct(Class<T> beanClass, Map properties)
      throws Exception {
    final T bean = beanClass.newInstance();
    for (final Object key : properties.keySet()) {
      final PropertyDescriptor descriptor = new PropertyDescriptor(
          key.toString(), beanClass);
      descriptor.getWriteMethod().invoke(bean, properties.get(key));
    }
    for (Method method : beanClass.getMethods()) {
      if (method.getParameterTypes().length == 0
          && method.getAnnotation(PostConstruct.class) != null) {
        method.invoke(bean);
      }
    }
    return bean;
  }

}
