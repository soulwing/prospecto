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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.json.JsonPTarget;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.api.text.OutputStreamTarget;

/**
 * Unit tests for {@link JsonPViewWriter}.
 *
 * @author Carl Harris
 */
public class JsonPViewWriterTest {

  private static final Date DATE = new Date();
  private static final Calendar CALENDAR = Calendar.getInstance();

  @Rule
  public final ExpectedException expectedException = ExpectedException.none();

  private final MockView view = new MockView();

  private final Options options = new OptionsMap();

  private JsonPTarget target = new JsonPTarget();

  @Before
  public void setUp() throws Exception {
    options.put(WriterKeys.INCLUDE_NULL_PROPERTIES, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWriteViewWithNonJsonPTarget() throws Exception {
    new JsonPViewWriter(view, options).writeView(
        new OutputStreamTarget(new ByteArrayOutputStream()));
  }

  @Test(expected = ViewException.class)
  public void testWriteViewWithNoTarget() throws Exception {
    new JsonPViewWriter(view, options).writeView();
  }

  @Test
  public void testEmptyView() throws Exception {
    expectedException.expect(ViewException.class);
    expectedException.expectMessage("unexpected end of view");
    new JsonPViewWriter(view, options).writeView(target);
  }

  @Test
  public void testNonStructureView() throws Exception {
    view.addEvent(View.Event.Type.VALUE);
    expectedException.expect(ViewException.class);
    expectedException.expectMessage("object or array");
    new JsonPViewWriter(view, options).writeView(target);
  }

  @Test
  public void testEmptyObject() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_OBJECT);
    view.addEvent(View.Event.Type.END_OBJECT);
    new JsonPViewWriter(view, options).writeView(target);
    final JsonStructure structure = target.toJson();
    assertThat(structure, is(instanceOf(JsonObject.class)));
    assertThat(((JsonObject) structure).size(), is(equalTo(0)));
  }

