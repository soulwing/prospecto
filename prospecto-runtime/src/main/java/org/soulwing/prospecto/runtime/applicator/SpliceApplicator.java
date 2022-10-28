/*
 * File created on Mar 24, 2017
 *
 * Copyright (c) 2017 Carl Harris, Jr
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
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.splice.SpliceHandler;
import org.soulwing.prospecto.api.template.SpliceNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.view.ConcreteView;

/**
 * An applicator for view events associated with a splice.
 *
 * @author Carl Harris
 */
class SpliceApplicator extends AbstractViewEventApplicator<SpliceNode> {

  SpliceApplicator(SpliceNode node) {
    super(node);
  }


  @Override
  Object onToModelValue(ViewEntity parentEntity,
      View.Event triggerEvent, Deque<View.Event> events,
      ScopedViewContext context) throws Exception {
    return node.getHandler().apply(node, slurpSplicedView(triggerEvent, events), context);
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    final SpliceHandler.Injector injector = node.get(SpliceHandler.Injector.class);
    if (injector != null) {
      injector.inject(target, value, context);
    }
  }

  private View slurpSplicedView(View.Event triggerEvent,
      Deque<View.Event> events) {
    final List<View.Event> slurpedEvents = new LinkedList<>();
    slurpSplicedViewEvents(triggerEvent, events, slurpedEvents);
    return new ConcreteView(slurpedEvents);
  }

  private void slurpSplicedViewEvents(View.Event triggerEvent,
      Deque<View.Event> events, List<View.Event> slurpedEvents) {
    slurpedEvents.add(triggerEvent);
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType() == triggerEvent.getType().complement()) {
        if (!Objects.equals(triggerEvent.getNamespace(), event.getNamespace())
            || !Objects.equals(triggerEvent.getName(), event.getName())) {
          throw new ViewInputException("spliced view is not well formed");
        }
        slurpedEvents.add(event);
        return;
      }
      if (event.getType() != event.getType().complement()) {
        if (!event.getType().isBegin()) {
          throw new ViewInputException("spliced view is not well formed");
        }
        slurpSplicedViewEvents(event, events, slurpedEvents);
      }
      else {
        slurpedEvents.add(event);
      }
    }
    throw new ViewInputException("spliced view is not well formed");
  }


}
