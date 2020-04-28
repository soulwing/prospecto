/*
 * File created on Apr 28, 2020
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
package org.soulwing.prospecto.runtime.view;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.withName;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.withNoName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * Unit tests for {@link ConcreteView}.
 *
 * @author Carl Harris
 */
public class ConcreteViewTest {

  @Test
  public void testEnvelopeObject() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    final ConcreteView view = new ConcreteView(events);
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null));
    events.add(new ConcreteViewEvent(View.Event.Type.VALUE, "string", null, "string"));
    events.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT, null, null));
    final View envelopedView = view.envelope()
        .putProperty("type", Object.class.getSimpleName())
        .seal("subview");
    final Iterator<View.Event> result = envelopedView.iterator();
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withNoName())));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.VALUE, withName("type"),
            whereValue(is(equalTo("Object"))))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withName("subview"))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.VALUE, withName("string"),
            whereValue(is(equalTo("string"))))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withName("subview"))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withNoName())));
  }

  @Test
  public void testEnvelopeArray() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    final ConcreteView view = new ConcreteView(events);
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, null, null));
    events.add(new ConcreteViewEvent(View.Event.Type.VALUE, null, null, "string"));
    events.add(new ConcreteViewEvent(View.Event.Type.END_ARRAY, null, null));
    final View envelopedView = view.envelope()
        .putProperty("type", Object.class.getSimpleName())
        .seal("subview");
    final Iterator<View.Event> result = envelopedView.iterator();
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.BEGIN_OBJECT, withNoName())));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.VALUE, withName("type"),
            whereValue(is(equalTo("Object"))))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.BEGIN_ARRAY, withName("subview"))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.VALUE,
            whereValue(is(equalTo("string"))))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.END_ARRAY, withName("subview"))));
    assertThat(result.next(),
        is(eventOfType(View.Event.Type.END_OBJECT, withNoName())));
  }

}