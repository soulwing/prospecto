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

import java.util.HashMap;
import java.util.Map;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.injector.BeanFactory;
import org.soulwing.prospecto.runtime.injector.JdkBeanFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.Convertable;
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

  private final BeanFactory beanFactory = new JdkBeanFactory();

  private final ViewTemplateBuilder parent;
  private final ContainerViewNode target;
  private final Cursor cursor;

  public ConcreteViewTemplateBuilder(Class<?> modelType,
      ContainerViewNode target) {
    this(null, new Cursor(modelType), modelType, target);
  }

  private ConcreteViewTemplateBuilder(ViewTemplateBuilder parent,
      Cursor cursor, Class<?> modelType, ContainerViewNode target) {
    this.parent = parent;
    this.cursor = new Cursor(cursor, modelType);
    this.target = target;
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
    return new ConcreteViewTemplateBuilder(this, cursor, modelType, node);
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
    return new ConcreteViewTemplateBuilder(this, cursor, modelType, node);
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
    return new ConcreteViewTemplateBuilder(this, cursor, cursor.getModelType(),
        node);
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
    cursor.setModelName(name);
    cursor.update();
    return this;
  }

  @Override
  public ViewTemplateBuilder accessType(AccessType accessType) {
    cursor.setAccessType(accessType);
    cursor.update();
    return this;
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Object... configuration) {
    try {
      final ValueTypeConverter<?> converter = beanFactory.construct(
          converterClass, configuration);
      return converter(converter);
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Map<String, Object> configuration) {
    try {
      final ValueTypeConverter<?> converter = beanFactory.construct(
          converterClass, configuration);
      return converter(converter);
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder converter(ValueTypeConverter<?> converter) {
    if (!(cursor.getNode() instanceof Convertable)) {
      throw new ViewTemplateException("node '" + cursor.getNode().getName()
          + "' does not support a converter");
    }
    ((Convertable) cursor.getNode()).setConverter(converter);
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
    cursor.advance();
    return new ConcreteViewTemplate(target);
  }

  static class Cursor {
    private final Class<?> modelType;

    private AbstractViewNode node;
    private String modelName;
    private AccessType accessType = AccessType.PROPERTY;

    Cursor(Class<?> modelType) {
      this.modelType = modelType;
    }

    Cursor(Cursor cursor, Class<?> modelType) {
      this.modelType = modelType;
      this.accessType = cursor.accessType;
    }

    /**
     * Gets the {@code modelType} property.
     * @return property value
     */
    public Class<?> getModelType() {
      return modelType;
    }

    /**
     * Gets the {@code node} property.
     * @return property value
     */
    public AbstractViewNode getNode() {
      return node;
    }

    /**
     * Gets the {@code modelName} property.
     * @return property value
     */
    public String getModelName() {
      return modelName;
    }

    /**
     * Sets the {@code modelName} property.
     * @param modelName the property value to set
     */
    public void setModelName(String modelName) {
      this.modelName = modelName;
    }

    /**
     * Gets the {@code accessType} property.
     * @return property value
     */
    public AccessType getAccessType() {
      return accessType;
    }

    /**
     * Sets the {@code accessType} property.
     * @param accessType the property value to set
     */
    public void setAccessType(AccessType accessType) {
      this.accessType = accessType;
    }

    public void advance() {
      advance(null, null);
    }

    public void advance(AbstractViewNode node) {
      advance(node, node.getName());
    }

    public void advance(AbstractViewNode node, String modelName) {
      update();
      this.node = node;
      this.modelName = modelName;
    }

    public void update() {
      if (modelName == null) return;
      if (modelType == null) {
        throw new AssertionError("modelType is required");
      }
      try {
        node.setAccessor(AccessorFactory.accessor(modelType, modelName,
            accessType));
      }
      catch (Exception ex) {
        throw new ViewTemplateException(ex);
      }
    }

  }

}
