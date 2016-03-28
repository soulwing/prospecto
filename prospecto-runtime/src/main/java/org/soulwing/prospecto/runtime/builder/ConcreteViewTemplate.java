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
package org.soulwing.prospecto.runtime.builder;

import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.runtime.context.ConcreteScopedViewContextFactory;
import org.soulwing.prospecto.runtime.context.ScopedViewContextFactory;
import org.soulwing.prospecto.runtime.editor.ConcreteModelEditorFactory;
import org.soulwing.prospecto.runtime.editor.ModelEditorFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.runtime.node.ReferenceNode;
import org.soulwing.prospecto.runtime.view.ConcreteView;

/**
 * A {@link ViewTemplate} implementation.
 *
 * @author Carl Harris
 */
class ConcreteViewTemplate implements ComposableViewTemplate {

  private final AbstractViewNode root;
  private final ScopedViewContextFactory viewContextFactory;
  private final ModelEditorFactory modelEditorFactory;

  public ConcreteViewTemplate(AbstractViewNode root) {
    this(root, new ConcreteScopedViewContextFactory());
  }

  ConcreteViewTemplate(AbstractViewNode root,
      ScopedViewContextFactory viewContextFactory) {
    this(root, viewContextFactory, new ConcreteModelEditorFactory());
  }

  ConcreteViewTemplate(AbstractViewNode root,
      ScopedViewContextFactory viewContextFactory,
      ModelEditorFactory modelEditorFactory) {
    this.root = root;
    this.viewContextFactory = viewContextFactory;
    this.modelEditorFactory = modelEditorFactory;
  }


  public AbstractViewNode getRoot() {
    return root;
  }

  @Override
  public View generateView(Object source, ViewContext context)
      throws ViewException {
    try {
      return new ConcreteView(root.evaluate(source,
          viewContextFactory.newContext(context)));
    }
    catch (Exception ex) {
      throw new ViewException(ex);
    }
  }

  @Override
  public ModelEditor generateEditor(View source, ViewContext context) {
    return modelEditorFactory.newEditor(root, source, context);
  }

  @Override
  public ContainerViewNode object(String name, String namespace) {
    assertRootIsContainerViewNode(name);
    ObjectNode node = new ObjectNode(name, namespace, root.getModelType());
    assert root instanceof ContainerViewNode;
    node.addChildren(((ContainerViewNode) root).getChildren());
    return node;
  }

  @Override
  public ContainerViewNode reference(String name, String namespace) {
    assertRootIsContainerViewNode(name);
    ReferenceNode node = new ReferenceNode(name, namespace, root.getModelType());
    assert root instanceof ContainerViewNode;
    node.addChildren(((ContainerViewNode) root).getChildren());
    return node;
  }

  @Override
  public ContainerViewNode arrayOfObjects(String name, String elementName,
      String namespace) {
    assertRootIsContainerViewNode(name);
    ArrayOfObjectNode node = new ArrayOfObjectNode(name, elementName, namespace,
        root.getModelType());
    assert root instanceof ContainerViewNode;
    node.addChildren(((ContainerViewNode) root).getChildren());
    return node;
  }

  private void assertRootIsContainerViewNode(String name) {
    if (!(root instanceof ContainerViewNode)) {
      throw new ViewTemplateException("referenced view template for node '"
          + name + "' must have a root node of object or array-of-object type");
    }
  }

}
