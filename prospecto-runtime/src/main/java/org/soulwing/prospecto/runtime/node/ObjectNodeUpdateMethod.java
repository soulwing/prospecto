/*
 * File created on Apr 2, 2016
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
package org.soulwing.prospecto.runtime.node;

import java.util.Deque;
import java.util.EnumSet;

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.options.ModelEditorKeys;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * An {@link UpdatableViewNodeTemplate.Method} for an {@link ObjectNode}.
 *
 * @author Carl Harris
 */
class ObjectNodeUpdateMethod implements UpdatableViewNodeTemplate.Method {

  private static final EnumSet<View.Event.Type> UPDATE_EVENT_TYPES =
      EnumSet.of(View.Event.Type.BEGIN_OBJECT,
          View.Event.Type.BEGIN_ARRAY,
          View.Event.Type.VALUE);

  private final ContainerViewNode node;
  private final View.Event triggerEvent;
  private final Deque<View.Event> events;
  private final ScopedViewContext context;
  private final ViewEntityFactory entityFactory;

  public ObjectNodeUpdateMethod(ContainerViewNode node,
      View.Event triggerEvent, Deque<View.Event> events,
      ScopedViewContext context) {
    this(node, triggerEvent, events, context,
        ConcreteViewEntityFactory.INSTANCE);
  }

  ObjectNodeUpdateMethod(ContainerViewNode node, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context,
      ViewEntityFactory entityFactory) {
    this.node = node;
    this.triggerEvent = triggerEvent;
    this.events = events;
    this.context = context;
    this.entityFactory = entityFactory;
  }

  @Override
  public Object toModelValue() throws Exception {
    final MutableViewEntity entity = entityFactory.newEntity(node, events, context);
    View.Event.Type eventType = triggerEvent.getType();
    while (!events.isEmpty()) {
      View.Event event = events.removeFirst();
      eventType = event.getType();
      if (eventType == triggerEvent.getType().complement()) break;

      if (!UPDATE_EVENT_TYPES.contains(event.getType())) {
        // assumes that the event is not a BEGIN_*/END_* pair
        continue;
      }

      final String name = event.getName();
      if (name == null) {
        throw new ModelEditorException("unexpected anonymous event: " + event);
      }

      final AbstractViewNode child = node.getChild(entity.getType(), name);
      if (child == null) {
        if (context.getOptions().isEnabled(
            ModelEditorKeys.IGNORE_UNKNOWN_PROPERTIES)) continue;

        throw new ModelEditorException("found no child named '" + name + "'"
            + " in node " + node.getName());
      }

      if (!(child instanceof UpdatableViewNode)) continue;

      if (child instanceof ContainerViewNode
          && event.getType() == View.Event.Type.VALUE) {
        if (event.getValue() != null) {
          throw new ModelEditorException(
              "scalar value for object node must be null");
        }
        entity.put(name, null, (UpdatableViewNode) child);
      }
      else {
        final Object value = ((UpdatableViewNode) child)
            .toModelValue(entity, event, events, context);
        if (value != UndefinedValue.INSTANCE) {
          entity.put(name, value, (UpdatableViewNode) child);
        }
      }

    }

    if (eventType != triggerEvent.getType().complement()) {
      throw new ModelEditorException("expected " +
          triggerEvent.getType().complement());
    }

    return entity;
  }

}
