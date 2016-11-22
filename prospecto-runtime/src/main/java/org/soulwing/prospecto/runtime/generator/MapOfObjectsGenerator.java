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
import java.util.Map;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.listener.ViewMode;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.template.ArrayOfObjectsNode;
import org.soulwing.prospecto.api.template.MapOfObjectsNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.discriminator.ConcreteDiscriminatorEventService;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A generator for the events associated with a map-of-objects node.
 *
 * @author Carl Harris
 */
class MapOfObjectsGenerator
    extends AbstractViewEventGenerator<MapOfObjectsNode> {

  private final List<ViewEventGenerator> children;
  private final DiscriminatorEventService discriminatorEventService;

  MapOfObjectsGenerator(MapOfObjectsNode node,
      List<ViewEventGenerator> children) {
    this(node, children, ConcreteDiscriminatorEventService.INSTANCE);
  }

  MapOfObjectsGenerator(MapOfObjectsNode node,
      List<ViewEventGenerator> children,
      DiscriminatorEventService discriminatorEventService) {
    super(node);
    this.children = children;
    this.discriminatorEventService = discriminatorEventService;
  }

  @Override
  List<View.Event> onGenerate(Object model, ScopedViewContext context)
      throws Exception {
    if (!node.getAllowedModes().contains(AccessMode.READ)) {
      return Collections.emptyList();
    }

    final Iterator<Map.Entry> i = node.iterator(model);
    if (i == null) {
      return Collections.singletonList((View.Event)
          new ConcreteViewEvent(View.Event.Type.VALUE, node.getName(),
              node.getNamespace(), null));
    }

    final List<View.Event> viewEvents = new LinkedList<>();
    viewEvents.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT,
        node.getName(), node.getNamespace()));

    while (i.hasNext()) {

      final Map.Entry entry = i.next();
      final String key = entry.getKey().toString();  // TODO -- allow conversion
      final Object elementModel = entry.getValue();

      context.push(key);
      context.put(elementModel);

      final ViewNodePropertyEvent elementEvent = new ViewNodePropertyEvent(
          ViewMode.GENERATE, node, model, elementModel, context);

      final Object transformedModel =
          context.getListeners().didExtractValue(elementEvent);

      if (transformedModel != UndefinedValue.INSTANCE) {
        viewEvents.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT,
            key, node.getNamespace()));

        if (discriminatorEventService.isDiscriminatorNeeded(node)) {
          viewEvents.add(discriminatorEventService.newDiscriminatorEvent(node,
              transformedModel.getClass(), context));
        }

        for (final ViewEventGenerator child : children) {
          viewEvents.addAll(child.generate(transformedModel, context));
        }

        viewEvents.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT,
            key, node.getNamespace()));
      }

      context.pop();
    }

    viewEvents.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT,
        node.getName(), node.getNamespace()));

    return viewEvents;
  }
}
