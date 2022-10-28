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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

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
import org.soulwing.prospecto.api.splice.SpliceHandler;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 * Unit tests for the {@link JsonSpliceHandler}.
 *
 * @author Carl Harris
 */
public class JsonSpliceHandlerTest {

  public static class MockObject {}

  public static final Object INJECTED_VALUE = new Object();

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


  private Object injectedTarget;
  private Object injectedValue;

  private JsonSpliceHandler.Producer objectProducer = new JsonSpliceHandler.Producer() {
    @Override
    public JsonStructure apply(SpliceNode node, ViewContext context) {
      context.get(MockObject.class);
      return object;
    }
  };

  private JsonSpliceHandler.Consumer objectConsumer = new JsonSpliceHandler.Consumer() {
    @Override
    public Object apply(JsonStructure structure, SpliceNode node, ViewContext context) {
      return INJECTED_VALUE;
    }
  };

  private JsonSpliceHandler.Producer arrayProducer = new JsonSpliceHandler.Producer() {
    @Override
    public JsonStructure apply(SpliceNode node, ViewContext context) {
      context.get(MockObject.class);
      return array;
    }
  };

  private JsonSpliceHandler.Consumer arrayConsumer = new JsonSpliceHandler.Consumer() {
    @Override
    public Object apply(JsonStructure structure, SpliceNode node, ViewContext context) {
      return INJECTED_VALUE;
    }
  };

  private SpliceHandler.Injector injector = new SpliceHandler.Injector() {
    @Override
    public void inject(Object target, Object value) {
      injectedTarget = target;
      injectedValue = value;
    }
  };
  private ViewTemplate objectTemplate = ViewTemplateBuilderProducer
      .object(MockObject.class)
          .splice("json", JsonSpliceHandler.getInstance())
              .attribute(objectProducer)
              .attribute(objectConsumer)
              .attribute(injector)
          .end()
      .build();

  private ViewTemplate arrayTemplate = ViewTemplateBuilderProducer
      .object(MockObject.class)
          .splice("json", JsonSpliceHandler.getInstance())
              .attribute(arrayProducer)
              .attribute(arrayConsumer)
              .attribute(injector)
          .end()
      .build();

  private ViewTemplate bothTemplate = ViewTemplateBuilderProducer
      .object(MockObject.class)
          .splice("object", JsonSpliceHandler.getInstance())
              .attribute(objectProducer)
              .attribute(objectConsumer)
              .attribute(injector)
          .splice("array", JsonSpliceHandler.getInstance())
              .attribute(arrayProducer)
              .attribute(arrayConsumer)
              .attribute(injector)
          .end()
      .build();

  @Test
  public void testWithObjectSplice() throws Exception {
    final MockObject model = new MockObject();
    final ViewContext viewContext = ViewContextProducer.newContext();
    final View view = objectTemplate.generateView(model, viewContext);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ViewWriterFactoryProducer.getFactory("JSON")
        .newWriter(view, outputStream).writeView();
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());
    final View actualView = ViewReaderFactoryProducer.getFactory("JSON")
        .newReader(inputStream).readView();

    final MockObject actualModel = (MockObject)
        objectTemplate.createApplicator(actualView, viewContext).create();
    assertThat(actualModel, is(not(nullValue())));
    assertThat(injectedTarget, is(sameInstance(actualModel)));
    assertThat(injectedValue, is(sameInstance(INJECTED_VALUE)));
  }

  @Test
  public void testWithArraySplice() throws Exception {
    final MockObject model = new MockObject();
    final ViewContext viewContext = ViewContextProducer.newContext();
    final View view = arrayTemplate.generateView(model, viewContext);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ViewWriterFactoryProducer.getFactory("JSON")
        .newWriter(view, outputStream).writeView();
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());
    final View actualView = ViewReaderFactoryProducer.getFactory("JSON")
        .newReader(inputStream).readView();

    final MockObject actualModel = (MockObject)
        arrayTemplate.createApplicator(actualView, viewContext).create();
    assertThat(actualModel, is(not(nullValue())));
    assertThat(injectedTarget, is(sameInstance(actualModel)));
    assertThat(injectedValue, is(sameInstance(INJECTED_VALUE)));
  }

  @Test
  public void testWithBothSplices() throws Exception {
    final MockObject model = new MockObject();
    final ViewContext viewContext = ViewContextProducer.newContext();
    final View view = bothTemplate.generateView(model, viewContext);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ViewWriterFactoryProducer.getFactory("JSON")
        .newWriter(view, outputStream).writeView();
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());
    final View actualView = ViewReaderFactoryProducer.getFactory("JSON")
        .newReader(inputStream).readView();

    final MockObject actualModel = (MockObject)
        bothTemplate.createApplicator(actualView, viewContext).create();
    assertThat(actualModel, is(not(nullValue())));
    assertThat(injectedTarget, is(sameInstance(actualModel)));
    assertThat(injectedValue, is(sameInstance(INJECTED_VALUE)));
  }


}
