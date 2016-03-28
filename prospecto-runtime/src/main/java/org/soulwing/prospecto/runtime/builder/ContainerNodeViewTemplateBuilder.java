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

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.EnvelopeNode;
import org.soulwing.prospecto.runtime.node.SubtypeNode;
import org.soulwing.prospecto.runtime.node.UrlNode;
import org.soulwing.prospecto.runtime.node.ValueNode;

/**
 * A template builder for a container node.
 *
 * @author Carl Harris
 */
class ContainerNodeViewTemplateBuilder extends AbstractViewTemplateBuilder {

  ContainerNodeViewTemplateBuilder(AbstractViewTemplateBuilder parent,
      ContainerViewNode target, AbstractViewNode node) {
    super(parent, target, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ValueNode node) {
    return new ValueNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(UrlNode node) {
    return new UrlNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ArrayOfValueNode node) {
    return new ValueNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ContainerViewNode node) {
    return new ContainerNodeViewTemplateBuilder(this, node, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(EnvelopeNode node) {
    return new EnvelopeNodeViewTemplateBuilder(this, node, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(SubtypeNode node) {
    return new SubtypeNodeViewTemplateBuilder(this, node, node);
  }

  @Override
  public ViewTemplateBuilder accessType(AccessType accessType) {
    setAccessType(accessType);
    return this;
  }


  @Override
  public ViewTemplateBuilder onEnd() {
    return getParent();
  }

  @Override
  public ViewTemplate build() {
    throw new ViewTemplateException(
        "build invoked on an interior node; an `end` must be missing");
  }

}
