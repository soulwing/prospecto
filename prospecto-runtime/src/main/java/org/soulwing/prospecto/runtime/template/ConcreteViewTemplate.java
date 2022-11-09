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

import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.listener.ViewMode;
import org.soulwing.prospecto.api.listener.ViewTraversalEvent;
import org.soulwing.prospecto.api.template.ContainerNode;
import org.soulwing.prospecto.runtime.applicator.ConcreteViewApplicatorFactory;
import org.soulwing.prospecto.runtime.applicator.ViewApplicationVisitor;
import org.soulwing.prospecto.runtime.applicator.ViewApplicatorFactory;
import org.soulwing.prospecto.runtime.applicator.ViewEventApplicator;
import org.soulwing.prospecto.runtime.context.ConcreteScopedViewContextFactory;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.context.ScopedViewContextFactory;
import org.soulwing.prospecto.runtime.generator.ViewEventGenerator;
import org.soulwing.prospecto.runtime.generator.ViewGeneratingVisitor;
import org.soulwing.prospecto.runtime.view.ConcreteView;

/**
 * A {@link ViewTemplate} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplate implements ComposableViewTemplate {

  private final AbstractViewNode root;
  private final ScopedViewContextFactory viewContextFactory;
  private final ViewApplicatorFactory viewApplicatorFactory;

  public ConcreteViewTemplate(AbstractViewNode root) {
    this(root, ConcreteScopedViewContextFactory.INSTANCE);
  }

  ConcreteViewTemplate(AbstractViewNode root,
      ScopedViewContextFactory viewContextFactory) {
    this(root, viewContextFactory, ConcreteViewApplicatorFactory.INSTANCE);
  }

  ConcreteViewTemplate(AbstractViewNode root,
      ScopedViewContextFactory viewContextFactory,
      ViewApplicatorFactory viewApplicatorFactory) {
    this.root = root;
    this.viewContextFactory = viewContextFactory;
    this.viewApplicatorFactory = viewApplicatorFactory;
  }


  public AbstractViewNode getRoot() {
    return root;
  }

  @Override
  public Traversal breadthFirst() {
    return new BreadthFirstTraversal(root);
  }

  @Override
  public Traversal depthFirst() {
    return new DepthFirstTraversal(root);
  }

  @Override
  public View generateView(Object source, ViewContext context)
      throws ViewException {
    try {
      final ScopedViewContext scopedContext =
          viewContextFactory.newContext(context);

      scopedContext.appendScope().put(source);
      final ViewTraversalEvent event =
          new ViewTraversalEvent(ViewMode.GENERATE, this, scopedContext);

      scopedContext.getListeners().beforeTraversing(event);

      final ViewEventGenerator generator = (ViewEventGenerator)
          depthFirst().traverse(new ViewGeneratingVisitor(), null);

      final List<View.Event> viewEvents =
          generator.generate(source, scopedContext);

      scopedContext.getListeners().afterTraversing(event);

      return new ConcreteView(viewEvents);
    }
    catch (Exception ex) {
      throw new ViewException(ex);
    }
  }

  @Override
  public ViewApplicator createApplicator(View source, ViewContext context) {
    return createApplicator(source, context, null);
  }

  @Override
  public ViewApplicator createApplicator(View source, ViewContext context,
      String dataKey) {

    final ScopedViewContext scopedContext =
        viewContextFactory.newContext(context);

    final ViewTraversalEvent event =
        new ViewTraversalEvent(ViewMode.APPLY, this, scopedContext);

    scopedContext.getListeners().beforeTraversing(event);
    final ViewEventApplicator applicator = (ViewEventApplicator)
        depthFirst().traverse(new ViewApplicationVisitor(), null);
    return viewApplicatorFactory.newApplicator(root.getModelType(), applicator,
        source, scopedContext, dataKey, event);
  }

  @Override
  public AbstractContainerNode object(String name, String namespace) {
    assertRootIsContainerViewNode(name);
    final ConcreteObjectNode node = new ConcreteObjectNode(name, namespace,
        root.getModelType());
    copyInto(node);
    return node;
  }

  @Override
  public AbstractContainerNode reference(String name, String namespace) {
    assertRootIsContainerViewNode(name);
    final ConcreteReferenceNode node = new ConcreteReferenceNode(name, namespace,
        root.getModelType());
    copyInto(node);
    return node;
  }

  @Override
  public AbstractContainerNode arrayOfObjects(String name, String elementName,
      String namespace) {
    assertRootIsContainerViewNode(name);
    final ConcreteArrayOfObjectsNode node = new ConcreteArrayOfObjectsNode(name, elementName,
        namespace, root.getModelType());
    copyInto(node);
    return node;
  }

  @Override
  public AbstractContainerNode arrayOfReferences(String name, String elementName,
      String namespace) {
    assertRootIsContainerViewNode(name);
    ConcreteArrayOfReferencesNode node = new ConcreteArrayOfReferencesNode(name, elementName,
        namespace, root.getModelType());
    copyInto(node);
    return node;
  }

  @Override
  public ViewTemplate arrayOfObjectsTemplate(String name, String elementName,
      String namespace) {
    assertRootIsContainerViewNode(name);
    RootArrayOfObjectNode root = new RootArrayOfObjectNode(name, elementName,
        namespace, getRoot().getModelType());
    copyInto(root);
    return new ConcreteViewTemplate(root);
  }

  @Override
  public ViewTemplate arrayOfReferencesTemplate(String name, String elementName,
      String namespace) {
    assertRootIsContainerViewNode(name);
    RootArrayOfReferencesNode root = new RootArrayOfReferencesNode(name,
        elementName, namespace, getRoot().getModelType());
    copyInto(root);
    return new ConcreteViewTemplate(root);
  }

  @Override
  public AbstractContainerNode mapOfObjects(String name,
      String namespace, Class<?> keyType) {
    assertRootIsContainerViewNode(name);
    final ConcreteMapOfObjectsNode node = new ConcreteMapOfObjectsNode(name,
        namespace, keyType, root.getModelType());
    copyInto(node);
    return node;
  }

  @Override
  public AbstractContainerNode mapOfReferences(String name,
      String namespace, Class<?> keyType) {
    assertRootIsContainerViewNode(name);
    ConcreteMapOfReferencesNode node = new ConcreteMapOfReferencesNode(name,
        namespace, keyType, root.getModelType());
    copyInto(node);
    return node;
  }

  @Override
  public ViewTemplate mapOfObjectsTemplate(String name,
      String namespace, Class<?> keyType) {
    assertRootIsContainerViewNode(name);
    RootMapOfObjectsNode root = new RootMapOfObjectsNode(name,
        namespace, getRoot().getKeyType(), getRoot().getModelType());
    copyInto(root);
    return new ConcreteViewTemplate(root);
  }

  @Override
  public ViewTemplate mapOfReferencesTemplate(String name,
      String namespace, Class<?> keyType) {
    assertRootIsContainerViewNode(name);
    RootMapOfReferencesNode root = new RootMapOfReferencesNode(name,
        namespace, getRoot().getKeyType(), getRoot().getModelType());
    copyInto(root);
    return new ConcreteViewTemplate(root);
  }

  private void copyInto(AbstractContainerNode node) {
    assert root instanceof ContainerNode;
    node.addChildren((AbstractContainerNode) root);
    node.putAll(root);
  }


  private void assertRootIsContainerViewNode(String name) {
    if (!(root instanceof AbstractContainerNode)) {
      throw new ViewTemplateException("referenced view template for node '"
          + name + "' must have a root node of object or array-of-object type");
    }
  }

}
