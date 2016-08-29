/*
 * File created on Mar 21, 2016
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.stream.JsonParser;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.runtime.text.ViewWriterTestBase;

/**
 * Tests for {@link JsonViewWriter}.
 *
 * @author Carl Harris
 */
public class JsonViewWriterTest extends ViewWriterTestBase {

  public JsonViewWriterTest() {
    super(".json");
  }

  @Override
  protected ViewWriter newViewWriter(View view, OutputStream outputStream,
      Options options) {
    return new JsonViewWriter(view, outputStream, options);
  }

  @Override
  protected void validateView(InputStream actual,
      InputStream expected, Options options) throws Exception {
    JsonParser testParser = Json.createParser(expected);
    JsonParser viewParser = Json.createParser(actual);
    while (testParser.hasNext()) {
      assertThat(viewParser.hasNext(), is(true));
      JsonParser.Event testEvent = testParser.next();
      JsonParser.Event viewEvent = viewParser.next();
      assertThat(viewEvent, is(equalTo(testEvent)));
    }
  }

  @Test
  public void testUnenvelopedObjectView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT, "view"));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT, "view"));

    writeAndValidateView("flatObjectView", events);
  }

  @Test
  public void testEnvelopedObjectView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT, "view"));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT, "view"));

    options.put(WriterKeys.WRAP_OBJECT_IN_ENVELOPE, true);
    writeAndValidateView("envelopedFlatObjectView", events);
  }

  @Test
  public void testUnenvelopedArrayOfObjectsView() throws Exception {
    final List<View.Event> events = new ArrayList<>();

    events.add(newEvent(View.Event.Type.BEGIN_ARRAY, "view"));
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT));
    events.add(newEvent(View.Event.Type.END_ARRAY, "view"));

    options.put(WriterKeys.WRAP_ARRAY_IN_ENVELOPE, false);
    writeAndValidateView("arrayOfObjectsView", events);
  }

  @Test
  public void testEnvelopedArrayOfObjectsView() throws Exception {
    final List<View.Event> events = new ArrayList<>();

    events.add(newEvent(View.Event.Type.BEGIN_ARRAY, "view"));
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT));
    events.add(newEvent(View.Event.Type.END_ARRAY, "view"));

    writeAndValidateView("envelopedArrayOfObjectsView", events);
  }

  @Test
  public void testUnenvelopedArrayOfValuesView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_ARRAY, "view"));
    addArrayValues(events);
    events.add(newEvent(View.Event.Type.END_ARRAY, "view"));

    options.put(WriterKeys.WRAP_ARRAY_IN_ENVELOPE, false);
    writeAndValidateView("arrayOfValuesView", events);
  }

  @Test
  public void testEnvelopedArrayOfValuesView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_ARRAY, "view"));
    addArrayValues(events);
    events.add(newEvent(View.Event.Type.END_ARRAY, "view"));

    writeAndValidateView("envelopedArrayOfValuesView", events);
  }


}
