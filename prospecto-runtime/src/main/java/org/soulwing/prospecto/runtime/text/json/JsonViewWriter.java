/*
 * File created on Mar 19, 2016
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
package org.soulwing.prospecto.runtime.text.json;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.api.text.OutputStreamTarget;
import org.soulwing.prospecto.runtime.text.AbstractViewWriter;

/**
 * A {@link ViewWriter} that produces a JSON representation of a view.
 * <p>
 * Views that are configured with a name at the root node will be
 * <em>enveloped</em>.  An enveloped view has an additional JSON object as
 * a wrapper. The wrapper has a property whose name is the name of the root
 * view node whose value is the view itself. The wrapper has additional
 * properties as specified on the view's envelope.
 * <p>
 * If the root view node is not named, the view will have no envelope, and
 * any properties on the envelope will not appear in the textual representation.
 *
 * @author Carl Harris
 */
class JsonViewWriter extends AbstractViewWriter {

  private final JsonGeneratorFactory generatorFactory;

  enum GeneratorContext {
    OBJECT, ARRAY
  }

  private final Deque<GeneratorContext> contextStack = new LinkedList<>();

  private OutputStream outputStream;
  private JsonGenerator generator;
  private String viewName;
  private boolean enveloped;
  private boolean firstEvent = true;

  /**
   * Constructs a new instance.
   * @param view source view
   * @param options
   */
  JsonViewWriter(View view, Options options) {
    super(view, options);
    Map<String, Object> config = new HashMap<>();
    if (options.isEnabled(WriterKeys.PRETTY_PRINT_OUTPUT))
      config.put(JsonGenerator.PRETTY_PRINTING, true);

    generatorFactory = Json.createGeneratorFactory(config);
  }

  /**
   * Constructs a new instance.
   * @param view source view
   * @param outputStream target output stream for the textual representation
   * @param options
   */
   JsonViewWriter(View view,
      OutputStream outputStream, Options options) {
    this(view, options);
    this.outputStream = outputStream;
  }

  @Override
  public void writeView(Target target) {
    if (!(target instanceof OutputStreamTarget)) {
      throw new IllegalArgumentException("this writer supports only the "
          + OutputStreamTarget.class.getSimpleName() + " target");
    }
    this.outputStream = ((OutputStreamTarget) target).getOutputStream();
    writeView();
  }

  @Override
  protected void beforeViewEvents() {
    generator = generatorFactory.createGenerator(outputStream);
  }

  @Override
  protected void onBeginObject(View.Event event) throws Exception {
    writeStartEnvelope(event);
    writeStartObject(event);
  }

  @Override
  protected void onEndObject(View.Event event) throws Exception {
    writeEnd();
  }

  @Override
  protected void onBeginArray(View.Event event) throws Exception {
    writeStartEnvelope(event);
    writeStartArray(event);
  }

  @Override
  protected void onEndArray(View.Event event) throws Exception {
    writeEnd();
  }

  @Override
  protected void onValue(View.Event event) throws Exception {
    writeValue(event);
  }

  @Override
  protected void onMeta(View.Event event) throws Exception {
    writeValue(event);
  }

  @Override
  protected void onDiscriminator(View.Event event) throws Exception {
    writeValue(event);
  }

  @Override
  protected void afterViewEvents() throws Exception {
    if (enveloped) {
      writeEnd();
    }
    generator.flush();
  }

  private void writeStartEnvelope(View.Event event) {
    if (!firstEvent) return;
    enveloped = isEnveloped(event);
    if (!enveloped) return;
    generator.writeStartObject();
    for (final Map.Entry<String, Object> property : getView().getEnvelope()) {
      writeValue(property.getKey(), property.getValue());
    }
    contextStack.push(GeneratorContext.OBJECT);
  }

  private boolean isEnveloped(View.Event event) {
    if (event.getName() == null) return false;
    if (event.getType() == View.Event.Type.BEGIN_ARRAY) {
      return (boolean) getOptions().get(WriterKeys.WRAP_ARRAY_IN_ENVELOPE, true);
    }
    return (boolean) getOptions().get(WriterKeys.WRAP_OBJECT_IN_ENVELOPE, false);
  }

  private void writeStartObject(View.Event event) {
    final String name = event.getName();
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      if (firstEvent && !enveloped) {
        generator.writeStartObject();
      }
      else {
        generator.writeStartObject(name);
      }
    }
    else if (enveloped && viewName != null) {
      generator.writeStartObject(viewName);
      viewName = null;
    }
    else {
      generator.writeStartObject();
    }
    contextStack.push(GeneratorContext.OBJECT);
    firstEvent = false;
  }

  private void writeStartArray(View.Event event) {
    final String name = event.getName();
    if (name != null) {
      if (firstEvent && !enveloped) {
        generator.writeStartArray();
      }
      else {
        generator.writeStartArray(name);
      }
    }
    else if (enveloped && viewName != null) {
      generator.writeStartArray(viewName);
      viewName = null;
    }
    else {
      generator.writeStartArray();
    }
    contextStack.push(GeneratorContext.ARRAY);
    firstEvent = false;
  }

  private void writeEnd() {
    generator.writeEnd();
    contextStack.pop();
  }

  private void writeValue(View.Event event) {
    writeValue(event.getName(), event.getValue());
  }

  private void writeValue(String name, Object value) {

    if (value instanceof BigDecimal) {
      writeBigDecimal(name, (BigDecimal) value);
    }
    else if (value instanceof BigInteger) {
      writeBigInteger(name, (BigInteger) value);
    }
    else if (value instanceof Number) {
      if (value instanceof Double || value instanceof Float) {
        writeDouble(name, ((Number) value).doubleValue());
      }
      else {
        writeLong(name, ((Number) value).longValue());
      }
    }
    else if (value instanceof Boolean) {
      writeBoolean(name, (Boolean) value);
    }
    else if (value instanceof Date) {
      writeLong(name, ((Date) value).getTime());
    }
    else if (value instanceof Calendar) {
      writeLong(name, ((Calendar) value).getTimeInMillis());
    }
    else if (value instanceof Enum) {
      writeString(name, ((Enum) value).name());
    }
    else if (value == null || value.toString() == null) {
      writeNull(name);
    }
    else {
      writeString(name, value.toString());
    }
  }

  private void writeBigDecimal(String name, BigDecimal value) {
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      generator.write(name, value);
    }
    else {
      generator.write(value);
    }
  }

  private void writeBigInteger(String name, BigInteger value) {
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      generator.write(name, value);
    }
    else {
      generator.write(value);
    }
  }

  private void writeLong(String name, long value) {
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      generator.write(name, value);
    }
    else {
      generator.write(value);
    }
  }

  private void writeDouble(String name, double value) {
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      generator.write(name, value);
    }
    else {
      generator.write(value);
    }
  }

  private void writeBoolean(String name, Boolean value) {
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      generator.write(name, value);
    }
    else {
      generator.write(value);
    }
  }

  private void writeString(String name, String value) {
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      generator.write(name, value);
    }
    else {
      generator.write(value);
    }
  }

  private void writeNull(String name) {
    if (name != null && contextStack.peek() != GeneratorContext.ARRAY) {
      generator.writeNull(name);
    }
    else {
      generator.writeNull();
    }
  }

}
