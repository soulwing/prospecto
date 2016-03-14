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

import java.util.Map;

/**
 * A factory that produces simple JavaBeans.
 *
 * @author Carl Harris
 */
public interface BeanFactory {

  /**
   * Constructs an instance of a bean.
   * <p>
   * Constructs and injects property values, and invokes any
   * {@link javax.annotation.PostConstruct} initializers.
   *
   * @param beanClass a bean class (must have a no-arg constructor)
   * @param properties map of property values; each name in the map must
   *   correspond to a JavaBeans-style property of {@code beanClass}
   * @param <T> type of bean to create
   * @return bean instance
   * @throws Exception
   */
  <T> T construct(Class<T> beanClass, Map<String, Object> properties)
      throws Exception;

}
