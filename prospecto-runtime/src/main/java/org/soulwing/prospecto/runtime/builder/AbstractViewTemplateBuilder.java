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
import org.soulwing.prospecto.api.MetadataHandler;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.accessor.AccessorBuilder;
import org.soulwing.prospecto.runtime.accessor.AccessorBuilderFactory;
import org.soulwing.prospecto.runtime.accessor.ReflectionAccessorBuilderFactory;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;
import org.soulwing.prospecto.runtime.injector.BeanFactory;
import org.soulwing.prospecto.runtime.injector.JdkBeanFactory;
import org.soulwing.prospecto.runtime.meta.UrlResolvingMetadataHandler;
import org.soulwing.prospecto.runtime.template.AbstractViewNode;
import org.soulwing.prospecto.runtime.template.ComposableViewTemplate;
import org.soulwing.prospecto.runtime.template.ConcreteArrayOfObjectsNode;
import org.soulwing.prospecto.runtime.template.ConcreteArrayOfReferencesNode;
import org.soulwing.prospecto.runtime.template.ConcreteArrayOfValuesNode;
import org.soulwing.prospecto.runtime.template.ConcreteContainerNode;
import org.soulwing.prospecto.runtime.template.ConcreteEnvelopeNode;
import org.soulwing.prospecto.runtime.template.ConcreteMetaNode;
import org.soulwing.prospecto.runtime.template.ConcreteObjectNode;
import org.soulwing.prospecto.runtime.template.ConcreteReferenceNode;
import org.soulwing.prospecto.runtime.template.ConcreteSubtypeNode;
import org.soulwing.prospecto.runtime.template.ConcreteValueNode;

