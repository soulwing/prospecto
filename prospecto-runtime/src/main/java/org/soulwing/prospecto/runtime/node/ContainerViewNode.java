/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.runtime.node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A view node that contains other view nodes.
 *
 * @author Carl Harris
 */
public abstract class ContainerViewNode extends AbstractViewNode {

  private final List<AbstractViewNode> children;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   */
  protected ContainerViewNode(String name, String namespace, Class<?> modelType) {
    this(name, namespace, modelType, new ArrayList<AbstractViewNode>());
  }

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   * @param children node children
   */
  protected ContainerViewNode(String name, String namespace, Class<?> modelType,
      List<AbstractViewNode> children) {
    super(name, namespace, modelType);
    this.children = children;
  }

  public List<AbstractViewNode> getChildren() {
    return children;
  }

  public void addChild(AbstractViewNode child) {
    children.add(child);
  }

  protected final List<View.Event> evaluateChildren(Object model,
      ScopedViewContext context) throws Exception {
    context.put(model);
    final List<View.Event> events = new LinkedList<>();
    for (AbstractViewNode child : getChildren()) {
      events.addAll(child.evaluate(model, context));
    }
    return events;
  }

}
