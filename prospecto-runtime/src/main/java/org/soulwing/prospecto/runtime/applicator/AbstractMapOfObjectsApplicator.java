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

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.template.ArrayOfObjectsNode;
import org.soulwing.prospecto.api.template.MapOfObjectsNode;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ToManyMappedAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.entity.ViewEntityFactory;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for {@link MapOfObjectsApplicator} subtypes.
 *
 * @author Carl Harris
 */
abstract class AbstractMapOfObjectsApplicator<N extends MapOfObjectsNode>
    extends AbstractContainerApplicator<N> {


  protected final ToManyMappedAssociationUpdater associationUpdater;

  AbstractMapOfObjectsApplicator(N node, List<ViewEventApplicator> children,
      ViewEntityFactory entityFactory,
      TransformationService transformationService,
      ToManyMappedAssociationUpdater associationUpdater,
      ContainerApplicatorLocator applicatorLocator) {
    super(node, children, entityFactory, transformationService,
        applicatorLocator);
    this.associationUpdater = associationUpdater;
  }

  @Override
  Object onToModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {

    final Map<Object, Object> entities = new HashMap<>();

    View.Event.Type lastEvent = triggerEvent.getType();
    while (!events.isEmpty()) {

      final View.Event event = events.removeFirst();
      lastEvent = event.getType();

      if (lastEvent == triggerEvent.getType().complement()) break;

      if (lastEvent == View.Event.Type.VALUE && event.getValue() != null) {
          throw new ViewApplicatorException(
              "scalar value for object node must be null");
      }

      if (lastEvent != View.Event.Type.BEGIN_OBJECT
          && lastEvent != View.Event.Type.VALUE) {
        throw new ViewApplicatorException(
            "unexpected non-object event in map-of-objects");
      }

      context.push(event.getName(), Object.class);

      Object entity = null;

      if (lastEvent == View.Event.Type.BEGIN_OBJECT) {
        entity = super.onToModelValue(parentEntity, event, events, context);
      }
      else {
        entity = transformationService.valueToInject(parentEntity,
            node.getComponentType(), null, node, context);
      }

      context.pop();

      if (entity != UndefinedValue.INSTANCE) {
        entities.put(event.getName(), entity);
      }

    }

    if (lastEvent != triggerEvent.getType().complement()) {
      throw new ViewApplicatorException("expected "
          + triggerEvent.getType().complement() + " event");
    }

    return entities;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    if (node.getAllowedModes().contains(AccessMode.WRITE)) {
      associationUpdater.findManagerAndUpdate(node, target,
          (Map<?, ?>) value, node.getDefaultManager(), context);
    }
  }

}
