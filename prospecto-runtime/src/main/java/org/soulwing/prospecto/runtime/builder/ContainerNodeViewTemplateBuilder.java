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
 * A template builder for a container node.
 *
 * @author Carl Harris
 */
class ContainerNodeViewTemplateBuilder extends AbstractViewTemplateBuilder {

  ContainerNodeViewTemplateBuilder(AbstractViewTemplateBuilder parent,
      AbstractContainerNode target, AbstractViewNode node) {
    super(parent, target, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(AbstractValueNode node) {
    return new ValueNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteMetaNode node) {
    return new MetaNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteSpliceNode node) {
    return new SpliceNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteArrayOfValuesNode node) {
    return new ValueNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteMapOfValuesNode node) {
    return new ValueNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(AbstractContainerNode node) {
    return new ContainerNodeViewTemplateBuilder(this, node, node);
  }

  @Override
  protected ViewTemplateBuilder newValueNodeTemplateBuilder(
      AbstractContainerNode node) {
    return new ValueNodeViewTemplateBuilder(this, getTarget(), node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteEnvelopeNode node) {
    return new EnvelopeNodeViewTemplateBuilder(this, node, node);
  }

  @Override
  protected ViewTemplateBuilder newTemplateBuilder(ConcreteSubtypeNode node) {
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
