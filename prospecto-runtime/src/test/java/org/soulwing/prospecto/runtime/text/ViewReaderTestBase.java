/*
 * File created on Mar 20, 2016
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
package org.soulwing.prospecto.runtime.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.discriminator.Discriminator;

/**
 * Common infrastructure and tests for view reader test classes.
 *
 * @author Carl Harris
 */
public abstract class ViewReaderTestBase {

  private final String fileSuffix;

  public ViewReaderTestBase(String fileSuffix) {
    this.fileSuffix = fileSuffix;
  }

  protected ViewReader newViewReader(InputStream inputStream) {
    return newViewReader(inputStream, Collections.<String, Object>emptyMap());
  }

  protected abstract ViewReader newViewReader(InputStream inputStream,
      Map<String, Object> properties);

  @Test
  public void testFlatObjectView() throws Exception {
    final ViewReader reader = newViewReader(getTestResource("flatObjectView"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    validateObjectProperties(events);
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_OBJECT)));
  }

  @Test
  public void testNestedObjectView() throws Exception {
    final ViewReader reader = newViewReader(getTestResource("nestedObjectView"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT, Constants.OBJECT_NAME)));
    validateObjectProperties(events);
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_OBJECT, Constants.OBJECT_NAME)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_OBJECT)));
  }

  @Test
  public void testArrayOfObjectsView() throws Exception {
    final ViewReader reader = newViewReader(
        getTestResource("arrayOfObjectsView"));
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

  private static void validateObjectProperties(Iterator<View.Event> events) {
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "string", Constants.STRING_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "integral", Constants.INTEGRAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "decimal", Constants.DECIMAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "boolean", Constants.BOOLEAN_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, "null", null)));
  }

  @Test
  public void testArrayOfValuesView() throws Exception {
    final ViewReader reader = newViewReader(
        getTestResource("arrayOfValuesView"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_ARRAY)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, Constants.STRING_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, Constants.INTEGRAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, Constants.DECIMAL_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, Constants.BOOLEAN_VALUE)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.VALUE, null, null)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.END_ARRAY)));
  }

  @Test
  public void testDefaultDiscriminatorView() throws Exception {
    final ViewReader reader = newViewReader(
        getTestResource("defaultDiscriminatorView"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.DISCRIMINATOR, Discriminator.DEFAULT_NAME,
        Constants.DISCRIMINATOR_VALUE)));
  }

  @Test
  public void testDefaultUrlView() throws Exception {
    final ViewReader reader = newViewReader(
        getTestResource("defaultUrlView"));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.URL, null, Constants.URL_VALUE)));
  }

  @Test
  public void testCustomUrlView() throws Exception {
    final ViewReader reader = newViewReader(
        getTestResource("customUrlView"),
        Collections.<String, Object>singletonMap(
            ReaderKeys.URL_NAME, Constants.CUSTOM_NAME));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.URL, null, Constants.URL_VALUE)));
  }


  protected InputStream getTestResource(String resourceName) {
    final InputStream inputStream = getClass().getResourceAsStream(
        resourceName + fileSuffix);
    assertThat(inputStream, is(not(nullValue())));
    return inputStream;
  }

  protected static Matcher<View.Event> eventWith(View.Event.Type type) {
    return eventWith(type, null, null);
  }

  protected static Matcher<View.Event> eventWith(View.Event.Type type,
      String name) {
    return eventWith(type, name, null);
  }


  protected static Matcher<View.Event> eventWith(View.Event.Type type,
      String name, Object value) {
    return allOf(
        not(nullValue()),
        hasProperty("type", equalTo(type)),
        hasProperty("name", equalTo(name)),
        hasProperty("value", equalTo(value)));
  }

}
