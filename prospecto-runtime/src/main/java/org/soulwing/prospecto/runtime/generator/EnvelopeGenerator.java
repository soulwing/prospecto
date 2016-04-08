/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.runtime.generator;

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.EnvelopeNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A generator for the events associated with an envelope node.
 *
 * @author Carl Harris
 */
class EnvelopeGenerator extends AbstractViewEventGenerator<EnvelopeNode> {

  private final List<ViewEventGenerator> children;

  EnvelopeGenerator(EnvelopeNode node,
      List<ViewEventGenerator> children) {
    super(node);
    this.children = children;
  }

  @Override
  List<View.Event> onGenerate(Object model, ScopedViewContext context)
      throws Exception {
    final List<View.Event> events = new LinkedList<>();

    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT,
        node.getName(), node.getNamespace()));

    for (final ViewEventGenerator child : children) {
      events.addAll(child.generate(model, context));
    }

    events.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT,
        node.getName(), node.getNamespace()));

    return events;
  }

  @Override
  void push(Object model, ScopedViewContext context) {
    context.push(node.getName(), null);
  }

}
