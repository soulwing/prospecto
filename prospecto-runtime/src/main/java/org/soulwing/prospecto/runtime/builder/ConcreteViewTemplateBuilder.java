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

import java.util.Map;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.EnvelopeNode;
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.runtime.node.UrlNode;
import org.soulwing.prospecto.runtime.node.ValueNode;

/**
 * A {@link ViewTemplateBuilder} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilder implements ViewTemplateBuilder {

  private final ViewTemplateBuilder parent;
  private final Class<?> sourceType;
  private final ContainerViewNode target;

  private ViewNodeConfigurator nodeConfigurator;

  public ConcreteViewTemplateBuilder(Class<?> sourceType,
      ContainerViewNode target) {
    this(null, sourceType, target, null);
  }

  private ConcreteViewTemplateBuilder(ViewTemplateBuilder parent,
      Class<?> sourceType, ContainerViewNode target,
      ViewNodeConfigurator nodeConfigurator) {
    this.parent = parent;
    this.sourceType = sourceType;
    this.target = target;
    this.nodeConfigurator = nodeConfigurator;
  }

  @Override
  public ViewTemplateBuilder value(String name) {
    return value(name, null);
  }

  @Override
  public ViewTemplateBuilder value(String name, String namespace) {
    configureCurrentNode();
    AbstractViewNode node = new ValueNode(name, namespace);
    target.addChild(node);
    if (sourceType == null) {
      throw new IllegalStateException("sourceType is required");
    }
    nodeConfigurator = new ViewNodeConfigurator(node, sourceType, name);
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
    configureCurrentNode();
    AbstractViewNode node = new ArrayOfValueNode(name, elementName, namespace);
    target.addChild(node);
    if (sourceType == null) {
      throw new IllegalStateException("sourceType is required");
    }
    nodeConfigurator = new ViewNodeConfigurator(node, sourceType, name);
    return this;
  }

  @Override
  public ViewTemplateBuilder object(String name, Class<?> modelType) {
    return object(name, null, modelType);
  }

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType) {
    configureCurrentNode();
    ObjectNode node = new ObjectNode(name, namespace, modelType);
    target.addChild(node);
    nodeConfigurator = new ViewNodeConfigurator(node, this.sourceType, name);
    return new ConcreteViewTemplateBuilder(this, modelType, node,
        nodeConfigurator);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, Class<?> modelType) {
    return arrayOfObjects(name, null, null, modelType);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      Class<?> modelType) {
    return arrayOfObjects(name, elementName, null, modelType);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, Class<?> modelType) {
    configureCurrentNode();
    ArrayOfObjectNode node = new ArrayOfObjectNode(name, elementName,
        namespace, modelType);
    target.addChild(node);
    nodeConfigurator = new ViewNodeConfigurator(node, this.sourceType, name);
    return new ConcreteViewTemplateBuilder(this, modelType, node,
        nodeConfigurator);
  }

  @Override
  public ViewTemplateBuilder subview(String name, ViewTemplate template) {
    configureCurrentNode();
    final AbstractViewNode node = (AbstractViewNode)
        template.generateSubView(name);
    nodeConfigurator = new ViewNodeConfigurator(node, this.sourceType, name);
    target.addChild(node);
    return this;
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
    configureCurrentNode();
    AbstractViewNode node = new UrlNode(name, namespace);
    target.addChild(node);
    return this;
  }

  @Override
  public ViewTemplateBuilder envelope(String name) {
    return envelope(name, null);
  }

  @Override
  public ViewTemplateBuilder envelope(String name, String namespace) {
    configureCurrentNode();
    EnvelopeNode node = new EnvelopeNode(name, namespace);
    target.addChild(node);
    return new ConcreteViewTemplateBuilder(this, sourceType, node,
        nodeConfigurator);
  }

  @Override
  public ViewTemplateBuilder source(String name) {
    nodeConfigurator.setSource(name);
    return this;
  }

  @Override
  public ViewTemplateBuilder accessType(AccessType accessType) {
    nodeConfigurator.setAccessType(accessType);
    return this;
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Object... configuration) {
    nodeConfigurator.setConverter(converterClass, configuration);
    return this;
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Map<String, Object> configuration) {
    nodeConfigurator.setConverter(converterClass, configuration);
    return this;
  }

  @Override
  public ViewTemplateBuilder converter(ValueTypeConverter<?> converter) {
    nodeConfigurator.setConverter(converter);
    return this;
  }

  public Accessor configureCurrentNode() {
    Accessor accessor = null;
    if (nodeConfigurator != null) {
      accessor = nodeConfigurator.configure();
    }
    nodeConfigurator = null;
    return accessor;
  }

  @Override
  public ViewTemplateBuilder end() {
    configureCurrentNode();
    if (parent == null) return this;
    return parent;
  }

  @Override
  public ViewTemplate build() {
    configureCurrentNode();
    return new ConcreteViewTemplate(target);
  }

}
