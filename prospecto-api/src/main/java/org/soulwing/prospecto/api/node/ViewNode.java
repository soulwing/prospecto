/*
 * File created on Apr 5, 2016
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
package org.soulwing.prospecto.api.node;

import org.soulwing.prospecto.api.scope.Scope;

/**
 * A node in a view hierarchy.
 * <p>
 * A node produces a sequence of events when evaluated with a given model object
 * and context.
 *
 * @author Carl Harris
 */
public interface ViewNode extends Scope {

  /**
   * Gets the name associated with this node.
   * @return name or {@code null} if none was specified
   */
  String getName();

  /**
   * Gets the namespace associated with this node.
   * @return namespace or {@code null} if none was specified
   */
  String getNamespace();

  /**
   * Gets the model type associated with this node.
   * @return model type or {@code null} if none was specified
   */
  Class<?> getModelType();

}