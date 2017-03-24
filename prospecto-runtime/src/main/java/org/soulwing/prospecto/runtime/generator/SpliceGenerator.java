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
package org.soulwing.prospecto.runtime.generator;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.SpliceNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A generator for the events associated with a splice node.
 *
 * @author Carl Harris
 */
class SpliceGenerator extends AbstractViewEventGenerator<SpliceNode> {

  SpliceGenerator(SpliceNode node) {
    super(node);
  }

  @Override
  List<View.Event> onGenerate(Object parentModel, ScopedViewContext context)
      throws Exception {

    final View view = node.getHandler().generate(node, context);

    if (view == null) return Collections.emptyList();

    final List<View.Event> events = new LinkedList<>();

    final Iterator<View.Event> i = view.iterator();
    View.Event event = i.next();
    if (event != null) {
      events.add(renameEvent(event));
      while (i.hasNext()) {
        event = i.next();
        if (i.hasNext()) {
          events.add(event);
        }
      }
      events.add(renameEvent(event));
    }

    return events;
  }

  private ConcreteViewEvent renameEvent(View.Event event) {
    return new ConcreteViewEvent(event.getType(), node.getName(),
        node.getNamespace());
  }

}
