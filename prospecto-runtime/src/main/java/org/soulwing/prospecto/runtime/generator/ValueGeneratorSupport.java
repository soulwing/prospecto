/*
 * File created on Nov 7, 2022
 *
 * Copyright (c) 2022 Carl Harris, Jr
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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * Support for generating events for value nodes.
 *
 * @author Carl Harris
 */
class ValueGeneratorSupport {

  private final ViewNode node;

  ValueGeneratorSupport(ViewNode node) {
    this.node = node;
  }

  List<View.Event> valueEvents(String name, Object value,
      ScopedViewContext context) throws Exception {
    final List<View.Event> events = new LinkedList<>();
    if (value instanceof Map) {
      events.addAll(mapEvents(name, (Map<?, ?>) value, context));
    }
    else if (value instanceof Collection) {
      events.addAll(collectionEvents(name, (Collection<?>) value, context));
    }
    else if (value != null && value.getClass().isArray()) {
      events.addAll(collectionEvents(name, Arrays.asList((Object[]) value), context));
    }
    else if (value != UndefinedValue.INSTANCE) {
      events.add(new ConcreteViewEvent(View.Event.Type.VALUE,
          name, node.getNamespace(), value));
    }
    return events;
  }

  private List<View.Event> mapEvents(String name, Map<?, ?> map,
      ScopedViewContext context) throws Exception {
    final List<View.Event> events = new LinkedList<>();
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT,
        name, node.getNamespace()));
    for (final Map.Entry<?, ?> entry : map.entrySet()) {
      final String childKey = context.getKeyTypeConverters().toViewKey(
          entry.getKey(), node, context);

      final Object childValue = context.getValueTypeConverters().toViewValue(
          entry.getValue(), node, context);

      events.addAll(valueEvents(childKey, childValue, context));
    }
    events.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT,
        name, node.getNamespace()));
    return events;
  }

  private List<View.Event> collectionEvents(String name, Iterable<?> collection,
      ScopedViewContext context) throws Exception {
    final List<View.Event> events = new LinkedList<>();
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY,
        name, node.getNamespace()));

    for (final Object element : collection) {
      final Object childValue = context.getValueTypeConverters().toViewValue(
          element, node, context);

      events.addAll(valueEvents(null, childValue, context));
    }

    events.add(new ConcreteViewEvent(View.Event.Type.END_ARRAY,
        name, node.getNamespace()));
    return events;
  }

}
