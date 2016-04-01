/*
 * File created on Mar 14, 2016
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
package org.soulwing.prospecto.runtime.node;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.event.ViewEventMatchers.eventOfType;
import static org.soulwing.prospecto.runtime.event.ViewEventMatchers.inNamespace;
import static org.soulwing.prospecto.runtime.event.ViewEventMatchers.whereValue;
import static org.soulwing.prospecto.runtime.event.ViewEventMatchers.withName;

import java.util.Deque;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.ValueTypeConverterService;
import org.soulwing.prospecto.runtime.testing.JUnitRuleClassImposterizingMockery;

/**
 * Unit tests for {@link ValueNode}.
 *
 * @author Carl Harris
 */
public class ValueNodeTest {

  private static final String NAME = "name";
  private static final String NAMESPACE = "namespace";
  private static final Object MODEL = new Object();
  private static final Object MODEL_VALUE = new Object();
  private static final Object TRANSFORMED_VALUE = new Object();
  private static final Object VIEW_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context =
      new JUnitRuleClassImposterizingMockery();

  @Mock
  AbstractViewNode parent;

  @Mock
  TransformationService transformationService;

  @Mock
  UpdatableViewNodeTemplate template;

  @Mock
  Accessor accessor;

  @Mock
  ValueTypeConverterService converters;

  @Mock
  ScopedViewContext viewContext;

  @Mock
  ViewEntity parentEntity;

  @Mock
  View.Event triggerEvent;

  @Mock
  Deque<View.Event> events;

  ValueNode node;

  @Before
  public void setUp() throws Exception {
    node = new ValueNode(NAME, NAMESPACE, transformationService, template);
    node.setAccessor(accessor);
    node.setParent(parent);
  }

  @Test
  public void testOnEvaluate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(accessor).get(MODEL);
        will(returnValue(MODEL_VALUE));
        oneOf(transformationService).valueToExtract(MODEL, MODEL_VALUE,
            node, viewContext);
        will(returnValue(TRANSFORMED_VALUE));
      }
    });

    assertThat(node.onEvaluate(MODEL, viewContext),
        contains(
            eventOfType(View.Event.Type.VALUE,
                withName(NAME),
                inNamespace(NAMESPACE),
                whereValue(is(sameInstance(TRANSFORMED_VALUE))))));
  }

  @Test
  public void testOnEvaluateWhenUndefinedValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(accessor).get(MODEL);
        will(returnValue(MODEL_VALUE));
        oneOf(transformationService).valueToExtract(MODEL, MODEL_VALUE,
            node, viewContext);
        will(returnValue(UndefinedValue.INSTANCE));
      }
    });

    assertThat(node.onEvaluate(MODEL, viewContext), is(empty()));
  }

  @Test
  public void testToModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(template).toModelValue(
            with(node),
            with(parentEntity),
            with(viewContext),
            with(any(UpdatableViewNodeTemplate.Method.class)));
        will(returnValue(MODEL_VALUE));
      }
    });

    assertThat(
        node.toModelValue(parentEntity, triggerEvent, events, viewContext),
        is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testUpdateMethod() throws Exception {
    final UpdatableViewNodeTemplate.Method method = node.new Method(
        parentEntity, triggerEvent, viewContext);

    context.checking(new Expectations() {
      {
        oneOf(triggerEvent).getValue();
        will(returnValue(VIEW_VALUE));
        oneOf(accessor).getDataType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity, Object.class,
            VIEW_VALUE, node, viewContext);
        will(returnValue(MODEL_VALUE));
      }
    });

    assertThat(method.toModelValue(), is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testInject() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(accessor).forSubtype(MODEL.getClass());
        will(returnValue(accessor));
        oneOf(accessor).set(MODEL, MODEL_VALUE);
      }
    });

    node.inject(MODEL, MODEL_VALUE, viewContext);
  }

}


