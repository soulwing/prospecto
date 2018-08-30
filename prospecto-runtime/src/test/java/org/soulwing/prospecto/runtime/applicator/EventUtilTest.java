/*
 * File created on Aug 30, 2018
 *
 * Copyright (c) 2018 Carl Harris, Jr
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
package org.soulwing.prospecto.runtime.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * Unit tests for {@link EventUtil}.
 *
 * @author Carl Harris
 */
public class EventUtilTest {

  @Test
  public void testConsumeSubtreeWithValue() throws Exception {
    final Deque<View.Event> events = events(
        beginObject("object"),
        value("value"),
        endObject("object"));
    EventUtil.consumeSubtree(events.removeFirst(), events);
    assertThat(events.isEmpty(), is(true));
  }

  @Test
  public void testConsumeSubtreeWithNestedObject() throws Exception {
    final Deque<View.Event> events = events(
        beginObject("object"),
        beginObject("nested"),
        endObject("nested"),
        endObject("object"));
    EventUtil.consumeSubtree(events.removeFirst(), events);
    assertThat(events.isEmpty(), is(true));
  }

  @Test
  public void testConsumeSubtreeWithNestedObjectsAndValues() throws Exception {
    final Deque<View.Event> events = events(
        beginObject("object"),
        value("value"),
        beginObject("nested"),
        value("value"),
        endObject("nested"),
        endObject("object"));
    EventUtil.consumeSubtree(events.removeFirst(), events);
    assertThat(events.isEmpty(), is(true));
  }

  private Deque<View.Event> events(View.Event... events) {
    final Deque<View.Event> deque = new LinkedList<>();
    deque.addAll(Arrays.asList(events));
    return deque;
  }

  private View.Event value(String name) {
    return newEvent(View.Event.Type.VALUE, name);
  }

  private View.Event beginObject(String name) {
    return newEvent(View.Event.Type.BEGIN_OBJECT, name);
  }

  private View.Event endObject(String name) {
    return newEvent(View.Event.Type.END_OBJECT, name);
  }

  private View.Event newEvent(View.Event.Type type, String name) {
    return new ConcreteViewEvent(type, name, null);
  }

}
