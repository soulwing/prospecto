/*
 * File created on Oct 21, 2020
 *
 * Copyright (c) 2020 Carl Harris, Jr
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
package org.soulwing.prospecto.api.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 * Unit tests for the {@link JsonSpliceHandler}.
 *
 * @author Carl Harris
 */
public class JsonSpliceHandlerTest {

  private final JsonObject object = Json.createObjectBuilder()
      .add("string", "string")
      .add("number", 42)
      .add("boolean", true)
      .add("object", Json.createObjectBuilder())
      .add("array", Json.createArrayBuilder())
      .build();

  private final JsonArray array = Json.createArrayBuilder()
      .add("string")
      .add(42)
      .add(true)
      .add(Json.createObjectBuilder())
      .add(Json.createArrayBuilder())
      .build();


  private JsonObject actualObject;
  private JsonArray actualArray;

  private JsonSpliceHandler.Producer objectProducer = new JsonSpliceHandler.Producer() {
    @Override
    public JsonStructure apply(SpliceNode node, ViewContext context) {
      return object;
    }
  };

  private JsonSpliceHandler.Consumer objectConsumer = new JsonSpliceHandler.Consumer() {
    @Override
    public void apply(JsonStructure structure, SpliceNode node, ViewContext context) {
      actualObject = (JsonObject) structure;
    }
  };

  private JsonSpliceHandler.Producer arrayProducer = new JsonSpliceHandler.Producer() {
    @Override
    public JsonStructure apply(SpliceNode node, ViewContext context) {
      return array;
    }
  };

  private JsonSpliceHandler.Consumer arrayConsumer = new JsonSpliceHandler.Consumer() {
    @Override
    public void apply(JsonStructure structure, SpliceNode node, ViewContext context) {
      actualArray = (JsonArray) structure;
    }
  };

  private ViewTemplate objectTemplate = ViewTemplateBuilderProducer
      .object(Object.class)
          .splice("json", JsonSpliceHandler.getInstance())
              .attribute(objectProducer)
              .attribute(objectConsumer)
          .end()
      .build();

  private ViewTemplate arrayTemplate = ViewTemplateBuilderProducer
      .object(Object.class)
          .splice("json", JsonSpliceHandler.getInstance())
              .attribute(arrayProducer)
              .attribute(arrayConsumer)
          .end()
      .build();

  private ViewTemplate bothTemplate = ViewTemplateBuilderProducer
      .object(Object.class)
          .splice("object", JsonSpliceHandler.getInstance())
              .attribute(objectProducer)
              .attribute(objectConsumer)
          .splice("array", JsonSpliceHandler.getInstance())
              .attribute(arrayProducer)
              .attribute(arrayConsumer)
          .end()
      .build();

  @Test
  public void testWithObjectSplice() throws Exception {
    final Object model = new Object();
    final ViewContext viewContext = ViewContextProducer.newContext();
    final View view = objectTemplate.generateView(model, viewContext);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ViewWriterFactoryProducer.getFactory("JSON")
        .newWriter(view, outputStream).writeView();
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());
    final View actualView = ViewReaderFactoryProducer.getFactory("JSON")
        .newReader(inputStream).readView();

    final Object actualModel =
        objectTemplate.createApplicator(actualView, viewContext).create();
    assertThat(actualModel, is(not(nullValue())));
    assertThat(actualObject, is(equalTo(object)));
  }

  @Test
  public void testWithArraySplice() throws Exception {
    final Object model = new Object();
    final ViewContext viewContext = ViewContextProducer.newContext();
    final View view = arrayTemplate.generateView(model, viewContext);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ViewWriterFactoryProducer.getFactory("JSON")
        .newWriter(view, outputStream).writeView();
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());
    final View actualView = ViewReaderFactoryProducer.getFactory("JSON")
        .newReader(inputStream).readView();

    final Object actualModel =
        arrayTemplate.createApplicator(actualView, viewContext).create();
    assertThat(actualModel, is(not(nullValue())));
    assertThat(actualArray, is(equalTo(array)));
  }

  @Test
  public void testWithBothSplices() throws Exception {
    final Object model = new Object();
    final ViewContext viewContext = ViewContextProducer.newContext();
    final View view = bothTemplate.generateView(model, viewContext);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ViewWriterFactoryProducer.getFactory("JSON")
        .newWriter(view, outputStream).writeView();
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());
    final View actualView = ViewReaderFactoryProducer.getFactory("JSON")
        .newReader(inputStream).readView();

    final Object actualModel =
        bothTemplate.createApplicator(actualView, viewContext).create();
    assertThat(actualModel, is(not(nullValue())));
    assertThat(actualObject, is(equalTo(object)));
    assertThat(actualArray, is(equalTo(array)));
  }


}
