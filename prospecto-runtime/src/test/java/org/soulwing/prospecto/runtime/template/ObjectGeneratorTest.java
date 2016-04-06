/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.runtime.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.node.ObjectNode;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;

/**
 * Unit tests for {@link ObjectGenerator}.
 *
 * @author Carl Harris
 */
public class ObjectGeneratorTest
    extends AbstractViewEventGeneratorTest<ObjectNode> {

  @Mock
  private View.Event childEvent;

  @Mock
  private ViewEventGenerator child;

  @Mock
  private DiscriminatorEventService discriminatorEventService;

  @Mock
  private View.Event discriminatorEvent;

  @Override
  ObjectNode newNode() {
    return context.mock(ObjectNode.class);
  }

  @Override
  AbstractViewEventGenerator<ObjectNode> newGenerator(ObjectNode node) {
    return new ObjectGenerator(node, Collections.singletonList(child),
        discriminatorEventService);
  }

  @Test
  public void testGenerate() throws Exception {
    context.checking(baseExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getObject(MODEL);
        will(returnValue(MODEL));
        oneOf(discriminatorEventService).isDiscriminatorNeeded(node);
        will(returnValue(false));
        oneOf(child).generate(MODEL, viewContext);
        will(returnValue(Collections.singletonList(childEvent)));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(3));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1), is(sameInstance(childEvent)));
    assertThat(events.get(2).getType(), is(equalTo(View.Event.Type.END_OBJECT)));
    assertThat(events.get(2).getName(), is(equalTo(NAME)));
    assertThat(events.get(2).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(2).getValue(), is(nullValue()));
  }

  @Test
  public void testGenerateWhenHasDiscriminator() throws Exception {
    context.checking(baseExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getObject(MODEL);
        will(returnValue(MODEL));
        oneOf(discriminatorEventService).isDiscriminatorNeeded(node);
        will(returnValue(true));
        oneOf(discriminatorEventService).newDiscriminatorEvent(
            with(node), with(any(Class.class)), with(viewContext));
        will(returnValue(discriminatorEvent));
        oneOf(child).generate(MODEL, viewContext);
        will(returnValue(Collections.singletonList(childEvent)));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(4));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1), is(sameInstance(discriminatorEvent)));
    assertThat(events.get(2), is(sameInstance(childEvent)));
    assertThat(events.get(3).getType(), is(equalTo(View.Event.Type.END_OBJECT)));
    assertThat(events.get(3).getName(), is(equalTo(NAME)));
    assertThat(events.get(3).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(3).getValue(), is(nullValue()));
  }

  @Test
  public void testOnEvaluateWhenNull() throws Exception {
    context.checking(baseExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getObject(MODEL);
        will(returnValue(null));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(1));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.VALUE)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
  }

}
