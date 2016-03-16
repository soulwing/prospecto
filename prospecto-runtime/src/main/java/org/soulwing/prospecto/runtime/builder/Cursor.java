/*
 * File created on Mar 15, 2016
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

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A tree node cursor used by {@link ConcreteViewTemplateBuilder}.
 * <p>
 * As nodes are added to the tree produced by a template builder, the cursor
 * is pointed at the newly added node. The cursor has several properties which
 * can be set to configure the node at the cursor. When the cursor is advanced
 * the node is updated using the configured properties before it is advanced to
 * the new node.
 *
 * @author Carl Harris
 */
interface Cursor {

  /**
   * Gets the node at the cursor.
   * @return cursor node or {@code null} if the cursor has been advanced past
   *    the last node in a subtree.
   */
  AbstractViewNode getNode();

  /**
   * Gets the model type associated with the container node.
   * @return model type
   */
  Class<?> getModelType();

  /**
   * Gets the model name associated with the cursor node.
   * @return cursor node's model name
   */
  String getModelName();

  /**
   * Sets the model name associated with the cursor node.
   * @param modelName the model name to set
   */
  void setModelName(String modelName);

  /**
   * Gets the access type associated with the cursor node.
   * @return access type
   */
  AccessType getAccessType();

  /**
   * Sets the access type associated with the cursor node.
   * @param accessType the access type to set
   */
  void setAccessType(AccessType accessType);

  /**
   * Advances the cursor past the last node in the current subtree.
   */
  void advance();

  /**
   * Advances the cursor to the given node.
   * @param node the new cursor node
   */
  void advance(AbstractViewNode node);

  /**
   * Advances the cursor to the given node.
   * @param node the new cursor node
   * @param modelName model name to set in the new cursor node
   */
  void advance(AbstractViewNode node, String modelName);

  /**
   * Updates the cursor node without repositioning it.
   * <p>
   * The current values of the cursor properties are used to update the node.
   */
  void update();

  /**
   * Creates a copy of this cursor with a different model type.
   * @param modelType the new model type
   * @return model type
   */
  Cursor copy(Class<?> modelType);

}
