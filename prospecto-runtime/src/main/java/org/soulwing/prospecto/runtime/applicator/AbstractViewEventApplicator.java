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

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.listener.ViewMode;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * An abstract base for a {@link ViewEventApplicator} implementation.
 *
 * @param <N> view node type
 * @author Carl Harris
 */
abstract class AbstractViewEventApplicator<N extends ViewNode>
    implements ViewEventApplicator {

  protected final N node;

  AbstractViewEventApplicator(N node) {
    this.node = node;
  }

  @Override
  public N getNode() {
    return node;
  }

  @Override
  public final Object toModelValue(ViewEntity parentEntity,
      View.Event triggerEvent, Deque<View.Event> events,
      ScopedViewContext context) throws Exception {

    push(parentEntity, context);

    final ViewNodeEvent nodeEvent = new ViewNodeEvent(
        ViewMode.APPLY, node, parentEntity, context);

    Object value = UndefinedValue.INSTANCE;
    if (context.getListeners().shouldVisitNode(nodeEvent)) {
      value = onToModelValue(parentEntity, triggerEvent, events, context);
      context.getListeners().nodeVisited(nodeEvent);
    }

    pop(context);

    return value;
  }

  abstract Object onToModelValue(ViewEntity parentEntity,
      View.Event triggerEvent, Deque<View.Event> events,
      ScopedViewContext context) throws Exception;

  void push(ViewEntity entity, ScopedViewContext context) {
    final String name = node != null ? node.getName() : null;
    final Class<?> type = entity != null ? entity.getType() : null;
    context.push(name, type);
    if (entity != null) {
      context.put(entity);
    }
  }

  void pop(ScopedViewContext context) {
    context.pop();
  }

}
