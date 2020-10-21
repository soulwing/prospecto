/*
 * File created on Apr 7, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Deque;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.template.UpdatableValueNode;
import org.soulwing.prospecto.api.template.ValueNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An applicator for a value node.
 *
 * @author Carl Harris
 */
class ValueApplicator extends AbstractViewEventApplicator<ValueNode>
    implements InjectableViewEntity.ValueInjector {

  private final TransformationService transformationService;

  ValueApplicator(ValueNode node) {
    this(node, ConcreteTransformationService.INSTANCE);
  }

  ValueApplicator(ValueNode node,
      TransformationService transformationService) {
    super(node);
    this.transformationService = transformationService;
  }

  @Override
  Object onToModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {

    final Class<?> dataType = node.getDataType();
    if (JsonStructure.class.isAssignableFrom(dataType)) {
      if (JsonObject.class.isAssignableFrom(dataType)) {
        return jsonObject(triggerEvent, events);
      }
      else if (JsonArray.class.isAssignableFrom(dataType)) {
        return jsonArray(triggerEvent, events);
      }
      else {
        throw new ViewInputException("unrecognized JSON structure type");
      }
    }
    else {
      return transformationService.valueToInject(parentEntity, dataType,
          triggerEvent.getValue(), node, context);
    }
  }

  private JsonObject jsonObject(View.Event triggerEvent,
      Deque<View.Event> events) {
    if (triggerEvent.getType() != View.Event.Type.BEGIN_OBJECT) {
      throw new ViewInputException("expected start of JSON object");
    }
    final JsonObjectBuilder object = Json.createObjectBuilder();
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType().complement().equals(triggerEvent.getType())) {
        break;
      }
      switch (event.getType()) {
        case BEGIN_OBJECT:
          object.add(event.getName(), jsonObject(event, events));
          break;
        case BEGIN_ARRAY:
          object.add(event.getName(), jsonArray(event, events));
          break;
        case VALUE:
          object.add(event.getName(), jsonValue(event));
          break;
        default:
          throw new ViewInputException("unexpected event type " + event.getType());
      }
    }

    return object.build();
  }

  private JsonArray jsonArray(View.Event triggerEvent,
      Deque<View.Event> events) {
    if (triggerEvent.getType() != View.Event.Type.BEGIN_ARRAY) {
      throw new ViewInputException("expected start of JSON array");
    }
    final JsonArrayBuilder array = Json.createArrayBuilder();
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType().complement().equals(triggerEvent.getType())) {
        break;
      }
      switch (event.getType()) {
        case BEGIN_OBJECT:
          array.add(jsonObject(event, events));
          break;
        case BEGIN_ARRAY:
          array.add(jsonArray(event, events));
          break;
        case VALUE:
          array.add(jsonValue(event));
          break;
        default:
          throw new ViewInputException("unexpected event type " + event.getType());
      }
    }

    return array.build();

  }

  private JsonValue jsonValue(View.Event event) {
    Object value = event.getValue();
    if (value == null) {
      return JsonValue.NULL;
    }
    if (value instanceof String) {
      return Json.createValue((String) value);
    }
    if (value instanceof Boolean) {
      return ((Boolean) value) ? JsonValue.TRUE : JsonValue.FALSE;
    }
    if (value instanceof Integer || value instanceof Long) {
      return Json.createValue(((Number) value).longValue());
    }
    if (value instanceof BigInteger) {
      return Json.createValue((BigInteger) value);
    }
    if (value instanceof BigDecimal) {
      return Json.createValue((BigDecimal) value);
    }
    if (value instanceof Float || value instanceof Double) {
      return Json.createValue(((Number) value).doubleValue());
    }
    throw new ViewInputException("unsupported JSON value type");
  }


  @Override
  public void inject(Object target, Object value) throws Exception {
    if (!(node instanceof UpdatableValueNode)) return;
    if (((UpdatableValueNode) node).getAllowedModes().contains(AccessMode.WRITE)) {
      ((UpdatableValueNode) node).setValue(target, value);
    }
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    inject(target, value);
  }

}
