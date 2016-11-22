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

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.template.ArrayOfValuesNode;
import org.soulwing.prospecto.api.template.MapOfValuesNode;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ToManyMappedAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ValueCollectionToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ValueMapToManyMappedAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An applicator for a map-of-values node.
 *
 * @author Carl Harris
 */
class MapOfValuesApplicator
    extends AbstractViewEventApplicator<MapOfValuesNode> {

  private final TransformationService transformationService;
  private final ToManyMappedAssociationUpdater associationUpdater;

  MapOfValuesApplicator(MapOfValuesNode node) {
    this(node, ConcreteTransformationService.INSTANCE,
        ValueMapToManyMappedAssociationUpdater.INSTANCE);
  }

  MapOfValuesApplicator(MapOfValuesNode node,
      TransformationService transformationService,
      ToManyMappedAssociationUpdater associationUpdater) {
    super(node);
    this.transformationService = transformationService;
    this.associationUpdater = associationUpdater;
  }

  @Override
  Object onToModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {

    final Map<Object, Object> map = new HashMap<>();
    View.Event.Type lastType = triggerEvent.getType();
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      lastType = event.getType();
      if (lastType == triggerEvent.getType().complement()) break;
      if (lastType != View.Event.Type.VALUE) {
        throw new ViewApplicatorException(
            "unexpected non-value event in map-of-values");
      }

      context.push(event.getName());
      final Object valueToInject = transformationService.valueToInject(
          parentEntity, node.getComponentType(),
          event.getValue(), node, context);
      context.pop();

      final String key = event.getName();  // TODO -- allow conversion

      map.put(key, valueToInject);
    }

    if (lastType != triggerEvent.getType().complement()) {
      throw new ViewApplicatorException("expected "
          + triggerEvent.getType().complement() + " event");
    }

    return map;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    associationUpdater.findManagerAndUpdate(node, target,
        (Map<?, ?>) value,
        node.getDefaultManager(), context);
  }

}
