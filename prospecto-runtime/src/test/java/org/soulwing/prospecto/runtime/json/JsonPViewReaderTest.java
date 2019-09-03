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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.withName;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.withNoName;

import java.math.BigDecimal;
import java.util.Iterator;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.json.JsonPSource;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;

/**
 * Unit tests for {@link JsonPViewReader}.
 *
 * @author Carl Harris
 */
public class JsonPViewReaderTest {


  private Options options = new OptionsMap();

  @Test
  public void testEmptyObject() throws Exception {

    final View view = new JsonPViewReader(
        new JsonPSource(Json.createObjectBuilder().build()), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withNoName())));
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testEmptyArray() throws Exception {

    final View view = new JsonPViewReader(
        new JsonPSource(Json.createArrayBuilder().build()), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_ARRAY, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_ARRAY, withNoName())));
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testFlatObject() throws Exception {

    final JsonObject object = Json.createObjectBuilder()
        .addNull("null")
        .add("boolean", true)
        .add("string", "string")
        .add("integral", 42)
        .add("decimal", BigDecimal.valueOf(1.1))
        .build();

    final View view = new JsonPViewReader(
        new JsonPSource(object), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("null"), whereValue(is(nullValue())))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("boolean"), whereValue(is(true)))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("string"), whereValue(is(equalTo("string"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("integral"), whereValue(is(equalTo(42L))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("decimal"), whereValue(is(equalTo(BigDecimal.valueOf(1.1)))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withNoName())));
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testFlatArray() throws Exception {

    final JsonArray array = Json.createArrayBuilder()
        .addNull()
        .add(true)
        .add("string")
        .add(42)
        .add(BigDecimal.valueOf(1.1))
        .build();

    final View view = new JsonPViewReader(
        new JsonPSource(array), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_ARRAY, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(nullValue())))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(true)))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo("string"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo(42L))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo(BigDecimal.valueOf(1.1)))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_ARRAY, withNoName())));
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testObjectWithNestedObject() throws Exception {
    final JsonObject object = Json.createObjectBuilder()
        .add("a", "a")
        .add("nested", Json.createObjectBuilder()
            .add("b", "b"))
        .add("c", "c")
        .build();

    final View view = new JsonPViewReader(
        new JsonPSource(object), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("a"), whereValue(is(equalTo("a"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withName("nested"))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("b"), whereValue(is(equalTo("b"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withName("nested"))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("c"), whereValue(is(equalTo("c"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withNoName())));
    assertThat(events.hasNext(), is(false));

  }

  @Test
  public void testObjectWithNestedArray() throws Exception {
    final JsonObject object = Json.createObjectBuilder()
        .add("a", "a")
        .add("nested", Json.createArrayBuilder()
            .add("b"))
        .add("c", "c")
        .build();

    final View view = new JsonPViewReader(
        new JsonPSource(object), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("a"), whereValue(is(equalTo("a"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_ARRAY, withName("nested"))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo("b"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_ARRAY, withName("nested"))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("c"), whereValue(is(equalTo("c"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withNoName())));
    assertThat(events.hasNext(), is(false));

  }

  @Test
  public void testArrayWithNestedObject() throws Exception {
    final JsonArray array = Json.createArrayBuilder()
        .add("a")
        .add(Json.createObjectBuilder()
            .add("b", "b"))
        .add("c")
        .build();

    final View view = new JsonPViewReader(
        new JsonPSource(array), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_ARRAY, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo("a"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withName("b"), whereValue(is(equalTo("b"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo("c"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_ARRAY, withNoName())));
    assertThat(events.hasNext(), is(false));

  }

  @Test
  public void testArrayWithNestedArray() throws Exception {
    final JsonArray array = Json.createArrayBuilder()
        .add("a")
        .add(Json.createArrayBuilder()
            .add("b"))
        .add("c")
        .build();

    final View view = new JsonPViewReader(
        new JsonPSource(array), options).readView();

    final Iterator<View.Event> events = view.iterator();
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_ARRAY, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo("a"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.BEGIN_ARRAY, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo("b"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_ARRAY, withNoName())));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.VALUE,
            withNoName(), whereValue(is(equalTo("c"))))));
    assertThat(events.next(),
        is(eventOfType(View.Event.Type.END_ARRAY, withNoName())));
    assertThat(events.hasNext(), is(false));

  }

}