  @Test
  public void testEmptyArray() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_ARRAY);
    view.addEvent(View.Event.Type.END_ARRAY);
    new JsonPViewWriter(view, options).writeView(target);
    final JsonStructure structure = target.toJson();
    assertThat(structure, is(instanceOf(JsonArray.class)));
    assertThat(((JsonArray) structure).size(), is(equalTo(0)));
  }

  @Test
  public void testStructureMismatchObjectArray() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_OBJECT);
    view.addEvent(View.Event.Type.END_ARRAY);
    expectedException.expect(ViewException.class);
    expectedException.expectMessage("unexpected end of");
    new JsonPViewWriter(view, options).writeView(target);
  }

  @Test
  public void testStructureMismatchArrayObject() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_ARRAY);
    view.addEvent(View.Event.Type.END_OBJECT);
    expectedException.expect(ViewException.class);
    expectedException.expectMessage("unexpected end of");
    new JsonPViewWriter(view, options).writeView(target);
  }

  @Test
  public void testMissingEnd() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_OBJECT);
    expectedException.expect(ViewException.class);
    expectedException.expectMessage("end of view");
    new JsonPViewWriter(view, options).writeView(target);
  }

  @Test
  public void testFlatObject() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_OBJECT);
    view.addEvent(View.Event.Type.VALUE, "null", null);
    view.addEvent(View.Event.Type.VALUE, "boolean", true);
    view.addEvent(View.Event.Type.VALUE, "string", "string");
    view.addEvent(View.Event.Type.VALUE, "int", 42);
    view.addEvent(View.Event.Type.VALUE, "long", 42L);
    view.addEvent(View.Event.Type.VALUE, "bigInteger", BigInteger.valueOf(42L));
    view.addEvent(View.Event.Type.VALUE, "float", 1.0f);
    view.addEvent(View.Event.Type.VALUE, "double", 1.0);
    view.addEvent(View.Event.Type.VALUE, "bigDecimal", BigDecimal.ONE);
    view.addEvent(View.Event.Type.VALUE, "date", DATE);
    view.addEvent(View.Event.Type.VALUE, "calendar", CALENDAR);
    view.addEvent(View.Event.Type.VALUE, "enum", View.Event.Type.VALUE);
    view.addEvent(View.Event.Type.META, "meta", "meta");
    view.addEvent(View.Event.Type.DISCRIMINATOR, "discriminator", "discriminator");
    view.addEvent(View.Event.Type.END_OBJECT);
    new JsonPViewWriter(view, options).writeView(target);
    final JsonObject object = (JsonObject) target.toJson();

    assertThat(object.isNull("null"), is(true));
    assertThat(object.getBoolean("boolean"), is(true));
    assertThat(object.getString("string"), is(equalTo("string")));
    assertThat(object.getInt("int"), is(equalTo(42)));
    assertThat(object.getJsonNumber("long").longValue(),
        is(equalTo(42L)));
    assertThat(object.getJsonNumber("bigInteger").bigIntegerValue(),
        is(equalTo(BigInteger.valueOf(42L))));
    assertThat(object.getJsonNumber("float").doubleValue(),
        is(equalTo(1.0)));
    assertThat(object.getJsonNumber("double").doubleValue(),
        is(equalTo(1.0)));
    assertThat(object.getJsonNumber("bigDecimal").bigDecimalValue(),
        is(equalTo(BigDecimal.ONE)));

    assertThat(object.getJsonNumber("date").longValue(),
        is(equalTo(DATE.getTime())));
    assertThat(object.getJsonNumber("calendar").longValue(),
        is(equalTo(CALENDAR.getTimeInMillis())));

    assertThat(object.getString("enum"),
        is(equalTo(View.Event.Type.VALUE.name())));

    assertThat(object.getString("meta"),
        is(equalTo("meta")));
    assertThat(object.getString("discriminator"),
        is(equalTo("discriminator")));
  }

  @Test
  public void testFlatArray() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_ARRAY);
    view.addEvent(View.Event.Type.VALUE, "null", null);
    view.addEvent(View.Event.Type.VALUE, "boolean", true);
    view.addEvent(View.Event.Type.VALUE, "string", "string");
    view.addEvent(View.Event.Type.VALUE, "int", 42);
    view.addEvent(View.Event.Type.VALUE, "long", 42L);
    view.addEvent(View.Event.Type.VALUE, "bigInteger", BigInteger.valueOf(42L));
    view.addEvent(View.Event.Type.VALUE, "float", 1.0f);
    view.addEvent(View.Event.Type.VALUE, "double", 1.0);
    view.addEvent(View.Event.Type.VALUE, "bigDecimal", BigDecimal.ONE);
    view.addEvent(View.Event.Type.VALUE, "date", DATE);
    view.addEvent(View.Event.Type.VALUE, "calendar", CALENDAR);
    view.addEvent(View.Event.Type.VALUE, "enum", View.Event.Type.VALUE);
    view.addEvent(View.Event.Type.END_ARRAY);
    new JsonPViewWriter(view, options).writeView(target);
    final JsonArray array = (JsonArray) target.toJson();
    assertThat(array.isNull(0), is(true));
    assertThat(array.getBoolean(1), is(true));
    assertThat(array.getString(2), is(equalTo("string")));
    assertThat(array.getInt(3), is(equalTo(42)));

    assertThat(array.getJsonNumber(4).longValue(),
        is(equalTo(42L)));
    assertThat(array.getJsonNumber(5).bigIntegerValue(),
        is(equalTo(BigInteger.valueOf(42L))));
    assertThat(array.getJsonNumber(6).doubleValue(),
        is(equalTo(1.0)));
    assertThat(array.getJsonNumber(7).doubleValue(),
        is(equalTo(1.0)));
    assertThat(array.getJsonNumber(8).bigDecimalValue(),
        is(equalTo(BigDecimal.ONE)));

    assertThat(array.getJsonNumber(9).longValue(),
        is(equalTo(DATE.getTime())));
    assertThat(array.getJsonNumber(10).longValue(),
        is(equalTo(CALENDAR.getTimeInMillis())));

    assertThat(array.getString(11),
        is(equalTo(View.Event.Type.VALUE.name())));

  }

  @Test
  public void testObjectWithNestedObject() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_OBJECT);
    view.addEvent(View.Event.Type.VALUE, "a", "a");
    view.addEvent(View.Event.Type.BEGIN_OBJECT, "object", null);
    view.addEvent(View.Event.Type.VALUE, "b", "b");
    view.addEvent(View.Event.Type.END_OBJECT, "object", null);
    view.addEvent(View.Event.Type.VALUE, "c", "c");
    view.addEvent(View.Event.Type.END_OBJECT);

    new JsonPViewWriter(view, options).writeView(target);
    final JsonObject object = (JsonObject) target.toJson();
    assertThat(object.getString("a"), is(equalTo("a")));
    assertThat(object.getString("c"), is(equalTo("c")));
    final JsonObject nested = object.getJsonObject("object");
    assertThat(nested, is(not(nullValue())));
    assertThat(nested.getString("b"), is(equalTo("b")));
  }

  @Test
  public void testObjectWithNestedArray() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_OBJECT);
    view.addEvent(View.Event.Type.VALUE, "a", "a");
    view.addEvent(View.Event.Type.BEGIN_ARRAY, "array", null);
    view.addEvent(View.Event.Type.VALUE, "b", "b");
    view.addEvent(View.Event.Type.END_ARRAY, "array", null);
    view.addEvent(View.Event.Type.VALUE, "c", "c");
    view.addEvent(View.Event.Type.END_OBJECT);

    new JsonPViewWriter(view, options).writeView(target);
    final JsonObject object = (JsonObject) target.toJson();
    assertThat(object.getString("a"), is(equalTo("a")));
    assertThat(object.getString("c"), is(equalTo("c")));
    final JsonArray nested = object.getJsonArray("array");
    assertThat(nested, is(not(nullValue())));
    assertThat(nested.getString(0), is(equalTo("b")));
  }

  @Test
  public void testArrayWithNestedArray() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_ARRAY);
    view.addEvent(View.Event.Type.VALUE, "a");
    view.addEvent(View.Event.Type.BEGIN_ARRAY);
    view.addEvent(View.Event.Type.VALUE, "b");
    view.addEvent(View.Event.Type.END_ARRAY);
    view.addEvent(View.Event.Type.VALUE, "c");
    view.addEvent(View.Event.Type.END_ARRAY);

    new JsonPViewWriter(view, options).writeView(target);
    final JsonArray array = (JsonArray) target.toJson();
    assertThat(array.getString(0), is(equalTo("a")));
    assertThat(array.getString(2), is(equalTo("c")));
    final JsonArray nested = array.getJsonArray(1);
    assertThat(nested, is(not(nullValue())));
    assertThat(nested.getString(0), is(equalTo("b")));
  }

  @Test
  public void testArrayWithNestedObject() throws Exception {
    view.addEvent(View.Event.Type.BEGIN_ARRAY);
    view.addEvent(View.Event.Type.VALUE, "a");
    view.addEvent(View.Event.Type.BEGIN_OBJECT);
    view.addEvent(View.Event.Type.VALUE, "b", "b");
    view.addEvent(View.Event.Type.END_OBJECT);
    view.addEvent(View.Event.Type.VALUE, "c");
    view.addEvent(View.Event.Type.END_ARRAY);

    new JsonPViewWriter(view, options).writeView(target);
    final JsonArray array = (JsonArray) target.toJson();
    assertThat(array.getString(0), is(equalTo("a")));
    assertThat(array.getString(2), is(equalTo("c")));
    final JsonObject nested = array.getJsonObject(1);
    assertThat(nested, is(not(nullValue())));
    assertThat(nested.getString("b"), is(equalTo("b")));
  }

  @Test
  public void testEnvelopedObjectView() throws Exception {
    final View.Envelope envelope = view.getEnvelope();
    envelope.putProperty("null", null);
    envelope.putProperty("boolean", true);
    envelope.putProperty("string", "string");
    envelope.putProperty("int", 42);
    envelope.putProperty("double", 1.0);
    envelope.putProperty("bigInteger", BigInteger.valueOf(42L));
    envelope.putProperty("bigDecimal", BigDecimal.ONE);
    envelope.putProperty("date", DATE);
    envelope.putProperty("calendar", CALENDAR);
    envelope.putProperty("enum", View.Event.Type.VALUE);
    envelope.putProperty("collection", Collections.emptyList());

    view.addEvent(View.Event.Type.BEGIN_OBJECT, "object", null);
    view.addEvent(View.Event.Type.VALUE, "foo", "bar");
    view.addEvent(View.Event.Type.END_OBJECT, "object", null);

    options.put(WriterKeys.WRAP_OBJECT_IN_ENVELOPE, true);
    new JsonPViewWriter(view, options).writeView(target);
    final JsonObject object = (JsonObject) target.toJson();

    assertThat(object.isNull("null"), is(true));
    assertThat(object.getBoolean("boolean"), is(true));
    assertThat(object.getString("string"), is(equalTo("string")));
    assertThat(object.getInt("int"), is(equalTo(42)));
    assertThat(object.getJsonNumber("double").doubleValue(), is(equalTo(1.0)));
    assertThat(object.getJsonNumber("bigInteger").bigIntegerValue(),
        is(equalTo(BigInteger.valueOf(42L))));
    assertThat(object.getJsonNumber("bigDecimal").bigDecimalValue(),
        is(equalTo(BigDecimal.ONE)));
    assertThat(object.getJsonNumber("date").longValue(),
        is(equalTo(DATE.getTime())));
    assertThat(object.getJsonNumber("calendar").longValue(),
        is(equalTo(CALENDAR.getTimeInMillis())));
    assertThat(object.getString("enum"),
        is(equalTo(View.Event.Type.VALUE.name())));
    assertThat(object.getString("collection"), is(equalTo("[]")));

    final JsonObject nested = object.getJsonObject("object");
    assertThat(nested, is(not(nullValue())));
    assertThat(nested.getString("foo"), is(equalTo("bar")));
  }

  @Test
  public void testEnvelopedArrayView() throws Exception {
    final View.Envelope envelope = view.getEnvelope();
    envelope.putProperty("null", null);
    envelope.putProperty("boolean", true);
    envelope.putProperty("string", "string");
    envelope.putProperty("int", 42);
    envelope.putProperty("double", 1.0);
    envelope.putProperty("bigInteger", BigInteger.valueOf(42L));
    envelope.putProperty("bigDecimal", BigDecimal.ONE);
    envelope.putProperty("date", DATE);
    envelope.putProperty("calendar", CALENDAR);
    envelope.putProperty("enum", View.Event.Type.VALUE);
    envelope.putProperty("collection", Collections.emptyList());

    view.addEvent(View.Event.Type.BEGIN_ARRAY, "array", null);
    view.addEvent(View.Event.Type.VALUE, null, "bar");
    view.addEvent(View.Event.Type.END_ARRAY, "array", null);

    options.put(WriterKeys.WRAP_OBJECT_IN_ENVELOPE, true);
    new JsonPViewWriter(view, options).writeView(target);
    final JsonObject object = (JsonObject) target.toJson();

    assertThat(object.isNull("null"), is(true));
    assertThat(object.getBoolean("boolean"), is(true));
    assertThat(object.getString("string"), is(equalTo("string")));
    assertThat(object.getInt("int"), is(equalTo(42)));
    assertThat(object.getJsonNumber("double").doubleValue(), is(equalTo(1.0)));
    assertThat(object.getJsonNumber("bigInteger").bigIntegerValue(),
        is(equalTo(BigInteger.valueOf(42L))));
    assertThat(object.getJsonNumber("bigDecimal").bigDecimalValue(),
        is(equalTo(BigDecimal.ONE)));
    assertThat(object.getJsonNumber("date").longValue(),
        is(equalTo(DATE.getTime())));
    assertThat(object.getJsonNumber("calendar").longValue(),
        is(equalTo(CALENDAR.getTimeInMillis())));
    assertThat(object.getString("enum"),
        is(equalTo(View.Event.Type.VALUE.name())));
    assertThat(object.getString("collection"), is(equalTo("[]")));

    final JsonArray nested = object.getJsonArray("array");
    assertThat(nested, is(not(nullValue())));
    assertThat(nested.getString(0), is(equalTo("bar")));
  }


  private static class MockView implements View {

    private static class MockEvent implements Event {

      private final Type type;
      private final String name;
      private final Object value;

      public MockEvent(Type type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
      }

      @Override
      public Type getType() {
        return type;
      }

      @Override
      public String getName() {
        return name;
      }

      @Override
      public String getNamespace() {
        return null;
      }

      @Override
      public Object getValue() {
        return value;
      }
    }

    private static class MockEnvelope implements Envelope {

      private final Map<String, Object> properties = new LinkedHashMap<>();

      @Override
      public Envelope putProperty(String name, Object value) {
        properties.put(name, value);
        return this;
      }

      @Override
      public Iterator<Map.Entry<String, Object>> iterator() {
        return properties.entrySet().iterator();
      }

      @Override
      public View seal(String name) {
        return seal(name, null);
      }

      @Override
      public View seal(String name, String namespace) {
        throw new UnsupportedOperationException();
      }
    }

    private final List<Event> events = new ArrayList<>();
    private final Envelope envelope = new MockEnvelope();

    private void addEvent(Event.Type type, String name, Object value) {
      events.add(new MockEvent(type, name, value));
    }

    private void addEvent(Event.Type type, Object value) {
      addEvent(type, null, value);
    }

    private void addEvent(Event.Type type) {
      addEvent(type, null, null);
    }


    @Override
    public Envelope getEnvelope() {
      return envelope;
    }

    @Override
    public Envelope envelope() {
      return envelope;
    }

    @Override
    public Iterator<Event> iterator() {
      return events.iterator();
    }
  }


}
