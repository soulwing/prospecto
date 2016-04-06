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
import java.util.List;

import org.soulwing.prospecto.api.node.ContainerNode;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.runtime.discriminator.ConcreteDiscriminatorEventService;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;

/**
 * A view node that contains other view nodes.
 *
 * @author Carl Harris
 */
public abstract class ConcreteContainerNode extends AbstractViewNode
    implements ContainerNode {

  private final List<ViewNode> children;
  private final DiscriminatorEventService discriminatorEventService;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   */
  protected ConcreteContainerNode(String name, String namespace,
      Class<?> modelType) {
    this(name, namespace, modelType, new ArrayList<ViewNode>());
  }

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   * @param children node children
   */
  protected ConcreteContainerNode(String name, String namespace, Class<?> modelType,
      List<ViewNode> children) {
    this(name, namespace, modelType, children,
        ConcreteDiscriminatorEventService.INSTANCE);
  }

  ConcreteContainerNode(String name, String namespace, Class<?> modelType,
      List<ViewNode> children,
      DiscriminatorEventService discriminatorEventService) {
    super(name, namespace, modelType);
    this.children = children;
    this.discriminatorEventService = discriminatorEventService;
  }

  @Override
  public Iterator<ViewNode> iterator() {
    return children.iterator();
  }

  public List<ViewNode> getChildren() {
    return children;
  }

  public ViewNode getChild(Class<?> modelType, String name) {
    Iterator<ViewNode> i = children.iterator();
    while (i.hasNext()) {
      final ViewNode child = i.next();
      if (child instanceof ConcreteSubtypeNode
          && modelType.isAssignableFrom(((ConcreteSubtypeNode) child).getModelType())) {
        final ViewNode node =
            ((ConcreteSubtypeNode) child).getChild(modelType, name);
        if (node != null) return node;
      }
    }

    i = children.iterator();
    while (i.hasNext()) {
      final ViewNode child = i.next();
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

  public void addChildren(Iterable<ViewNode> nodes) {
    for (ViewNode node : nodes) {
      addChild((AbstractViewNode) node);
    }
  }

}
