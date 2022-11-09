/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.api.template;

import java.util.Iterator;
import java.util.Map;

import org.soulwing.prospecto.api.association.ToManyAssociationManager;

/**
 * A node that represents a map structure.
 *
 * @author Carl Harris
 */
public interface MapNode extends UpdatableNode {

  /**
   * Gets the data type of the keys of this node.
   * @return key data type
   */
  Class<?> getKeyType();

  /**
   * Gets the data type of the elements of this node.
   * @return component data type
   */
  Class<?> getComponentType();

  /**
   * Gets an iterator for the key-value pairs associated with this node in
   * the given model.
   * @param model model containing the map to iterate
   * @return iterator for the subject values
   * @throws Exception
   */
  Iterator<Map.Entry<?, ?>> iterator(Object model) throws Exception;

  /**
   * Gets this node's default association manager.
   * @return association manager
   */
  ToManyAssociationManager<?, ?> getDefaultManager();

}
