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
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.MapOfValuesNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * A generator for the events associated with a map-of-values node.
 *
 * @author Carl Harris
 */
class MapOfValuesGenerator
    extends AbstractViewEventGenerator<MapOfValuesNode> {

  private final ValueGeneratorSupport generatorSupport;
  private final TransformationService transformationService;

  MapOfValuesGenerator(MapOfValuesNode node) {
    this(node, ConcreteTransformationService.INSTANCE);
  }

  MapOfValuesGenerator(MapOfValuesNode node,
      TransformationService transformationService) {
    super(node);
    this.generatorSupport = new ValueGeneratorSupport(node);
    this.transformationService = transformationService;
  }

  @Override
  List<View.Event> onGenerate(Object owner, ScopedViewContext context)
      throws Exception {

    if (!node.getAllowedModes().contains(AccessMode.READ)) {
      return Collections.emptyList();
    }

    final Iterator<Map.Entry<?, ?>> i = node.iterator(owner);
    final List<View.Event> events = new LinkedList<>();

    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT,
        node.getName(), node.getNamespace()));

    while (i.hasNext()) {
      final Map.Entry<?, ?> entry = i.next();
      context.push(entry.getKey());
      final Map.Entry<String, ?> transformedValue =
          transformationService.pairToExtract(owner, entry, node, context);
      context.pop();

      events.addAll(generatorSupport.valueEvents(transformedValue.getKey(),
          transformedValue.getValue(), context));

    }

    events.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT,
        node.getName(), node.getNamespace()));

    return events;
  }

}
