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

import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.node.ContainerNode;
import org.soulwing.prospecto.api.node.UpdatableNode;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.entity.ViewEntityFactory;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for container node applicators.
 *
 * @author Carl Harris
 */
abstract class AbstractContainerApplicator<N extends ViewNode>
    extends AbstractViewEventApplicator<N>  {

  private static final EnumSet<View.Event.Type> UPDATE_EVENT_TYPES =
      EnumSet.of(View.Event.Type.BEGIN_OBJECT,
          View.Event.Type.BEGIN_ARRAY,
          View.Event.Type.VALUE);

  final ViewEntityFactory entityFactory;
  final TransformationService transformationService;

  private final List<ViewEventApplicator> children;

  AbstractContainerApplicator(N node, List<ViewEventApplicator> children,
      ViewEntityFactory entityFactory,
      TransformationService transformationService) {
    super(node);
    this.children = children;
    this.entityFactory = entityFactory;
    this.transformationService = transformationService;
  }

  @Override
  Object onToModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
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

      final ViewEventApplicator applicator = findDescendant(entity.getType(), name);
      if (applicator == null) {
        if (context.getOptions().isEnabled(
            ViewKeys.IGNORE_UNKNOWN_PROPERTIES)) continue;

        throw new ModelEditorException("found no child named '" + name + "'"
            + " in node '" + node.getName() + "'");
      }

      if (!(applicator.getNode() instanceof UpdatableNode)) continue;

      Object value = null;
      if (applicator.getNode() instanceof ContainerNode
          && event.getType() == View.Event.Type.VALUE) {
        if (event.getValue() != null) {
          throw new ModelEditorException(
              "scalar value for object node must be null");
        }
        value = transformationService.valueToInject(parentEntity,
            node.getModelType(), null, node, context);
      }
      else {
        value = applicator.toModelValue(entity, event, events, context);
      }

      if (value != UndefinedValue.INSTANCE) {
        entity.put(name, value, applicator);
      }

    }

    if (eventType != triggerEvent.getType().complement()) {
      throw new ModelEditorException("expected " +
          triggerEvent.getType().complement());
    }

    return entity;
  }

  ViewEventApplicator findDescendant(Class<?> subtype, String name) {
    Iterator<ViewEventApplicator> i = children.iterator();
    while (i.hasNext()) {
      final ViewEventApplicator child = i.next();
      if (child instanceof SubtypeApplicator
          && subtype.isAssignableFrom(child.getNode().getModelType())) {
        final ViewEventApplicator descendant =
            ((SubtypeApplicator) child).findDescendant(subtype, name);
        if (descendant != null) return descendant;
      }
    }

    i = children.iterator();
    while (i.hasNext()) {
      final ViewEventApplicator child = i.next();
      if (name.equals(child.getNode().getName())) {
        return child;
      }
    }

    return null;
  }

}
