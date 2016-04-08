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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.ObjectNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.discriminator.ConcreteDiscriminatorEventService;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A generator for the events associated with an object node.
 *
 * @author Carl Harris
 */
class ObjectGenerator extends AbstractViewEventGenerator<ObjectNode> {

  private final List<ViewEventGenerator> children;

  private final DiscriminatorEventService discriminatorEventService;

  ObjectGenerator(ObjectNode node, List<ViewEventGenerator> children) {
    this(node, children, ConcreteDiscriminatorEventService.INSTANCE);
  }

  ObjectGenerator(ObjectNode node, List<ViewEventGenerator> children,
      DiscriminatorEventService discriminatorEventService) {
    super(node);
    this.children = children;
    this.discriminatorEventService = discriminatorEventService;
  }

  @Override
  List<View.Event> onGenerate(Object owner, ScopedViewContext context)
      throws Exception {
    final List<View.Event> viewEvents = new LinkedList<>();
    final Object model = node.getObject(owner);
    if (model == UndefinedValue.INSTANCE) return Collections.emptyList();

    if (model != null) {
      viewEvents.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT,
          node.getName(), node.getNamespace()));
      if (discriminatorEventService.isDiscriminatorNeeded(node)) {
        viewEvents.add(discriminatorEventService.newDiscriminatorEvent(node,
            model.getClass(), context));
      }
      for (final ViewEventGenerator child : children) {
        viewEvents.addAll(child.generate(model, context));
      }
      viewEvents.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT,
          node.getName(), node.getNamespace()));
    }
    else {
      viewEvents.add(new ConcreteViewEvent(View.Event.Type.VALUE,
          node.getName(), node.getNamespace(), null));
    }
    return viewEvents;
  }

}
