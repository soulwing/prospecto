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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.xml.bind.DatatypeConverter;

import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.WriterKeys;

/**
 * An abstract base for generators that produce structural JSON-P elements.
 *
 * @author Carl Harris
 */
abstract class AbstractStructureGenerator<T extends JsonStructure> {

  private final boolean isoDateTime;

  AbstractStructureGenerator(Options options) {
    this.isoDateTime = options.isEnabled(WriterKeys.USE_ISO_DATETIME);
  }

  abstract T finish();

  abstract void storeObject(String name, JsonObject object);

  abstract void storeArray(String name, JsonArray array);

  void storeValue(String name, Object value) {
    if (value instanceof BigDecimal) {
      generateBigDecimal(name, (BigDecimal) value);
    }
    else if (value instanceof BigInteger) {
      generateBigInteger(name, (BigInteger) value);
    }
    else if (value instanceof Number) {
      if (value instanceof Double || value instanceof Float) {
        generateDouble(name, ((Number) value).doubleValue());
      }
      else {
        generateLong(name, ((Number) value).longValue());
      }
    }
    else if (value instanceof Boolean) {
      generateBoolean(name, (Boolean) value);
    }
    else if (value instanceof Date) {
      if (isoDateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) value);
        generateString(name, DatatypeConverter.printDateTime(calendar));
      }
      else {
        generateLong(name, ((Date) value).getTime());
      }
    }
    else if (value instanceof Calendar) {
      if (isoDateTime) {
        generateString(name, DatatypeConverter.printDateTime((Calendar) value));
      }
      else {
        generateLong(name, ((Calendar) value).getTimeInMillis());
      }
    }
    else if (value instanceof Enum) {
      generateString(name, ((Enum<?>) value).name());
    }
    else if (value instanceof JsonValue) {
      generateJsonValue(name, (JsonValue) value);
    }
    else if (value == null || value.toString() == null) {
      generateNull(name);
    }
    else {
      assert value != null;
      generateString(name, value.toString());
    }
  }

  private void generateJsonValue(String name, JsonValue value) {
    switch (value.getValueType()) {
      case STRING:
        generateString(name, ((JsonString) value).getString());
        break;
      case NUMBER:
        if (((JsonNumber) value).isIntegral()) {
          generateBigInteger(name, ((JsonNumber) value).bigIntegerValue());
        }
        else {
          generateBigDecimal(name, ((JsonNumber) value).bigDecimalValue());
        }
        break;
      case TRUE:
        generateBoolean(name, true);
        break;
      case FALSE:
        generateBoolean(name, false);
        break;
      case NULL:
        generateNull(name);
        break;
      case OBJECT:
        storeObject(name, (JsonObject) value);
        break;
      case ARRAY:
        storeArray(name, (JsonArray) value);
        break;
      default:
        throw new IllegalArgumentException("unrecognized type");
    }
  }

  abstract void generateNull(String name);

  abstract void generateBoolean(String name, Boolean value);

  abstract void generateString(String name, String value);

  abstract void generateLong(String name, Long value);

  abstract void generateDouble(String name, Double value);

  abstract void generateBigInteger(String name, BigInteger value);

  abstract void generateBigDecimal(String name, BigDecimal value);

}
