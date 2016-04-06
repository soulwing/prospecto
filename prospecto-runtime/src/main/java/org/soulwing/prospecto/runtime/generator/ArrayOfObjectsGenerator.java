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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.node.ArrayOfObjectsNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.discriminator.ConcreteDiscriminatorEventService;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A generator for the events associated with an array-of-objects node.
 *
 * @author Carl Harris
 */
class ArrayOfObjectsGenerator
    extends AbstractViewEventGenerator<ArrayOfObjectsNode> {

  private final List<ViewEventGenerator> children;
  private final DiscriminatorEventService discriminatorEventService;

  ArrayOfObjectsGenerator(ArrayOfObjectsNode node,
      List<ViewEventGenerator> children) {
    this(node, children, ConcreteDiscriminatorEventService.INSTANCE);
  }

  ArrayOfObjectsGenerator(ArrayOfObjectsNode node,
      List<ViewEventGenerator> children,
      DiscriminatorEventService discriminatorEventService) {
    super(node);
    this.children = children;
    this.discriminatorEventService = discriminatorEventService;
  }

  @Override
  List<View.Event> onGenerate(Object model, ScopedViewContext context)
      throws Exception {
    final Iterator<?> i = node.iterator(model);
    if (i == null) {
      return Collections.singletonList((View.Event)
          new ConcreteViewEvent(View.Event.Type.VALUE, node.getName(),
              node.getNamespace(), null));
    }

    final List<View.Event> viewEvents = new LinkedList<>();
    viewEvents.add(new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY,
        node.getName(), node.getNamespace()));

    while (i.hasNext()) {

      final Object elementModel = i.next();

      context.push(node.getElementName(), elementModel.getClass());
      context.put(elementModel);

      final ViewNodePropertyEvent elementEvent = new ViewNodePropertyEvent(
          ViewNodeEvent.Mode.VIEW_GENERATION, node, model, elementModel, context);

      final Object transformedModel =
          context.getListeners().didExtractValue(elementEvent);

      if (transformedModel != UndefinedValue.INSTANCE) {
        viewEvents.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT,
            node.getElementName(), node.getNamespace()));

        if (discriminatorEventService.isDiscriminatorNeeded(node)) {
          viewEvents.add(discriminatorEventService.newDiscriminatorEvent(node,
              transformedModel.getClass(), context));
        }

        for (final ViewEventGenerator child : children) {
          viewEvents.addAll(child.generate(transformedModel, context));
        }

        viewEvents.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT,
            node.getElementName(), node.getNamespace()));
      }

      context.pop();
    }

    viewEvents.add(new ConcreteViewEvent(View.Event.Type.END_ARRAY,
        node.getName(), node.getNamespace()));

    return viewEvents;
  }
}
