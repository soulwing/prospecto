/*
 * File created on Mar 28, 2016
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

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.accessor.AccessorBuilder;
import org.soulwing.prospecto.runtime.accessor.AccessorBuilderFactory;
import org.soulwing.prospecto.runtime.accessor.ReflectionAccessorBuilderFactory;
import org.soulwing.prospecto.runtime.injector.BeanFactory;
import org.soulwing.prospecto.runtime.injector.JdkBeanFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.DiscriminatorEventFactory;
import org.soulwing.prospecto.runtime.node.EnvelopeNode;
import org.soulwing.prospecto.runtime.node.ModelAccessingNode;
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.runtime.node.ReferenceNode;
import org.soulwing.prospecto.runtime.node.SubtypeNode;
import org.soulwing.prospecto.runtime.node.UrlNode;
import org.soulwing.prospecto.runtime.node.ValueNode;

/**
 * An abstract base for {@link ViewTemplateBuilder} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractViewTemplateBuilder implements ViewTemplateBuilder {

  private final BeanFactory beanFactory = new JdkBeanFactory();

  private final AccessorBuilderFactory accessorBuilderFactory =
      new ReflectionAccessorBuilderFactory();


  private final AbstractViewTemplateBuilder parent;
  private final ContainerViewNode target;
  private final AbstractViewNode node;
  private final AccessorBuilder accessorBuilder;

  private AccessType accessType = AccessType.PROPERTY;

  AbstractViewTemplateBuilder(AbstractViewTemplateBuilder parent,
      ContainerViewNode target, AbstractViewNode node) {
    this.parent = parent;
    this.target = target;
    this.node = node;
    this.accessType = parent == null ? AccessType.PROPERTY : parent.accessType;
    this.accessorBuilder = newAccessorBuilder(parent, node, accessType,
        accessorBuilderFactory);
  }

  private static AccessorBuilder newAccessorBuilder(
      AbstractViewTemplateBuilder parent, AbstractViewNode node,
      AccessType accessType, AccessorBuilderFactory builderFactory) {
    if (parent == null) return null;
    if (node.getName() == null) return null;
    return builderFactory.newBuilder(parent.getTarget().getModelType())
        .propertyName(node.getName())
        .accessType(accessType);
  }

  public AbstractViewTemplateBuilder getParent() {
    return parent;
  }

  public ContainerViewNode getTarget() {
    return target;
  }

  public AccessorBuilder getAccessorBuilder() {
    return accessorBuilder;
  }

  public AccessType getAccessType() {
    return accessType;
  }

  public void setAccessType(AccessType accessType) {
    this.accessType = accessType;
  }

  @Override
  public ViewTemplateBuilder value(String name) {
    return value(name, null);
  }

  @Override
  public ViewTemplateBuilder value(String name, String namespace) {
    final ValueNode node = new ValueNode(name, namespace);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ValueNode node);

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
    final UrlNode node = new UrlNode(name, namespace);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(UrlNode node);

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
    final ArrayOfValueNode node = new ArrayOfValueNode(name, elementName,
        namespace);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ArrayOfValueNode node);

  @Override
  public ViewTemplateBuilder object(String name, Class<?> modelType) {
    return object(name, null, modelType);
  }

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType) {
    final ObjectNode node = new ObjectNode(name, namespace, modelType);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  @Override
  public ViewTemplateBuilder object(String name, ViewTemplate template) {
    return object(name, null, template);
  }

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      ViewTemplate template) {
    assert template instanceof ComposableViewTemplate;
    final ContainerViewNode node = ((ComposableViewTemplate) template)
        .object(name, namespace);
    addChildToTarget(node);
    return newValueNodeTemplateBuilder(node);
  }

  @Override
  public ViewTemplateBuilder reference(String name, Class<?> modelType) {
    return reference(name, null, modelType);
  }

  @Override
  public ViewTemplateBuilder reference(String name, String namespace,
      Class<?> modelType) {
    final ReferenceNode node = new ReferenceNode(name, namespace, modelType);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  @Override
  public ViewTemplateBuilder reference(String name, ViewTemplate template) {
    return reference(name, null, template);
  }

  @Override
  public ViewTemplateBuilder reference(String name, String namespace,
      ViewTemplate template) {
    assert template instanceof ComposableViewTemplate;
    final ContainerViewNode node = ((ComposableViewTemplate) template)
        .reference(name, namespace);
    addChildToTarget(node);
    return newValueNodeTemplateBuilder(node);
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
    final ArrayOfObjectNode node = new ArrayOfObjectNode(name, elementName,
        namespace, modelType);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, ViewTemplate template) {
    return arrayOfObjects(name, null, null, template);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      ViewTemplate template) {
    return arrayOfObjects(name, elementName, null, template);
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, ViewTemplate template) {
    assert template instanceof ComposableViewTemplate;
    final ContainerViewNode node = ((ComposableViewTemplate) template)
        .arrayOfObjects(name, elementName, namespace);
    addChildToTarget(node);
    return newValueNodeTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ContainerViewNode node);

  protected abstract ViewTemplateBuilder newValueNodeTemplateBuilder(ContainerViewNode node);

  @Override
  public ViewTemplateBuilder envelope(String name) {
    return envelope(name, null);
  }

  @Override
  public ViewTemplateBuilder envelope(String name, String namespace) {
    final EnvelopeNode node = new EnvelopeNode(name, namespace,
        target.getModelType());
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(EnvelopeNode node);

  @Override
  public ViewTemplateBuilder subtype(Class<?> subtype) {
    assertIsSubTypeOfModelType(subtype);
    assertTargetHasDiscriminator();
    final SubtypeNode node = new SubtypeNode(subtype);
    node.put(DiscriminatorEventFactory.DISCRIMINATOR_FLAG_KEY, true);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(SubtypeNode node);

  private void assertIsSubTypeOfModelType(Class<?> subtype) {
    final Class<?> base = target.getModelType();
    if (!base.isAssignableFrom(subtype) || base.equals(subtype)) {
      throw new ViewTemplateException(subtype + " is not a subtype of "
          + base);
    }
  }

  private void assertTargetHasDiscriminator() {
    if (target.get(DiscriminatorEventFactory.DISCRIMINATOR_FLAG_KEY, Boolean.class) != null) return;
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
    assertDiscriminatorNotSet();
    assertTargetHasNoChildren();
    target.put(DiscriminatorEventFactory.DISCRIMINATOR_FLAG_KEY, true);
    if (discriminator != null) {
      target.put(discriminator);
    }
    return this;
  }

  private void assertTargetHasNoChildren() {
    final List<AbstractViewNode> children = target.getChildren();
    if (!children.isEmpty()) {
      throw new ViewTemplateException(
          "discriminator must be the first child of the parent node");
    }
  }

  private void assertDiscriminatorNotSet() {
    if (target.get(DiscriminatorEventFactory.DISCRIMINATOR_FLAG_KEY, Boolean.class) != null) {
      throw new ViewTemplateException("only one discriminator is allowed");
    }
  }

  @Override
  public ViewTemplateBuilder source(String name) {
    accessorBuilder.propertyName(name);
    return this;
  }

  @Override
  public ViewTemplateBuilder allow(AccessMode mode, AccessMode... modes) {
    return allow(EnumSet.of(mode, modes));
  }

  @Override
  public ViewTemplateBuilder allow(EnumSet<AccessMode> modes) {
    accessorBuilder.accessModes(modes);
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
      Class<? extends ValueTypeConverter> converterClass,
      Map configuration) {
    try {
      return converter(beanFactory.construct(converterClass, configuration));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder converter(ValueTypeConverter<?> converter) {
    if (node.get(ValueTypeConverter.class) != null) {
      throw new ViewTemplateException("only one converter is allowed");
    }
    node.put(converter);
    return this;
  }

  @Override
  public ViewTemplateBuilder attribute(Object value) {
    node.put(value);
    return this;
  }

  @Override
  public ViewTemplateBuilder attribute(String name, Object value) {
    node.put(name, value);
    return this;
  }

  public final ViewTemplateBuilder end() {
    injectAccessor();
    return onEnd();
  }

  protected abstract ViewTemplateBuilder onEnd();

  protected void addChildToTarget(AbstractViewNode node) {
    injectAccessor();
    target.addChild(node);
  }

  protected void injectAccessor() {
    if (accessorBuilder != null
        && this.node instanceof ModelAccessingNode) {
      ((ModelAccessingNode) this.node).setAccessor(accessorBuilder.build());
    }
  }

}
