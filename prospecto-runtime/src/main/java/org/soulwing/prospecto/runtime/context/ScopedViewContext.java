/*
 * File created on Mar 10, 2016
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

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.factory.ObjectFactories;
import org.soulwing.prospecto.runtime.association.AssociationManagerService;
import org.soulwing.prospecto.runtime.converter.ValueTypeConverterService;
import org.soulwing.prospecto.runtime.factory.ObjectFactoryService;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;
import org.soulwing.prospecto.runtime.reference.ReferenceResolverService;

/**
 * A {@link ViewContext} onto which additional scopes can be pushed and popped
 * as node tree of a view template is traversed.
 *
 * @author Carl Harris
 */
public interface ScopedViewContext extends ViewContext {

  /**
   * Gets the collection of view listeners.
   * @return view listners
   */
  @Override
  NotifiableViewListeners getListeners();

  /**
   * Gets the collection of resource resolvers.
   * @return resource resolvers
   */
  @Override
  ReferenceResolverService getReferenceResolvers();

  /**
   * Gets the collection of association managers.
   * @return association managers
   */
  @Override
  AssociationManagerService getAssociationManagers();

  /**
   * Gets the collection of object factory strategies
   * @return factory strategies
   */
  @Override
  ObjectFactoryService getObjectFactories();

  /**
   * Gets the collection of value type converters
   * @return converter collection
   */
  @Override
  ValueTypeConverterService getValueTypeConverters();

  /**
   * Pushes a new frame onto the context stack.
   * @param name name of the subject view node
   * @param modelType model type associated with the node
   */
  void push(String name, Class<?> modelType);

  /**
   * Pushes a new frame onto the context stack for an indexed frame
   * @param index index to associate with the frame
   */
  void push(int index);

  /**
   * Pops the top frame from the context stack
   */
  void pop();

}
