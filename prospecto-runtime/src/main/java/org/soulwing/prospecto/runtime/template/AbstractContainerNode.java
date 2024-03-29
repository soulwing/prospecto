/*
 * File created on Apr 8, 2016
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.template.ContainerNode;
import org.soulwing.prospecto.api.template.ViewNode;

/**
 * A view node that contains other view nodes.
 *
 * @author Carl Harris
 */
public abstract class AbstractContainerNode extends AbstractViewNode
    implements ContainerNode {

  protected final List<ViewNode> children = new LinkedList<>();

  /**
   * Constructs a new instance.
   *
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param keyType element key type
   * @param modelType element model type
   */
  protected AbstractContainerNode(String name, String namespace,
      Class<?> keyType, Class<?> modelType) {
    super(name, namespace, keyType, modelType);
  }

  @Override
  public Iterator<ViewNode> iterator() {
    return children.iterator();
  }

  public List<ViewNode> getChildren() {
    return children;
  }

  public void addChild(ViewNode child) {
    children.add(child);
    ((AbstractViewNode) child).setParent(this);
  }

  public void addChildren(AbstractContainerNode source) {
    for (ViewNode child : source.children) {
      addChild(child);
    }
  }

}
