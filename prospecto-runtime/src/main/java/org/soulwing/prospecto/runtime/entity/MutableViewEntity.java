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

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A mutable {@link ViewEntity}.
 *
 * @author Carl Harris
 */
public interface MutableViewEntity extends ViewEntity {

  interface Injector {

    void inject(Object target, Object value, ScopedViewContext context)
        throws Exception;

  }

  interface ValueInjector extends Injector {

    void inject(Object target, Object value) throws Exception;

  }
  /**
   * Puts a property into this entity.
   * @param name name of the property
   * @param value property value
   * @param injector that will be used to inject {@code value} into a
   *    target model object
   */
  void put(String name, Object value, Injector injector);

  /**
   * Injects the properties of this entity into a model object.
   * @param target the target model object
   * @param context view context
   * @throws Exception
   */
  void inject(Object target, ScopedViewContext context) throws Exception;

}
