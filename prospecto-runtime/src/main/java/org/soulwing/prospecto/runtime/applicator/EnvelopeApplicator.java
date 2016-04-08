/*
 * File created on Apr 7, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import java.util.List;

import org.soulwing.prospecto.api.node.EnvelopeNode;
import org.soulwing.prospecto.runtime.association.ConcreteToOneAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ToOneAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.ConcreteViewEntityFactory;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.entity.ViewEntityFactory;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An applicator for an envelope node.
 *
 * @author Carl Harris
 */
class EnvelopeApplicator extends AbstractObjectApplicator<EnvelopeNode> {

  EnvelopeApplicator(EnvelopeNode node, List<ViewEventApplicator> children) {
    this(node, children,
        ConcreteViewEntityFactory.INSTANCE,
        ConcreteTransformationService.INSTANCE,
        ConcreteToOneAssociationUpdater.INSTANCE,
        HierarchicalContainerApplicatorLocator.INSTANCE);
  }

  EnvelopeApplicator(EnvelopeNode node, List<ViewEventApplicator> children,
      ViewEntityFactory entityFactory,
      TransformationService transformationService,
      ToOneAssociationUpdater associationUpdater,
      ContainerApplicatorLocator applicatorLocator) {
    super(node, children, entityFactory, transformationService,
        associationUpdater, applicatorLocator);
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    ((MutableViewEntity) value).inject(target, context);
  }

}
