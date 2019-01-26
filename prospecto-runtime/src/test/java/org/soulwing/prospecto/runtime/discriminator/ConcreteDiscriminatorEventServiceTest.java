/*
 * File created on Jan 26, 2019
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
package org.soulwing.prospecto.runtime.discriminator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.Iterator;

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * Unit tests for {@link ConcreteDiscriminatorEventService}.
 *
 * @author Carl Harris
 */
public class ConcreteDiscriminatorEventServiceTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private DiscriminatorStrategyLocator locator;

  private ConcreteDiscriminatorEventService service;

  @Before
  public void setUp() throws Exception {
    service = new ConcreteDiscriminatorEventService(locator);
  }

  @Test
  public void testSkipEventWithSimpleObject() throws Exception {
    final View.Event event = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.END_OBJECT)).iterator();

    ConcreteDiscriminatorEventService.skipEvent(event, events);
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testSkipEventWithSimpleArray() throws Exception {
    final View.Event event = eventOfType(View.Event.Type.BEGIN_ARRAY);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.END_ARRAY)).iterator();

    ConcreteDiscriminatorEventService.skipEvent(event, events);
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testSkipEventWithNestedObject() throws Exception {
    final View.Event event = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.END_OBJECT)).iterator();

    ConcreteDiscriminatorEventService.skipEvent(event, events);
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testSkipEventWithNestedArray() throws Exception {
    final View.Event event = eventOfType(View.Event.Type.BEGIN_ARRAY);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.BEGIN_ARRAY),
        eventOfType(View.Event.Type.END_ARRAY),
        eventOfType(View.Event.Type.END_ARRAY)).iterator();

    ConcreteDiscriminatorEventService.skipEvent(event, events);
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testSkipEventWithObjectSequence() throws Exception {
    final View.Event event = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.END_OBJECT)).iterator();

    ConcreteDiscriminatorEventService.skipEvent(event, events);
    assertThat(events.hasNext(), is(true));
    assertThat(events.next().getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.hasNext(), is(true));
    assertThat(events.next().getType(), is(equalTo(View.Event.Type.END_OBJECT)));
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testSkipEventWithObjectHavingMixedStructure() throws Exception {
    final View.Event event = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.END_OBJECT)).iterator();

    ConcreteDiscriminatorEventService.skipEvent(event, events);
    assertThat(events.hasNext(), is(false));
  }

  @Test
  public void testFindDiscriminatorAtStart() throws Exception {
    final View.Event triggerEvent = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.DISCRIMINATOR)).iterator();

    final View.Event event = service.findDiscriminatorEvent(triggerEvent, events);
    assertThat(event, is(not(nullValue())));
    assertThat(event.getType(), is(equalTo(View.Event.Type.DISCRIMINATOR)));
  }

  @Test
  public void testFindDiscriminatorInSimpleObject() throws Exception {
    final View.Event triggerEvent = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.DISCRIMINATOR)).iterator();

    final View.Event event = service.findDiscriminatorEvent(triggerEvent, events);
    assertThat(event, is(not(nullValue())));
    assertThat(event.getType(), is(equalTo(View.Event.Type.DISCRIMINATOR)));
  }

  @Test
  public void testFindDiscriminatorAfterNestedObject() throws Exception {
    final View.Event triggerEvent = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.DISCRIMINATOR)).iterator();

    final View.Event event = service.findDiscriminatorEvent(triggerEvent, events);
    assertThat(event, is(not(nullValue())));
    assertThat(event.getType(), is(equalTo(View.Event.Type.DISCRIMINATOR)));
  }

  @Test
  public void testFindDiscriminatorAfterDeeplyNestedObject() throws Exception {
    final View.Event triggerEvent = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.DISCRIMINATOR)).iterator();

    final View.Event event = service.findDiscriminatorEvent(triggerEvent, events);
    assertThat(event, is(not(nullValue())));
    assertThat(event.getType(), is(equalTo(View.Event.Type.DISCRIMINATOR)));
  }

  @Test
  public void testFindDiscriminatorAfterDiscriminatorsInNestedObjects()
      throws Exception {

    final View.Event triggerEvent = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final View.Event discriminator = eventOfType(View.Event.Type.DISCRIMINATOR, "one");

    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.DISCRIMINATOR, "two"),
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.DISCRIMINATOR, "three"),
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.END_OBJECT),
        discriminator).iterator();

    final View.Event event = service.findDiscriminatorEvent(triggerEvent, events);
    assertThat(event, is(not(nullValue())));
    assertThat(event.getType(), is(equalTo(View.Event.Type.DISCRIMINATOR)));
    assertThat(event.getValue(), is(equalTo((Object) "one")));
  }

  @Test
  public void testFindDiscriminatorWhenNotFound() throws Exception {
    final View.Event triggerEvent = eventOfType(View.Event.Type.BEGIN_OBJECT);
    final Iterator<View.Event> events = Arrays.asList(
        eventOfType(View.Event.Type.VALUE),
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.DISCRIMINATOR),  // this isn't the right one
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.END_OBJECT),
        eventOfType(View.Event.Type.VALUE, "next"),
        eventOfType(View.Event.Type.BEGIN_OBJECT),
        eventOfType(View.Event.Type.DISCRIMINATOR), // this isn't the right one
        eventOfType(View.Event.Type.END_OBJECT)).iterator();

    final View.Event event = service.findDiscriminatorEvent(triggerEvent, events);
    assertThat(event, is(nullValue()));
    assertThat(events.hasNext(), is(true));
    assertThat(events.next().getValue(), is(equalTo((Object) "next")));
  }


  private View.Event eventOfType(View.Event.Type type) {
    return new ConcreteViewEvent(type, null, null);
  }

  private View.Event eventOfType(View.Event.Type type, Object value) {
    return new ConcreteViewEvent(type, null, null, value);
  }

}