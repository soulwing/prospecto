/*
 * File created on Apr 7, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.ArrayOfValuesNode;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * Unit tests for {@link ArrayOfValuesApplicator}.
 *
 * @author Carl Harris
 */
public class ArrayOfValuesApplicatorTest
    extends AbstractViewEventApplicatorTest<ArrayOfValuesNode> {

  private static final Object VIEW_VALUE = new Object();
  private static final Object MODEL_VALUE = new Object();
  private static final List<?> MODEL_ARRAY = Collections.singletonList(MODEL_VALUE);

  private static final View.Event TRIGGER_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, null, null, null);

  private static final View.Event OTHER_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null, null);

  private static final View.Event VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, VIEW_VALUE);

  private static final View.Event NULL_VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, null);

  private static final View.Event END_EVENT =
      new ConcreteViewEvent(View.Event.Type.END_ARRAY, null, null, null);

  @Mock
  private TransformationService transformationService;

  @Mock
  private ToManyAssociationManager<?, ?> defaultManager;

  @Mock
  private ToManyAssociationUpdater associationUpdater;

  private Deque<View.Event> events = new LinkedList<>();

  @Override
  ArrayOfValuesNode newNode() {
    return context.mock(ArrayOfValuesNode.class);
  }

  @Override
  AbstractViewEventApplicator<ArrayOfValuesNode> newApplicator(
      ArrayOfValuesNode node) {
    return new ArrayOfValuesApplicator(node, transformationService,
        associationUpdater);
  }

  @Test
  public void testOnToModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity,
            Object.class, VIEW_VALUE, node, viewContext);
        will(returnValue(MODEL_VALUE));
        oneOf(viewContext).push(0);
        oneOf(viewContext).pop();
      }
    });

    events.add(VALUE_EVENT);
    events.add(END_EVENT);
    assertThat((Collection<?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext), contains(MODEL_VALUE));
  }

  @Test
  public void testOnToModelValueWithNullValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity,
            Object.class, null, node, viewContext);
        will(returnValue(null));
        oneOf(viewContext).push(0);
        oneOf(viewContext).pop();
      }
    });

    events.add(NULL_VALUE_EVENT);
    events.add(END_EVENT);
    assertThat((Collection<?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext), contains(nullValue()));
  }

  @Test(expected = ViewApplicatorException.class)
  public void testOnToModelValueWhenMissingEndEvent() throws Exception {
    context.checking(new Expectations() { {} });
    applicator.onToModelValue(parentEntity, TRIGGER_EVENT, events, viewContext);
  }

  @Test(expected = ViewApplicatorException.class)
  public void testOnToModelValueWhenNonValueEvent() throws Exception {
    context.checking(new Expectations() { {} });
    events.add(OTHER_EVENT);
    applicator.onToModelValue(parentEntity, TRIGGER_EVENT, events, viewContext);
  }

  @Test
  public void testOnToModelValueWhenEmptyArray() throws Exception {
    context.checking(new Expectations() { {} });
    events.add(END_EVENT);
    assertThat((Collection<?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext), is(empty()));
  }

  @Test
  public void testInjectInContext() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getDefaultManager();
        will(returnValue(defaultManager));
        oneOf(associationUpdater).update(node, MODEL, MODEL_ARRAY,
            defaultManager, viewContext);
      }
    });

    applicator.inject(MODEL, MODEL_ARRAY, viewContext);
  }

}
