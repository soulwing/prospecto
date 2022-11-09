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
import java.util.Map;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.template.AbstractContainerNode;
import org.soulwing.prospecto.runtime.template.AbstractValueNode;
import org.soulwing.prospecto.runtime.template.AbstractViewNode;
import org.soulwing.prospecto.runtime.template.ConcreteArrayOfValuesNode;
import org.soulwing.prospecto.runtime.template.ConcreteEnvelopeNode;
import org.soulwing.prospecto.runtime.template.ConcreteMapOfValuesNode;
import org.soulwing.prospecto.runtime.template.ConcreteMetaNode;
import org.soulwing.prospecto.runtime.template.ConcreteSpliceNode;
import org.soulwing.prospecto.runtime.template.ConcreteSubtypeNode;

/**
 * A template builder for a value node.
 *
 * @author Carl Harris
 */
class ValueNodeViewTemplateBuilder extends AbstractViewTemplateBuilder {

  private UnconfigurableNodeSupport delegate;

  ValueNodeViewTemplateBuilder(AbstractViewTemplateBuilder parent,
      AbstractContainerNode target, AbstractViewNode node) {
    super(parent, target, node);
    this.delegate = new UnconfigurableNodeSupport(node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(AbstractValueNode node) {
    return new ValueNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteMetaNode node) {
    return new MetaNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteSpliceNode node) {
    return new SpliceNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteArrayOfValuesNode node) {
    return new ValueNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteMapOfValuesNode node) {
    return new ValueNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(AbstractContainerNode node) {
    return new ContainerNodeViewTemplateBuilder(getParent(), node, node);
  }

  @Override
  protected ViewTemplateBuilder newValueNodeTemplateBuilder(
      AbstractContainerNode node) {
    return new ValueNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteEnvelopeNode node) {
    return new EnvelopeNodeViewTemplateBuilder(getParent(), node, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteSubtypeNode node) {
    return new SubtypeNodeViewTemplateBuilder(getParent(), node, node);
  }

  @Override
  public ViewTemplateBuilder discriminator() {
    return delegate.discriminator();
  }

  @Override
  public ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Object... configuration) {
    return delegate.discriminator(discriminatorClass, configuration);
  }

  @Override
  public ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Map configuration) {
    return delegate.discriminator(discriminatorClass, configuration);
  }

  @Override
  public ViewTemplateBuilder discriminator(DiscriminatorStrategy discriminator) {
    return delegate.discriminator(discriminator);
  }

  @Override
  public ViewTemplateBuilder source(String name) {
    assertNodeIsUpdatable("source");
    return super.source(name);
  }


  @Override
  public ViewTemplateBuilder accessType(AccessType accessType) {
    assertNodeIsUpdatable("accessType");
    getAccessorBuilder().accessType(accessType);
    return this;
  }

  @Override
  public ViewTemplateBuilder allow(EnumSet<AccessMode> modes) {
    assertNodeIsUpdatable("allow");
    return super.allow(modes);
  }

  @Override
  public ViewTemplateBuilder converter(ValueTypeConverter converter) {
    assertNodeIsUpdatable("allow");
    return super.converter(converter);
  }

  private void assertNodeIsUpdatable(String methodName) {
    if (!(getNode() instanceof UpdatableNode)) {
      throw new ViewTemplateException("cannot configure `" + methodName
          + "` on current node");
    }
  }

  @Override
  public ViewTemplateBuilder onEnd() {
    return getParent().end();
  }

  @Override
  public ViewTemplate build() {
    end();
    return getParent().build();
  }

}
