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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.soulwing.prospecto.api.View;

/**
 * Tests for {@link JsonViewReader}.
 *
 * @author Carl Harris
 */
public class JsonViewReaderTest {

  private static final String STRING_VALUE = "string";
  private static final long INTEGRAL_VALUE = -1L;
  private static final BigDecimal DECIMAL_VALUE = BigDecimal.valueOf(2.71828);
  private static final boolean BOOLEAN_VALUE = true;
  private static final String URL_VALUE = "url";
  private static final String CUSTOM_NAME = "custom";

  @Test
  public void testFlatObjectView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("flatObjectView.json"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    validateObjectProperties(events);
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_OBJECT)));
  }

  @Test
  public void testNestedObjectView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("nestedObjectView.json"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT, "object")));
    validateObjectProperties(events);
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_OBJECT, "object")));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_OBJECT)));
  }

  @Test
  public void testArrayOfObjectsView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("arrayOfObjectsView.json"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_ARRAY)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    validateObjectProperties(events);
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_ARRAY)));
  }

  @Test
  public void testArrayOfValuesView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("arrayOfValuesView.json"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_ARRAY)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, STRING_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, INTEGRAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, DECIMAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, BOOLEAN_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, null)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_ARRAY)));
  }

  private static void validateObjectProperties(Iterator<View.Event> events) {
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "string", STRING_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "integral", INTEGRAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "decimal", DECIMAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "boolean", BOOLEAN_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "null", null)));
  }

  @Test
  public void testDefaultStringDiscriminatorView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("defaultStringDiscriminatorView.json"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.DISCRIMINATOR, null, STRING_VALUE)));
  }

  @Test
  public void testDefaultNumericDiscriminatorView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("defaultNumericDiscriminatorView.json"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.DISCRIMINATOR, null, INTEGRAL_VALUE)));
  }

  @Test
  public void testCustomStringDiscriminatorView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("customStringDiscriminatorView.json"),
        Collections.<String, Object>singletonMap(
            JsonViewReader.DISCRIMINATOR_NAME_KEY, CUSTOM_NAME));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.DISCRIMINATOR, null, STRING_VALUE)));
  }

  @Test
  public void testCustomNumericDiscriminatorView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("customNumericDiscriminatorView.json"),
        Collections.<String, Object>singletonMap(
            JsonViewReader.DISCRIMINATOR_NAME_KEY, CUSTOM_NAME));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.DISCRIMINATOR, null, INTEGRAL_VALUE)));
  }

  @Test
  public void testDefaultUrlView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("defaultUrlView.json"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.URL, null, URL_VALUE)));
  }

  @Test
  public void testCustomUrlView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        newResourceStream("customUrlView.json"),
        Collections.<String, Object>singletonMap(
            JsonViewReader.URL_NAME_KEY, CUSTOM_NAME));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.URL, null, URL_VALUE)));
  }

  private InputStream newResourceStream(String resourceName)
      throws IOException {
    final InputStream inputStream = getClass().getResourceAsStream(resourceName);
    assertThat(inputStream, is(not(nullValue())));
    return inputStream;
  }

  private static Matcher<View.Event> eventWith(View.Event.Type type) {
    return eventWith(type, null, null);
  }

  private static Matcher<View.Event> eventWith(View.Event.Type type,
      String name) {
    return eventWith(type, name, null);
  }


  private static Matcher<View.Event> eventWith(View.Event.Type type,
      String name, Object value) {
    return allOf(
        not(nullValue()),
        hasProperty("type", equalTo(type)),
        hasProperty("name", equalTo(name)),
        hasProperty("value", equalTo(value)));
  }

}
