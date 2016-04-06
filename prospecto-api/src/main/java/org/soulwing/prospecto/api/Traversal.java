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
package org.soulwing.prospecto.api;

import org.soulwing.prospecto.api.node.ViewNodeVisitor;

/**
 * A traversal for a {@link ViewTemplate}.
 *
 * @author Carl Harris
 */
public interface Traversal {

  /**
   * Performs a traversal.
   * @param visitor visitor that will be invoked as nodes are visited
   * @param state input state
   * @return traversal result
   */
  Object traverse(ViewNodeVisitor visitor, Object state);

}
