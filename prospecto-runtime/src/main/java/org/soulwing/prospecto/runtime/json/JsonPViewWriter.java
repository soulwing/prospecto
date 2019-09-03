/*
 * File created on Aug 28, 2019
 *
 * Copyright (c) 2019 Carl Harris, Jr
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
package org.soulwing.prospecto.runtime.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.json.JsonPTarget;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.runtime.text.AbstractViewWriter;

/**
 * A {@link ViewWriter} that produces a JSON-P structure.
 *
 * @author Carl Harris
 */
public class JsonPViewWriter extends AbstractViewWriter {

  private final Deque<AbstractStructureGenerator> generatorStack =
      new LinkedList<>();

  private boolean firstEvent = true;
  private boolean enveloped;
  private String nameInEnvelope;
  private JsonPTarget target;

  JsonPViewWriter(View view, Options options) {
    super(view, options);
  }

  @Override
  public void writeView(Target target) {
    if (!(target instanceof JsonPTarget)) {
      throw new IllegalArgumentException("this writer supports only the "
          + JsonPTarget.class.getSimpleName() + " target type");
    }
    this.target = (JsonPTarget) target;
    writeView();
  }

  @Override
  protected void beforeViewEvents() throws Exception {
    if (target == null) {
      throw new ViewException(
          "this writer must be invoked with an explicit target");
    }
  }

  @Override
  protected void afterViewEvents() throws Exception {
    if (target.toJson() == null) {
      throw new ViewException("unexpected end of view");
    }
  }

  @Override
  protected void onBeginObject(View.Event event) throws Exception {
    checkForEnvelope(event);
    generatorStack.push(new ObjectGenerator());
  }

  @Override
  protected void onEndObject(View.Event event) throws Exception {
    final JsonStructure structure = generatorStack.pop().finish();
    if (!(structure instanceof JsonObject)) {
      throw new ViewException("unexpected end of object");
    }
    if (!generatorStack.isEmpty()) {
      generatorStack.peek().storeObject(event.getName(), (JsonObject) structure);
    }
    else {
      storeInTarget(structure);
    }
  }

  @Override
  protected void onBeginArray(View.Event event) throws Exception {
    checkForEnvelope(event);
    generatorStack.push(new ArrayGenerator());
  }

  @Override
  protected void onEndArray(View.Event event) throws Exception {
    final JsonStructure structure = generatorStack.pop().finish();
    if (!(structure instanceof JsonArray)) {
      throw new ViewException("unexpected end of array");
    }
    if (!generatorStack.isEmpty()) {
      generatorStack.peek().storeArray(event.getName(), (JsonArray) structure);
    }
    else {
      storeInTarget(structure);
    }
  }

  @Override
  protected void onValue(View.Event event) throws Exception {
    storeValue(event);
  }

  @Override
  protected void onMeta(View.Event event) throws Exception {
    storeValue(event);
  }

  @Override
  protected void onDiscriminator(View.Event event) throws Exception {
    storeValue(event);
  }

  private void storeValue(View.Event event) {
    if (generatorStack.isEmpty()) {
      throw new ViewException("only views of object or array type are supported");
    }
    generatorStack.peek().storeValue(event.getName(), event.getValue());
  }

  private void storeInTarget(JsonStructure structure) {
    if (!enveloped) {
      target.store(structure);
    }
    else {
      final JsonObjectBuilder builder = generateEnvelope(getView().getEnvelope());
      builder.add(nameInEnvelope, structure);
      target.store(builder.build());
    }
  }

  private JsonObjectBuilder generateEnvelope(View.Envelope envelope) {
    final JsonObjectBuilder builder = Json.createObjectBuilder();
    for (final Map.Entry<String, Object> prop : envelope) {
      final String name = prop.getKey();
      final Object value = prop.getValue();
      if (value == null) {
        builder.addNull(name);
      }
      else if (value instanceof String) {
        builder.add(name, (String) value);
      }
      else if (value instanceof Boolean) {
        builder.add(name, (Boolean) value);
      }
      else if (value instanceof BigDecimal) {
        builder.add(name, (BigDecimal) value);
      }
      else if (value instanceof BigInteger) {
        builder.add(name, (BigInteger) value);
      }
      else if (value instanceof Number) {
        if (value instanceof Double || value instanceof Float) {
          builder.add(name, ((Number) value).doubleValue());
        }
        else {
          builder.add(name, ((Number) value).longValue());
        }
      }
      else if (value instanceof Date) {
        builder.add(name, ((Date) value).getTime());
      }
      else if (value instanceof Calendar) {
        builder.add(name, ((Calendar) value).getTimeInMillis());
      }
      else if (value instanceof Enum) {
        builder.add(name, ((Enum) value).name());
      }
      else {
        builder.add(name, value.toString());
      }
    }
    return builder;

  }

  private void checkForEnvelope(View.Event event) {
    if (!firstEvent) return;
    firstEvent = false;
    enveloped = isEnveloped(event);
    if (enveloped) {
      nameInEnvelope = event.getName();
    }
  }

  private boolean isEnveloped(View.Event event) {
    return event.getName() != null
        && ((event.getType() == View.Event.Type.BEGIN_ARRAY
            && (boolean) getOptions().get(WriterKeys.WRAP_ARRAY_IN_ENVELOPE, true)
            || (event.getType() == View.Event.Type.BEGIN_OBJECT
                && (boolean) getOptions().get(WriterKeys.WRAP_OBJECT_IN_ENVELOPE, false))));
  }

}
