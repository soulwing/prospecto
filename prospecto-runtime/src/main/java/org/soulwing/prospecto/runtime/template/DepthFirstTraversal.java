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

import org.soulwing.prospecto.api.Traversal;
import org.soulwing.prospecto.api.node.ContainerNode;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A {@link Traversal} using depth-first order.
 *
 * @author Carl Harris
 */
class DepthFirstTraversal extends AbstractTraversal {

  DepthFirstTraversal(AbstractViewNode root) {
    super(root);
  }

  @Override
  Object traverse(AbstractViewNode node, ViewNodeVisitor visitor,
      Object state) {
    if (node instanceof ContainerNode) {
      return node.accept(visitor, traverseChildren((ContainerNode) node,
          visitor, state));
    }

    return node.accept(visitor, state);
  }

}
