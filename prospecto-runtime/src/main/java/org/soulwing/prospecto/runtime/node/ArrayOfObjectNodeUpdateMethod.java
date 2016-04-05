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

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A {@link ObjectNodeUpdateMethod} for an {@link ConcreteArrayOfObjectsNode}.
 *
 * @author Carl Harris
 */
class ArrayOfObjectNodeUpdateMethod
    implements UpdatableViewNodeTemplate.Method {

  private final ConcreteArrayOfObjectsNode node;
  private final ViewEntity parentEntity;
  private final View.Event triggerEvent;
  private final Deque<View.Event> events;
  private final ScopedViewContext context;
  private final UpdatableViewNodeTemplate template;

  public ArrayOfObjectNodeUpdateMethod(ConcreteArrayOfObjectsNode node,
      ViewEntity parentEntity, View.Event triggerEvent, Deque<View.Event> events,
      ScopedViewContext context, UpdatableViewNodeTemplate template) {
    this.node = node;
    this.parentEntity = parentEntity;
    this.triggerEvent = triggerEvent;
    this.events = events;
    this.context = context;
    this.template = template;
  }

  @Override
  public Object toModelValue() throws Exception {
    final List<MutableViewEntity> entities = new ArrayList<>();
    View.Event event = events.removeFirst();
    while (event != null
        && View.Event.Type.BEGIN_OBJECT.equals(event.getType())) {

      final UpdatableViewNodeTemplate.Method method =
          new ObjectNodeUpdateMethod(node, event, events, context);
      entities.add((MutableViewEntity)
          template.toModelValue(node, parentEntity, context, method));

      event = events.removeFirst();
    }
    if (event == null
        || event.getType() != triggerEvent.getType().complement()) {
      throw new ModelEditorException("expected END_ARRAY");
    }
    return entities;
  }

}
