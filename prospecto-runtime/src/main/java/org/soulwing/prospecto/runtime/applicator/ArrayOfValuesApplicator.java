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

import java.util.Collection;
import java.util.Deque;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.template.ArrayOfValuesNode;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ValueCollectionToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An applicator for an array-of-values node.
 *
 * @author Carl Harris
 */
class ArrayOfValuesApplicator
    extends AbstractViewEventApplicator<ArrayOfValuesNode> {

  private final TransformationService transformationService;
  private final ToManyAssociationUpdater associationUpdater;

  ArrayOfValuesApplicator(ArrayOfValuesNode node) {
    this(node, ConcreteTransformationService.INSTANCE,
        ValueCollectionToManyAssociationUpdater.INSTANCE);
  }

  ArrayOfValuesApplicator(ArrayOfValuesNode node,
      TransformationService transformationService,
      ToManyAssociationUpdater associationUpdater) {
    super(node);
    this.transformationService = transformationService;
    this.associationUpdater = associationUpdater;
  }

  @Override
  Object onToModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {

    final Object value = ValueApplicatorSupport.INSTANCE
        .consumeValue(triggerEvent, events);
    if (value != null && !(value instanceof Collection) && !value.getClass().isArray()) {
      throw new ViewInputException("array node requires a collection or array input");
    }

    return transformationService.valueToInject(
        parentEntity, node.getComponentType(), value, node, context);
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    associationUpdater.findManagerAndUpdate(node, target, (Iterable<?>) value,
        node.getDefaultManager(), context);
  }

}
