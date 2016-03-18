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

import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.converter.Convertible;
import org.soulwing.prospecto.runtime.injector.BeanFactory;
import org.soulwing.prospecto.runtime.injector.JdkBeanFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.DiscriminatorNode;
import org.soulwing.prospecto.runtime.node.EnvelopeNode;
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.runtime.node.SubtypeNode;
import org.soulwing.prospecto.runtime.node.UrlNode;
import org.soulwing.prospecto.runtime.node.ValueNode;

/**
 * A {@link ViewTemplateBuilder} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilder implements ViewTemplateBuilder {

  private final ViewTemplateBuilder parent;
  private final ContainerViewNode target;
  private final Cursor cursor;
  private final BeanFactory beanFactory;
  private final ViewTemplateBuilderFactory builderFactory;


  ConcreteViewTemplateBuilder(Class<?> modelType,
      ContainerViewNode target, ViewTemplateBuilderFactory builderFactory) {
    this(null, new ConcreteCursor(modelType), target, builderFactory);
  }

  ConcreteViewTemplateBuilder(ViewTemplateBuilder parent,
      Cursor cursor, ContainerViewNode target,
      ViewTemplateBuilderFactory builderFactory) {
    this(parent, cursor, target, new JdkBeanFactory(), builderFactory);
  }

  ConcreteViewTemplateBuilder(ViewTemplateBuilder parent,
      Cursor cursor, ContainerViewNode target, BeanFactory beanFactory,
      ViewTemplateBuilderFactory builderFactory) {
    this.parent = parent;
    this.cursor = cursor;
    this.target = target;
    this.beanFactory = beanFactory;
    this.builderFactory = builderFactory;
  }

  @Override
  public ViewTemplateBuilder value(String name) {
    return value(name, null);
  }

  @Override
  public ViewTemplateBuilder value(String name, String namespace) {
    AbstractViewNode node = new ValueNode(name, namespace);
    target.addChild(node);
    cursor.advance(node);
    return this;
  }

  @Override
  public ViewTemplateBuilder arrayOfValues(String name) {
    return arrayOfValues(name, null, null);
  }

  @Override
  public ViewTemplateBuilder arrayOfValues(String name, String elementName) {
    return arrayOfValues(name, elementName, null);
  }

  @Override
  public ViewTemplateBuilder arrayOfValues(String name, String elementName,
      String namespace) {
    AbstractViewNode node = new ArrayOfValueNode(name, elementName, namespace);
    target.addChild(node);
    cursor.advance(node);
    return this;
  }

  @Override
  public ViewTemplateBuilder object(String name, Class<?> modelType) {
    return object(name, null, modelType);
  }

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType) {
    ObjectNode node = new ObjectNode(name, namespace, modelType);
    target.addChild(node);
    cursor.advance(node);
    return builderFactory.newBuilder(this, cursor.copy(modelType), node);
  }

  @Override
  public ViewTemplateBuilder object(String name, ViewTemplate template) {
    return object(name, null, template);
  }

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      ViewTemplate template) {
    assert template instanceof ComposableViewTemplate;
    final AbstractViewNode node = ((ComposableViewTemplate) template)
        .object(name, namespace);
    target.addChild(node);
    cursor.advance(node);
    return this;
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, Class<?> modelType) {
    return arrayOfObjects(name, null, null, modelType);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, ViewTemplate template) {
    return arrayOfObjects(name, null, null, template);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      Class<?> modelType) {
    return arrayOfObjects(name, elementName, null, modelType);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      ViewTemplate template) {
    return arrayOfObjects(name, elementName, null, template);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, Class<?> modelType) {
    ArrayOfObjectNode node = new ArrayOfObjectNode(name, elementName,
        namespace, modelType);
    target.addChild(node);
    cursor.advance(node);
    return builderFactory.newBuilder(this, cursor.copy(modelType), node);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, ViewTemplate template) {
    assert template instanceof ComposableViewTemplate;
    final AbstractViewNode node = ((ComposableViewTemplate) template)
        .arrayOfObjects(name, elementName, namespace);
    target.addChild(node);
    cursor.advance(node);
    return this;
  }

  @Override
  public ViewTemplateBuilder envelope(String name) {
    return envelope(name, null);
  }

  @Override
  public ViewTemplateBuilder envelope(String name, String namespace) {
    EnvelopeNode node = new EnvelopeNode(name, namespace);
    target.addChild(node);
    cursor.advance(node, cursor.getModelName());
    return builderFactory.newBuilder(this, cursor, node);
  }

  @Override
  public ViewTemplateBuilder subtype(Class<?> subtype) {
    assertIsSubTypeOfModelType(subtype);
    assertTargetHasDiscriminator();
    SubtypeNode node = new SubtypeNode(subtype);
    target.addChild(node);
    return builderFactory.newBuilder(this, cursor.copy(subtype), node);
  }

  private void assertIsSubTypeOfModelType(Class<?> subtype) {
    final Class<?> base = cursor.getModelType();
    if (!base.isAssignableFrom(subtype) || base.equals(subtype)) {
      throw new ViewTemplateException(subtype + " is not a subtype of "
          + base);
    }
  }

  private void assertTargetHasDiscriminator() {
    for (final ViewNode child : target.getChildren()) {
      if (child instanceof DiscriminatorNode) return;
    }
    throw new ViewTemplateException(
        "discriminator is required before introducing subtypes");
  }

  @Override
  public ViewTemplateBuilder discriminator() {
    return discriminator(null);
  }

  @Override
  public ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Object... configuration) {
    try {
      return discriminator(beanFactory.construct(discriminatorClass,
          configuration));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Map configuration) {
    try {
      return discriminator(beanFactory.construct(discriminatorClass,
          configuration));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder discriminator(DiscriminatorStrategy discriminator) {
    assertTargetHasNoChildren();
    DiscriminatorNode node = new DiscriminatorNode();
    if (discriminator != null) {
      node.put(discriminator);
    }
    target.addChild(node);
    cursor.advance(node, null);
    node.setBase(cursor.getModelType());
    return this;
  }

  private void assertTargetHasNoChildren() {
    final List<AbstractViewNode> children = target.getChildren();
    for (final ViewNode child : children) {
      if (child instanceof DiscriminatorNode) {
        throw new ViewTemplateException("only one discriminator is allowed");
      }
    }
    if (!children.isEmpty()) {
      throw new ViewTemplateException(
          "discriminator must be the first child of the parent node");
    }
  }

  @Override
  public ViewTemplateBuilder url() {
    return url(UrlNode.DEFAULT_NAME, null);
  }

  @Override
  public ViewTemplateBuilder url(String name) {
    return url(name, null);
  }

  @Override
  public ViewTemplateBuilder url(String name, String namespace) {
    AbstractViewNode node = new UrlNode(name, namespace);
    target.addChild(node);
    cursor.advance(node, null);
    return this;
  }

  @Override
  public ViewTemplateBuilder source(String name) {
    if (cursor.getNode() == null) {
      throw new ViewTemplateException(
          "cursor must be positioned on an interior node");
    }
    cursor.setModelName(name);
    return this;
  }

  @Override
  public ViewTemplateBuilder accessType(AccessType accessType) {
    if (cursor.getNode() == null) {
      throw new ViewTemplateException(
          "cursor must be positioned on an interior node");
    }
    cursor.setAccessType(accessType);
    return this;
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Object... configuration) {
    try {
      return converter(beanFactory.construct(converterClass, configuration));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass, Map configuration) {
    try {
      return converter(beanFactory.construct(converterClass, configuration));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder converter(ValueTypeConverter<?> converter) {
    final AbstractViewNode node = cursor.getNode();
    if (!(node instanceof Convertible)) {
      throw new ViewTemplateException("cursor node does not support a converter");
    }
    node.put(converter);
    return this;
  }

  @Override
  public ViewTemplateBuilder attribute(Object value) {
    cursor.getNode().put(value);
    return this;
  }

  @Override
  public ViewTemplateBuilder attribute(String name, Object value) {
    cursor.getNode().put(name, value);
    return this;
  }

  @Override
  public ViewTemplateBuilder end() {
    if (parent == null) return this;
    cursor.advance();
    return parent;
  }

  @Override
  public ViewTemplate build() {
    if (parent != null) {
      throw new ViewTemplateException(
        "Build invoked on non-root builder; an `end` must be missing somewhere");
    }
    cursor.advance();
    return new ConcreteViewTemplate(target);
  }

}
