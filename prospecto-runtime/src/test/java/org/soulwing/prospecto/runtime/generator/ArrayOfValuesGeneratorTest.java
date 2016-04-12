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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.ArrayOfValuesNode;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * Unit tests for {@link ArrayOfValuesGenerator}.
 *
 * @author Carl Harris
 */
public class ArrayOfValuesGeneratorTest
    extends AbstractViewEventGeneratorTest<ArrayOfValuesNode> {

  private static final String ELEMENT_NAME = "elementName";
  private static final Object MODEL_VALUE = new Object();
  private static final Object TRANSFORMED_VALUE = new Object();

  @Mock
  private TransformationService transformationService;

  @Mock
  private Iterator<?> iterator;

  @Override
  ArrayOfValuesNode newNode() {
    return context.mock(ArrayOfValuesNode.class);
  }

  @Override
  AbstractViewEventGenerator<ArrayOfValuesNode> newGenerator(ArrayOfValuesNode node) {
    return new ArrayOfValuesGenerator(node, transformationService);
  }

  @Test
  public void testGenerate() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(iteratorExpectations());
    context.checking(contextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getAllowedModes();
        will(returnValue(EnumSet.of(AccessMode.READ)));
        oneOf(transformationService).valueToExtract(MODEL, MODEL_VALUE, node,
            viewContext);
        will(returnValue(TRANSFORMED_VALUE));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(equalTo(3)));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_ARRAY)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1).getType(), is(equalTo(View.Event.Type.VALUE)));
    assertThat(events.get(1).getName(), is(equalTo(ELEMENT_NAME)));
    assertThat(events.get(1).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(1).getValue(), is(TRANSFORMED_VALUE));
    assertThat(events.get(2).getType(), is(equalTo(View.Event.Type.END_ARRAY)));
    assertThat(events.get(2).getName(), is(equalTo(NAME)));
    assertThat(events.get(2).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(2).getValue(), is(nullValue()));
  }

  @Test
  public void testGenerateWhenUndefinedValue() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(contextExpectations());
    context.checking(iteratorExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getAllowedModes();
        will(returnValue(EnumSet.of(AccessMode.READ)));
        oneOf(transformationService).valueToExtract(MODEL, MODEL_VALUE,
            node, viewContext);
        will(returnValue(UndefinedValue.INSTANCE));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    validateEmptyArray(events);
  }

  @Test
  public void testGenerateWhenNotReadable() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getAllowedModes();
        will(returnValue(EnumSet.noneOf(AccessMode.class)));
      }
    });

    assertThat(generator.generate(MODEL, viewContext), is(empty()));
  }

  private Expectations contextExpectations() throws Exception {
    return new Expectations() {
      {
        allowing(node).getElementName();
        will(returnValue(ELEMENT_NAME));
        oneOf(viewContext).push(0);
        oneOf(viewContext).pop();
      }
    };
  }

  private Expectations iteratorExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(node).iterator(MODEL);
        will(returnValue(iterator));
        exactly(2).of(iterator).hasNext();
        will(onConsecutiveCalls(returnValue(true), returnValue(false)));
        oneOf(iterator).next();
        will(returnValue(MODEL_VALUE));
      }
    };
  }

  private void validateEmptyArray(List<View.Event> events) {
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

}
