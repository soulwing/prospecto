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
package org.soulwing.prospecto.runtime.applicator;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.template.ArrayNode;
import org.soulwing.prospecto.api.template.MapNode;
import org.soulwing.prospecto.api.template.ValueNode;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Support for applicators that handle value nodes.
 *
 * @author Carl Harris
 */
class ValueApplicatorSupport {

  private final ViewNode node;

  private Class<?> type;
  private boolean convertValues;

  ValueApplicatorSupport(ViewNode node) {
    this.node = node;
  }

  Object consumeValue(View.Event triggerEvent, Deque<View.Event> events,
      ScopedViewContext context) throws Exception {
    if (type == null) {
      type = type(node);
      convertValues = isConvertible(type);
    }
    switch (triggerEvent.getType()) {
      case VALUE: {
        final Object value = triggerEvent.getValue();
        if (value == null || !convertValues) return value;
        return context.getValueTypeConverters()
            .toModelValue(type, triggerEvent.getValue(), node, context);
      }
      case BEGIN_OBJECT:
        return consumeObject(events, context);
      case BEGIN_ARRAY:
        return consumeArray(events, context);
      default:
        throw new ViewInputException(String.format(
            "unexpected event of type %s for value node",
            triggerEvent.getType().name()));
    }
  }

  private Map<Object, Object> consumeObject(Deque<View.Event> events,
      ScopedViewContext context)
      throws Exception {
    final Map<Object, Object> map = new LinkedHashMap<>();
    boolean foundEnd = false;
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType() == View.Event.Type.END_OBJECT) {
        foundEnd = true;
        break;
      }
      map.put(event.getName(), consumeValue(event, events, context));
    }
    if (!foundEnd) {
      throw new ViewApplicatorException("expected end of object");
    }
    return map;
  }

  private List<Object> consumeArray(Deque<View.Event> events,
      ScopedViewContext context) throws Exception {
    final List<Object> list = new LinkedList<>();
    boolean foundEnd = false;
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType() == View.Event.Type.END_ARRAY) {
        foundEnd = true;
        break;
      }
      list.add(consumeValue(event, events, context));
    }
    if (!foundEnd) {
      throw new ViewApplicatorException("expected end of array");
    }
    return list;
  }

  private static boolean isConvertible(Class<?> type) {
    return type != null
        && !Object.class.equals(type)
        && !Map.class.isAssignableFrom(type)
        && !Collection.class.isAssignableFrom(type)
        && !type.isArray();
  }

  private static Class<?> type(ViewNode node) {
    if (node instanceof ValueNode) {
      return ((ValueNode) node).getDataType();
    }
    if (node instanceof ArrayNode) {
      return ((ArrayNode) node).getComponentType();
    }
    if (node instanceof MapNode) {
      return ((MapNode) node).getComponentType();
    }
    throw new IllegalArgumentException("unrecognized node type");
  }


}
