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
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.soulwing.prospecto.api.options.Options;

/**
 * A generator for a JSON-P array structure.
 *
 * @author Carl Harris
 */
class ArrayGenerator extends AbstractStructureGenerator<JsonArray> {

  private interface Generator {
    void generate(JsonArrayBuilder builder);
  }

  ArrayGenerator(Options options) {
    super(options);
  }

  private final List<Generator> generators = new ArrayList<>();

  @Override
  JsonArray finish() {
    final JsonArrayBuilder builder = Json.createArrayBuilder();
    for (final Generator generator : generators) {
      generator.generate(builder);
    }
    return builder.build();
  }

  @Override
  void storeObject(final String name, final JsonObject object) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(object);
      }
    });
  }

  @Override
  void storeArray(final String name, final JsonArray array) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(array);
      }
    });
  }

  @Override
  void generateNull(final String name) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.addNull();
      }
    });
  }

  @Override
  void generateBoolean(final String name, final Boolean value) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(value);
      }
    });
  }

  @Override
  void generateString(final String name, final String value) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(value);
      }
    });
  }

  @Override
  void generateLong(final String name, final Long value) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(value);
      }
    });
  }

  @Override
  void generateDouble(final String name, final Double value) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(value);
      }
    });

  }

  @Override
  void generateBigInteger(final String name, final BigInteger value) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(value);
      }
    });
  }

  @Override
  void generateBigDecimal(final String name, final BigDecimal value) {
    generators.add(new Generator() {
      @Override
      public void generate(JsonArrayBuilder builder) {
        builder.add(value);
      }
    });
  }

}
