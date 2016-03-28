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

import java.util.Map;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.EnvelopeNode;
import org.soulwing.prospecto.runtime.node.SubtypeNode;
import org.soulwing.prospecto.runtime.node.UrlNode;
import org.soulwing.prospecto.runtime.node.ValueNode;

/**
 * A template builder for a value node.
 *
 * @author Carl Harris
 */
class ValueNodeViewTemplateBuilder extends AbstractViewTemplateBuilder {

  private UnconfigurableNodeSupport delegate;

  ValueNodeViewTemplateBuilder(AbstractViewTemplateBuilder parent,
      ContainerViewNode target, AbstractViewNode node) {
    super(parent, target, node);
    this.delegate = new UnconfigurableNodeSupport(node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ValueNode node) {
    return new ValueNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(UrlNode node) {
    return new UrlNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ArrayOfValueNode node) {
    return new ValueNodeViewTemplateBuilder(getParent(), getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ContainerViewNode node) {
    return new ContainerNodeViewTemplateBuilder(getParent(), node, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(EnvelopeNode node) {
    return new EnvelopeNodeViewTemplateBuilder(getParent(), node, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(SubtypeNode node) {
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
  public ViewTemplateBuilder accessType(AccessType accessType) {
    getAccessorBuilder().accessType(accessType);
    return this;
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
