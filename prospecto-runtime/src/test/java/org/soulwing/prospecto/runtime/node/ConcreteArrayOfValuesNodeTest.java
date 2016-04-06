/*
 * File created on Mar 15, 2016
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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * Unit tests for {@link ConcreteArrayOfValuesNode}.
 *
 * @author Carl Harris
 */
public class ConcreteArrayOfValuesNodeTest {

  private static final String NAME = "name";
  private static final String ELEMENT_NAME = "elementName";
  private static final String NAMESPACE = "namespace";

  private static final Class<?> MODEL_TYPE = Collection.class;

  private static final Object MODEL = new Object();
  private static final Object MODEL_VALUE = new Object();
  private static final Object VIEW_VALUE = new Object();

  private static final View.Event TRIGGER_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, NAME, null, null);

  private static final ConcreteViewEvent END_EVENT =
      new ConcreteViewEvent(View.Event.Type.END_ARRAY, NAME, null, null);

  private static final ConcreteViewEvent VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, null, null, VIEW_VALUE);

  private static final ConcreteViewEvent OTHER_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null, null);

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  Accessor accessor;

  @Mock
  ScopedViewContext viewContext;

  @Mock
  TransformationService transformationService;

  @Mock
  UpdatableViewNodeTemplate template;

  @Mock
  MultiValuedAccessorFactory accessorFactory;

  @Mock
  MultiValuedAccessor multiValuedAccessor;

  @Mock
  ToManyAssociationUpdater associationUpdater;

  @Mock
  ViewEntity parentEntity;

  private Deque<View.Event> events = new LinkedList<>();

  private ConcreteArrayOfValuesNode node;

  @Before
  public void setUp() throws Exception {
    node = new ConcreteArrayOfValuesNode(NAME, ELEMENT_NAME, NAMESPACE,
        MODEL_VALUE.getClass(), transformationService, template,
        accessorFactory, associationUpdater);
    context.checking(new Expectations() {
      {
        allowing(accessor).getDataType();
        will(returnValue(MODEL_TYPE));
        allowing(accessorFactory).newAccessor(accessor,
            MODEL_VALUE.getClass());
        will(returnValue(multiValuedAccessor));
      }
    });

    node.setAccessor(accessor);
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
        node.toModelValue(parentEntity, TRIGGER_EVENT, events, viewContext),
        is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testToModelValueWhenUndefinedValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(template).toModelValue(
            with(node),
            with(parentEntity),
            with(viewContext),
            with(any(UpdatableViewNodeTemplate.Method.class)));
        will(returnValue(UndefinedValue.INSTANCE));
      }
    });

    events.add(END_EVENT);

    assertThat(
        node.toModelValue(parentEntity, TRIGGER_EVENT, events, viewContext),
        is(sameInstance((Object) UndefinedValue.INSTANCE)));

    assertThat(events, is(empty()));
  }

  @Test
  public void testUpdateMethod() throws Exception {
    final UpdatableViewNodeTemplate.Method method = node.new Method(
        parentEntity, TRIGGER_EVENT, events, viewContext);

    context.checking(new Expectations() {
      {
        oneOf(multiValuedAccessor).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity, Object.class,
            VIEW_VALUE, node, viewContext);
        will(returnValue(MODEL_VALUE));
      }
    });

    events.add(VALUE_EVENT);
    events.add(END_EVENT);

    final Object value = method.toModelValue();
    assertThat(value, instanceOf(Iterable.class));
    assertThat((Iterable<?>) value, contains(MODEL_VALUE));
  }

  @Test(expected = ModelEditorException.class)
  public void testUpdateMethodWhenUnexpectedEvent() throws Exception {
    final UpdatableViewNodeTemplate.Method method = node.new Method(
        parentEntity, TRIGGER_EVENT, events, viewContext);

    events.add(OTHER_EVENT);
    method.toModelValue();
  }

  @Test
  public void testInject() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associationUpdater).update(with(node),
            with(MODEL), with(contains(MODEL_VALUE)),
            with(multiValuedAccessor), with(viewContext));
      }
    });

    node.inject(MODEL, Collections.singletonList(MODEL_VALUE), viewContext);
  }

}
