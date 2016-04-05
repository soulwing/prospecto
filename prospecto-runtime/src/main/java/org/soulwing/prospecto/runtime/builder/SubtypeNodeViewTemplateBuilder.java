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
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ConcreteContainerNode;

/**
 * A template builder for a subtype node.
 *
 * @author Carl Harris
 */
class SubtypeNodeViewTemplateBuilder extends ContainerNodeViewTemplateBuilder {

  private final UnconfigurableNodeSupport delegate;

  SubtypeNodeViewTemplateBuilder(AbstractViewTemplateBuilder parent,
      ConcreteContainerNode target, AbstractViewNode node) {
    super(parent, target, node);
    delegate = new UnconfigurableNodeSupport(node);
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
    return delegate.source(name);
  }

  @Override
  public ViewTemplateBuilder allow(AccessMode mode, AccessMode... modes) {
    return delegate.allow(mode, modes);
  }

  @Override
  public ViewTemplateBuilder allow(EnumSet<AccessMode> modes) {
    return delegate.allow(modes);
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Object... configuration) {
    return delegate.converter(converterClass, configuration);
  }

  @Override
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Map configuration) {
    return delegate.converter(converterClass, configuration);
  }

  @Override
  public ViewTemplateBuilder converter(ValueTypeConverter<?> converter) {
    return delegate.converter(converter);
  }

}
