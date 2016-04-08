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
package org.soulwing.prospecto.runtime.template;

import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.template.ContainerNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;

/**
 * A {@link ViewTemplate.Traversal} using breadth-first order.
 *
 * @author Carl Harris
 */
class BreadthFirstTraversal extends AbstractTraversal {

  BreadthFirstTraversal(AbstractViewNode root) {
    super(root);
  }

  @Override
  Object traverse(AbstractViewNode node, ViewNodeVisitor visitor,
      Object state) {
    if (node instanceof ContainerNode) {
      return traverseChildren((ContainerNode) node, visitor,
          node.accept(visitor, state));
    }

    return node.accept(visitor, state);
  }


}
