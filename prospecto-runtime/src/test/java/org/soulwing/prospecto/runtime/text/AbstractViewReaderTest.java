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
package org.soulwing.prospecto.runtime.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.runtime.event.ViewEventFactory;

/**
 * Unit tests for {@link AbstractViewReader}.
 *
 * @author Carl Harris
 */
public class AbstractViewReaderTest {

  private static final String NAME = "name";
  private static final String STRING = "string";
  private static final Number NUMBER = -1;
  private static final Boolean BOOLEAN = true;
  private static final String DISCRIMINATOR = "discriminator";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ViewEventFactory eventFactory;

  @Mock
  private View.Event beginEvent;

  @Mock
  private View.Event endEvent;

  @Mock
  private View.Event discriminatorEvent;

  @Mock
  private View.Event valueEvent;

  private MockViewReader viewReader;

  @Before
  public void setUp() throws Exception {
    viewReader = new MockViewReader(eventFactory);
  }

  @Test
  public void testBeginAndEndObject() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(eventFactory).newEvent(View.Event.Type.BEGIN_OBJECT,
            NAME, null, null);
        will(returnValue(beginEvent));
        oneOf(eventFactory).newEvent(View.Event.Type.END_OBJECT,
            NAME, null, null);
        will(returnValue(endEvent));
      }
    });

    viewReader.beginObject(NAME);
    viewReader.end();
    final Iterator<View.Event> events = viewReader.readView().iterator();
    assertThat(events.next(), is(sameInstance(beginEvent)));
    assertThat(events.next(), is(sameInstance(endEvent)));
  }

  @Test
  public void testBeginAndEndArray() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(eventFactory).newEvent(View.Event.Type.BEGIN_ARRAY,
            NAME, null, null);
        will(returnValue(beginEvent));
        oneOf(eventFactory).newEvent(View.Event.Type.END_ARRAY,
            NAME, null, null);
        will(returnValue(endEvent));
      }
    });

    viewReader.beginArray(NAME);
    viewReader.end();
    final Iterator<View.Event> events = viewReader.readView().iterator();
    assertThat(events.next(), is(sameInstance(beginEvent)));
    assertThat(events.next(), is(sameInstance(endEvent)));
  }

  @Test
  public void testValueString() throws Exception {
    context.checking(valueExpectations(NAME, STRING));
    viewReader.value(NAME, STRING);
    assertThat(viewReader.readView().iterator().next(),
        is(sameInstance(valueEvent)));
  }

  @Test
  public void testValueNumber() throws Exception {
    context.checking(valueExpectations(NAME, NUMBER));
    viewReader.value(NAME, NUMBER);
    assertThat(viewReader.readView().iterator().next(),
        is(sameInstance(valueEvent)));
  }

  @Test
  public void testValueBoolean() throws Exception {
    context.checking(valueExpectations(NAME, BOOLEAN));
    viewReader.value(NAME, BOOLEAN);
    assertThat(viewReader.readView().iterator().next(),
        is(sameInstance(valueEvent)));
  }

  @Test
  public void testNullValue() throws Exception {
    context.checking(valueExpectations(NAME, null));
    viewReader.nullValue(NAME);
    assertThat(viewReader.readView().iterator().next(),
        is(sameInstance(valueEvent)));
  }

  @Test
  public void testPromoteDiscriminator() throws Exception {
    context.checking(valueExpectations(NAME, STRING));
    context.checking(discriminatorExpectations(
        ViewDefaults.DISCRIMINATOR_NAME, DISCRIMINATOR));
    context.checking(new Expectations() {
      {
        oneOf(eventFactory).newEvent(View.Event.Type.BEGIN_OBJECT,
            NAME, null, null);
        will(returnValue(beginEvent));
        oneOf(eventFactory).newEvent(View.Event.Type.END_OBJECT,
            NAME, null, null);
        will(returnValue(endEvent));
        oneOf(valueEvent).getType();
        will((returnValue(View.Event.Type.VALUE)));
      }
    });

    viewReader.beginObject(NAME);
    viewReader.value(NAME, STRING);
    viewReader.discriminator(DISCRIMINATOR);
    viewReader.end();

    final Iterator<View.Event> events = viewReader.readView().iterator();
    assertThat(events.next(), is(sameInstance(beginEvent)));
    assertThat(events.next(), is(sameInstance(discriminatorEvent)));
    assertThat(events.next(), is(sameInstance(valueEvent)));
    assertThat(events.next(), is(sameInstance(endEvent)));
  }

  @Test(expected = ViewInputException.class)
  public void testMultipleDiscriminators() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(eventFactory).newEvent(View.Event.Type.BEGIN_OBJECT,
            NAME, null, null);
        will(returnValue(beginEvent));
        oneOf(eventFactory).newEvent(View.Event.Type.DISCRIMINATOR,
            ViewDefaults.DISCRIMINATOR_NAME, null, DISCRIMINATOR);
        will(returnValue(discriminatorEvent));
        oneOf(eventFactory).newEvent(View.Event.Type.END_OBJECT,
            NAME, null, null);
        will(returnValue(endEvent));
        oneOf(discriminatorEvent).getType();
        will((returnValue(View.Event.Type.DISCRIMINATOR)));
      }
    });

    viewReader.beginObject(NAME);
    viewReader.discriminator(DISCRIMINATOR);
    viewReader.discriminator(DISCRIMINATOR);
  }

  private Expectations valueExpectations(final String name,
      final Object value) {
    return new Expectations() {
      {
        oneOf(eventFactory).newEvent(View.Event.Type.VALUE, name, null, value);
        will(returnValue(valueEvent));
      }
    };
  }

  private Expectations discriminatorExpectations(final String name,
      final Object value) {
    return new Expectations() {
      {
        oneOf(eventFactory).newEvent(View.Event.Type.DISCRIMINATOR,
            name, null, value);
        will(returnValue(discriminatorEvent));
      }
    };
  }

  class MockViewReader extends AbstractViewReader {

    public MockViewReader(ViewEventFactory eventFactory) {
      super(new OptionsMap(), eventFactory);
    }

    @Override
    protected void onReadView() throws Exception {
    }

  }

}
