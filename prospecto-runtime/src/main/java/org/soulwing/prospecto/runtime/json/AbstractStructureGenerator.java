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
import javax.json.JsonObject;
import javax.json.JsonStructure;

/**
 * An abstract base for generators that produce structural JSON-P elements.
 *
 * @author Carl Harris
 */
abstract class AbstractStructureGenerator<T extends JsonStructure> {

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
      generateLong(name, ((Date) value).getTime());
    }
    else if (value instanceof Calendar) {
      generateLong(name, ((Calendar) value).getTimeInMillis());
    }
    else if (value instanceof Enum) {
      generateString(name, ((Enum) value).name());
    }
    else if (value == null || value.toString() == null) {
      generateNull(name);
    }
    else {
      assert value != null;
      generateString(name, value.toString());
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
