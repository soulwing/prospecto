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
package org.soulwing.prospecto.runtime.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.eventDescribing;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.forModel;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.inContext;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.propertyValue;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.node.ArrayOfObjectsNode;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;

/**
 * Unit tests for {@link ArrayOfObjectsGenerator}.
 *
 * @author Carl Harris
 */
public class ArrayOfObjectsGeneratorTest
    extends AbstractViewEventGeneratorTest<ArrayOfObjectsNode> {

  private static final String ELEMENT_NAME = "elementName";

  @Mock
  private View.Event childEvent;

  @Mock
  private ViewEventGenerator child;

  @Mock
  private Iterator<?> iterator;

  @Mock
  private DiscriminatorEventService discriminatorEventService;

  @Mock
  private View.Event discriminatorEvent;

  @Mock
  private ElementModel elementModel;

  @Mock
  private TransformedModel transformedModel;

  @Override
  ArrayOfObjectsNode newNode() {
    return context.mock(ArrayOfObjectsNode.class);
  }

  @Override
  AbstractViewEventGenerator<ArrayOfObjectsNode> newGenerator(ArrayOfObjectsNode node) {
    return new ArrayOfObjectsGenerator(node,
        Collections.singletonList(child), discriminatorEventService);
  }

  @Test
  public void testGenerate() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).iterator(MODEL);
        will(returnValue(iterator));
        exactly(2).of(iterator).hasNext();
        will(onConsecutiveCalls(returnValue(true), returnValue(false)));
        oneOf(iterator).next();
        will(returnValue(elementModel));
        oneOf(discriminatorEventService).isDiscriminatorNeeded(node);
        will(returnValue(false));
        oneOf(listeners).didExtractValue((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node),
                forModel(MODEL), propertyValue(elementModel),
                inContext(viewContext))));
        will(returnValue(transformedModel));
        oneOf(child).generate(transformedModel, viewContext);
        will(returnValue(Collections.singletonList(childEvent)));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(equalTo(5)));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_ARRAY)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1).getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.get(1).getName(), is(equalTo(ELEMENT_NAME)));
    assertThat(events.get(1).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(1).getValue(), is(nullValue()));
    assertThat(events.get(2), is(sameInstance(childEvent)));
    assertThat(events.get(3).getType(), is(equalTo(View.Event.Type.END_OBJECT)));
    assertThat(events.get(3).getName(), is(equalTo(ELEMENT_NAME)));
    assertThat(events.get(3).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(3).getValue(), is(nullValue()));
    assertThat(events.get(4).getType(), is(equalTo(View.Event.Type.END_ARRAY)));
    assertThat(events.get(4).getName(), is(equalTo(NAME)));
    assertThat(events.get(4).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(4).getValue(), is(nullValue()));
  }

  @Test
  public void testGenerateWhenDiscriminatorNeeded() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).iterator(MODEL);
        will(returnValue(iterator));
        exactly(2).of(iterator).hasNext();
        will(onConsecutiveCalls(returnValue(true), returnValue(false)));
        oneOf(iterator).next();
        will(returnValue(elementModel));

        oneOf(listeners).didExtractValue((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node),
                forModel(MODEL), propertyValue(elementModel),
                inContext(viewContext))));
        will(returnValue(transformedModel));

        oneOf(discriminatorEventService).isDiscriminatorNeeded(node);
        will(returnValue(true));
        oneOf(discriminatorEventService).newDiscriminatorEvent(
            with(node), with(isAssignableFrom(TransformedModel.class)),
            with(viewContext));
        will(returnValue(discriminatorEvent));

        oneOf(child).generate(transformedModel, viewContext);
        will(returnValue(Collections.singletonList(childEvent)));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(equalTo(6)));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_ARRAY)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1).getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.get(1).getName(), is(equalTo(ELEMENT_NAME)));
    assertThat(events.get(1).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(1).getValue(), is(nullValue()));
    assertThat(events.get(2), is(sameInstance(discriminatorEvent)));
    assertThat(events.get(3), is(sameInstance(childEvent)));
    assertThat(events.get(4).getType(), is(equalTo(View.Event.Type.END_OBJECT)));
    assertThat(events.get(4).getName(), is(equalTo(ELEMENT_NAME)));
    assertThat(events.get(4).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(4).getValue(), is(nullValue()));
    assertThat(events.get(5).getType(), is(equalTo(View.Event.Type.END_ARRAY)));
    assertThat(events.get(5).getName(), is(equalTo(NAME)));
    assertThat(events.get(5).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(5).getValue(), is(nullValue()));
  }

  @Test
  public void testGenerateWhenUndefinedElementValue() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).iterator(MODEL);
        will(returnValue(iterator));
        exactly(2).of(iterator).hasNext();
        will(onConsecutiveCalls(returnValue(true), returnValue(false)));
        oneOf(iterator).next();
        will(returnValue(elementModel));
        oneOf(listeners).didExtractValue((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node),
                forModel(MODEL), propertyValue(elementModel),
                inContext(viewContext))));
        will(returnValue(UndefinedValue.INSTANCE));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(equalTo(2)));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_ARRAY)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1).getType(), is(equalTo(View.Event.Type.END_ARRAY)));
    assertThat(events.get(1).getName(), is(equalTo(NAME)));
    assertThat(events.get(1).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(1).getValue(), is(nullValue()));
  }



  @Test
  public void testOnEvaluateWhenNull() throws Exception {
    context.checking(baseExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).iterator(MODEL);
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

  private Expectations contextExpectations() throws Exception {
    return new Expectations() {
      {
        allowing(node).getElementName();
        will(returnValue(ELEMENT_NAME));
        oneOf(viewContext).push(with(ELEMENT_NAME),
            with(isAssignableFrom(ElementModel.class)));
        oneOf(viewContext).put(elementModel);
        oneOf(viewContext).pop();
      }
    };
  }

  private static Matcher<Class<?>> isAssignableFrom(final Class<?> expected) {
    return new BaseMatcher<Class<?>>() {
      @Override
      public boolean matches(Object item) {
        return expected.isAssignableFrom((Class) item);
      }

      @Override
      public void describeTo(Description description) {

      }
    };
  }

  private interface BaseModel {}

  private interface ElementModel extends BaseModel {}

  private interface TransformedModel extends BaseModel {}


}
