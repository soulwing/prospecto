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

import java.util.ArrayList;
import java.util.List;

import org.soulwing.prospecto.api.Traversal;
import org.soulwing.prospecto.api.node.ContainerNode;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * An abstract base for traversal implementations.
 * @author Carl Harris
 */
public abstract class AbstractTraversal implements Traversal {

  private final AbstractViewNode root;

  AbstractTraversal(AbstractViewNode root) {
    this.root = root;
  }

  @Override
  public final Object traverse(ViewNodeVisitor visitor, Object state) {
    return traverse(root, visitor, state);
  }

  abstract Object traverse(AbstractViewNode node, ViewNodeVisitor visitor,
      Object state);

  List<Object> traverseChildren(ContainerNode container,
      ViewNodeVisitor visitor, Object state) {
    final List<Object> results = new ArrayList<>();
    for (final ViewNode child : container) {
      results.add(traverse((AbstractViewNode) child, visitor, state));
    }
    return results;
  }

}
