/*
 * File created on Aug 29, 2019
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

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.soulwing.prospecto.ViewOptionsRegistry;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.json.JsonPSource;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.runtime.text.AbstractViewReader;

/**
 * A {@link ViewReader} that reads a JSON-P structure to produce a
 * {@link View}.
 *
 * @author Carl Harris
 */
public class JsonPViewReader extends AbstractViewReader {

  private final JsonPSource source;

  public JsonPViewReader(Source source, Options options) {
    super(options);
    if (!(source instanceof JsonPSource)) {
      throw new IllegalArgumentException("only the "
          + JsonPSource.class.getSimpleName() + " source type is supported");
    }
    this.source = (JsonPSource) source;
  }

  @Override
  protected void onReadView() throws Exception {
    final JsonStructure structure = source.toJson();
    if (structure instanceof JsonArray) {
      readArray(null, (JsonArray) structure);
    }
    else {
      readObject(null, (JsonObject) structure);
    }
  }

  private void readObject(String name, JsonObject object) {
    beginObject(name);
    for (String key : object.keySet()) {
      final JsonValue value = object.get(key);
      switch (value.getValueType()) {
        case NULL:
          nullValue(key);
          break;
        case FALSE:
        case TRUE:
          value(key, object.getBoolean(key));
          break;
        case STRING:
          readString(key, (JsonString) value);
          break;
        case NUMBER:
          readNumber(key, (JsonNumber) value);
          break;
        case OBJECT:
          readObject(key, object.getJsonObject(key));
          break;
        case ARRAY:
          readArray(key, object.getJsonArray(key));
          break;
        default:
          throw new IllegalArgumentException("unrecognized JSON data type");
      }
    }
    end();
  }

  private void readArray(String name, JsonArray array) {
    beginArray(name);
    for (int i = 0; i < array.size(); i++) {
      final JsonValue value = array.get(i);
      switch (value.getValueType()) {
        case NULL:
          nullValue(null);
          break;
        case FALSE:
        case TRUE:
          value(null, array.getBoolean(i));
          break;
        case STRING:
          readString(null, (JsonString) value);
          break;
        case NUMBER:
          readNumber(null, (JsonNumber) value);
          break;
        case OBJECT:
          readObject(null, array.getJsonObject(i));
          break;
        case ARRAY:
          readArray(null, array.getJsonArray(i));
          break;
        default:
          throw new IllegalArgumentException("unrecognized JSON data type");
      }
    }
    end();
  }

  private void readString(String name, JsonString string) {
    if (isDiscriminator(name)) {
      discriminator(string.getString());
    }
    else {
      value(name, string.getString());
    }
  }

  private void readNumber(String name, JsonNumber number) {
    if (isDiscriminator(name)) {
      discriminator(number.longValue());
    }
    else if (number.isIntegral()) {
      value(name, number.longValue());
    }
    else {
      value(name, number.bigDecimalValue());
    }
  }

  private boolean isDiscriminator(String name) {
    return name != null && name.equals(
        ViewOptionsRegistry.getOptions().get(
            ViewKeys.DISCRIMINATOR_NAME, ViewDefaults.DISCRIMINATOR_NODE_NAME)
            .toString());
  }

}
