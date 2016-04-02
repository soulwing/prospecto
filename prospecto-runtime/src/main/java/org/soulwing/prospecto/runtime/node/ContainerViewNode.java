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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.discriminator.ConcreteDiscriminatorEventService;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;

/**
 * A view node that contains other view nodes.
 *
 * @author Carl Harris
 */
public abstract class ContainerViewNode extends AbstractViewNode {

  private final List<AbstractViewNode> children;
  private final DiscriminatorEventService discriminatorEventService;

  private Accessor accessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   */
  protected ContainerViewNode(String name, String namespace,
      Class<?> modelType) {
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
    this(name, namespace, modelType, children,
        ConcreteDiscriminatorEventService.INSTANCE);
  }

  ContainerViewNode(String name, String namespace, Class<?> modelType,
      List<AbstractViewNode> children,
      DiscriminatorEventService discriminatorEventService) {
    super(name, namespace, modelType);
    this.children = children;
    this.discriminatorEventService = discriminatorEventService;
  }

  public Accessor getAccessor() {
    return accessor;
  }

  public void setAccessor(Accessor accessor) {
    this.accessor = accessor;
  }

  public List<AbstractViewNode> getChildren() {
    return children;
  }

  public AbstractViewNode getChild(Class<?> modelType, String name) {
    Iterator<AbstractViewNode> i = children.iterator();
    while (i.hasNext()) {
      final AbstractViewNode child = i.next();
      if (child instanceof SubtypeNode
          && modelType.isAssignableFrom(((SubtypeNode) child).getModelType())) {
        final AbstractViewNode node =
            ((SubtypeNode) child).getChild(modelType, name);
        if (node != null) return node;
      }
    }

    i = children.iterator();
    while (i.hasNext()) {
      final AbstractViewNode child = i.next();
      if (name.equals(child.getName())) {
        return child;
      }
    }

    return null;
  }

  public void addChild(AbstractViewNode child) {
    children.add(child);
    child.setParent(this);
  }

  public void addChildren(List<AbstractViewNode> children) {
    for (AbstractViewNode child : children) {
      addChild(child);
    }
  }

  protected final List<View.Event> evaluateChildren(Object model,
      ScopedViewContext context) throws Exception {
    context.put(model);
    final List<View.Event> events = new LinkedList<>();
    if (discriminatorEventService.isDiscriminatorNeeded(this)) {
      events.add(discriminatorEventService.newDiscriminatorEvent(this,
          model.getClass(), context));
    }
    for (AbstractViewNode child : getChildren()) {
      events.addAll(child.evaluate(model, context));
    }
    context.remove(model);
    return events;
  }

}
