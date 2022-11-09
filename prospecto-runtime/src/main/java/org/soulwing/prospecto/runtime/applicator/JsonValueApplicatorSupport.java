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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Deque;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewInputException;

/**
 * Support for applicators that handle values nodes of JSON-P type.
 *
 * @author Carl Harris
 */
class JsonValueApplicatorSupport {

  static final JsonValueApplicatorSupport INSTANCE =
      new JsonValueApplicatorSupport();

  private JsonValueApplicatorSupport() {
  }

  JsonValue consumeValue(View.Event triggerEvent, Deque<View.Event> events) {
    switch (triggerEvent.getType()) {
      case VALUE:
        return consumeValue(triggerEvent);
      case BEGIN_OBJECT:
        return consumeObject(events);
      case BEGIN_ARRAY:
        return consumeArray(events);
      default:
        throw new ViewInputException(String.format(
            "unexpected event of type %s for value node",
            triggerEvent.getType().name()));
    }
  }

  private JsonObject consumeObject(Deque<View.Event> events) {
    final JsonObjectBuilder builder = Json.createObjectBuilder();
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType() == View.Event.Type.END_OBJECT) break;
      builder.add(event.getName(), consumeValue(event, events));
    }
    return builder.build();
  }

  private JsonArray consumeArray(Deque<View.Event> events) {
    final JsonArrayBuilder builder = Json.createArrayBuilder();
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType() == View.Event.Type.END_ARRAY) break;
      builder.add(consumeValue(event, events));
    }
    return builder.build();
  }

  private JsonValue consumeValue(View.Event event) {
    final Object value = event.getValue();
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

}