/**
 * An abstract base for {@link ViewTemplateBuilder} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractViewTemplateBuilder implements ViewTemplateBuilder {

  private final BeanFactory beanFactory = new JdkBeanFactory();

  private final AbstractViewTemplateBuilder parent;
  private final ConcreteContainerNode target;
  private final AbstractViewNode node;
  private final AccessorBuilder accessorBuilder;

  private AccessType accessType = AccessType.PROPERTY;

  AbstractViewTemplateBuilder(AbstractViewTemplateBuilder parent,
      ConcreteContainerNode target, AbstractViewNode node) {
    this.parent = parent;
    this.target = target;
    this.node = node;
    this.accessType = parent == null ? AccessType.PROPERTY : parent.accessType;
    this.accessorBuilder = newAccessorBuilder(parent, node, accessType,
        ReflectionAccessorBuilderFactory.INSTANCE);
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

  public ConcreteContainerNode getTarget() {
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
    final ConcreteValueNode node = new ConcreteValueNode(name, namespace);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ConcreteValueNode node);

  @Override
  public ViewTemplateBuilder url() {
    return url(ViewDefaults.URL_NAME, null);
  }

  @Override
  public ViewTemplateBuilder url(String name) {
    return url(name, null);
  }

  @Override
  public ViewTemplateBuilder url(String name, String namespace) {
    return meta(name, namespace, UrlResolvingMetadataHandler.INSTANCE);
  }

  @Override
  public ViewTemplateBuilder meta(String name,
      Class<? extends MetadataHandler> handlerClass, Object... configuration) {
    return meta(name, null, handlerClass, configuration);
  }

  @Override
  public ViewTemplateBuilder meta(String name, String namespace,
      Class<? extends MetadataHandler> handlerClass, Object... configuration) {
    try {
      return meta(name, namespace, beanFactory.construct(handlerClass,
          configuration));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder meta(String name,
      Class<? extends MetadataHandler> handlerClass, Map configuration) {
    return meta(name, null, handlerClass, configuration);
  }

  @Override
  public ViewTemplateBuilder meta(String name, String namespace,
      Class<? extends MetadataHandler> handlerClass, Map configuration) {
    try {
      return meta(name, namespace, beanFactory.construct(handlerClass,
          configuration));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public ViewTemplateBuilder meta(String name, MetadataHandler handler) {
    return meta(name, null, handler);
  }

  @Override
  public ViewTemplateBuilder meta(String name, String namespace,
      MetadataHandler handler) {
    final ConcreteMetaNode node = new ConcreteMetaNode(name, namespace, handler);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ConcreteMetaNode node);

  @Override
  public ViewTemplateBuilder arrayOfValues(String name, Class<?> componentType) {
    return arrayOfValues(name, null, null, componentType);
  }

  @Override
  public ViewTemplateBuilder arrayOfValues(String name, String elementName,
      Class<?> componentType) {
    return arrayOfValues(name, elementName, null, componentType);
  }

  @Override
  public ViewTemplateBuilder arrayOfValues(String name, String elementName,
      String namespace, Class<?> componentType) {
    final ConcreteArrayOfValuesNode node = new ConcreteArrayOfValuesNode(name, elementName,
        namespace, componentType);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ConcreteArrayOfValuesNode node);

  @Override
  public ViewTemplateBuilder object(String name, Class<?> modelType) {
    return object(name, null, modelType);
  }

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType) {
    final ConcreteObjectNode node = new ConcreteObjectNode(name, namespace, modelType);
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
    final ConcreteContainerNode node = ((ComposableViewTemplate) template)
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
    final ConcreteReferenceNode node = new ConcreteReferenceNode(name, namespace, modelType);
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
    final ConcreteContainerNode node = ((ComposableViewTemplate) template)
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
    final ConcreteArrayOfObjectsNode node = new ConcreteArrayOfObjectsNode(name, elementName,
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
    final ConcreteContainerNode node = ((ComposableViewTemplate) template)
        .arrayOfObjects(name, elementName, namespace);
    addChildToTarget(node);
    return newValueNodeTemplateBuilder(node);
  }

  @Override
  public ViewTemplateBuilder arrayOfReferences(String name,
      Class<?> modelType) {
    return arrayOfReferences(name, null, null, modelType);
  }

  @Override
  public ViewTemplateBuilder arrayOfReferences(String name, String elementName,
      Class<?> modelType) {
    return arrayOfReferences(name, elementName, null, modelType);
  }

  @Override
  public ViewTemplateBuilder arrayOfReferences(String name, String elementName,
      String namespace, Class<?> modelType) {
    final ConcreteArrayOfReferencesNode node = new ConcreteArrayOfReferencesNode(name,
        elementName, namespace, modelType);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  @Override
  public ViewTemplateBuilder arrayOfReferences(String name,
      ViewTemplate template) {
    return arrayOfReferences(name, null, null, template);
  }

  @Override
  public ViewTemplateBuilder arrayOfReferences(String name, String elementName,
      ViewTemplate template) {
    return arrayOfReferences(name, elementName, null, template);
  }

  @Override
  public ViewTemplateBuilder arrayOfReferences(String name, String elementName,
      String namespace, ViewTemplate template) {
    assert template instanceof ComposableViewTemplate;
    final ConcreteContainerNode node = ((ComposableViewTemplate) template)
        .arrayOfReferences(name, elementName, namespace);
    addChildToTarget(node);
    return newValueNodeTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ConcreteContainerNode node);

  protected abstract ViewTemplateBuilder newValueNodeTemplateBuilder(ConcreteContainerNode node);

  @Override
  public ViewTemplateBuilder envelope(String name) {
    return envelope(name, null);
  }

  @Override
  public ViewTemplateBuilder envelope(String name, String namespace) {
    final ConcreteEnvelopeNode node = new ConcreteEnvelopeNode(name, namespace,
        target.getModelType());
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ConcreteEnvelopeNode node);

  @Override
  public ViewTemplateBuilder subtype(Class<?> subtype) {
    assertIsSubTypeOfModelType(subtype);
    assertTargetHasDiscriminator();
    final ConcreteSubtypeNode node = new ConcreteSubtypeNode(subtype);
    node.put(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY, true);
    addChildToTarget(node);
    return newTemplateBuilder(node);
  }

  protected abstract ViewTemplateBuilder newTemplateBuilder(ConcreteSubtypeNode node);

  private void assertIsSubTypeOfModelType(Class<?> subtype) {
    final Class<?> base = target.getModelType();
    if (!base.isAssignableFrom(subtype) || base.equals(subtype)) {
      throw new ViewTemplateException(subtype + " is not a subtype of "
          + base);
    }
  }

  private void assertTargetHasDiscriminator() {
    if (target.get(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY, Boolean.class) != null) return;
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
    target.put(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY, true);
    if (discriminator != null) {
      target.put(discriminator);
    }
    return this;
  }

  private void assertTargetHasNoChildren() {
    final List<ViewNode> children = target.getChildren();
    if (!children.isEmpty()) {
      throw new ViewTemplateException(
          "discriminator must be the first child of the parent node");
    }
  }

  private void assertDiscriminatorNotSet() {
    if (target.get(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY, Boolean.class) != null) {
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
        && this.node instanceof UpdatableNode) {
      this.node.setAccessor(accessorBuilder.build());
    }
  }

}
