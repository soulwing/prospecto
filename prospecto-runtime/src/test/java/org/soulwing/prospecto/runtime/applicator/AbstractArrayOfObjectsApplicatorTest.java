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

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.ArrayOfObjectsNode;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.entity.ViewEntityFactory;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for tests of {@link AbstractArrayOfObjectsApplicator}
 * subytpes.
 *
 * @author Carl Harris
 */
public abstract class AbstractArrayOfObjectsApplicatorTest
    <N extends ArrayOfObjectsNode> extends AbstractViewEventApplicatorTest<N> {

  private static final Object VIEW_VALUE = "viewValue";
  private static final Object MODEL_VALUE = "modelValue";
  private static final List<?> MODEL_ARRAY = Collections.singletonList(MODEL_VALUE);

  private static final View.Event TRIGGER_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, null, null, null);

  private static final View.Event BEGIN_OBJECT_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null, null);

  private static final View.Event END_OBJECT_EVENT =
      new ConcreteViewEvent(View.Event.Type.END_OBJECT, null, null, null);

  private static final View.Event VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, VIEW_VALUE);

  private static final View.Event NULL_VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, null, null, null);

  private static final View.Event END_EVENT =
      new ConcreteViewEvent(View.Event.Type.END_ARRAY, null, null, null);

  @Mock
  ViewEntityFactory entityFactory;

  @Mock
  ToManyAssociationUpdater associationUpdater;

  @Mock
  TransformationService transformationService;

  @Mock
  InjectableViewEntity entity;

  @Mock
  ViewEventApplicator child;

  @Mock
  UpdatableNode childNode;

  @Mock
  ToManyAssociationManager<?, ?> defaultManager;

  @Mock
  ContainerApplicatorLocator applicatorLocator;

  Deque<View.Event> events = new LinkedList<>();

  @Test
  public void testOnToModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(entityFactory).newEntity(node, events, viewContext);
        will(returnValue(entity));
        oneOf(entity).getType();
        will(returnValue(MockModel.class));

        oneOf(applicatorLocator).findApplicator(NAME, MockModel.class,
            (ContainerApplicator) applicator);
        will(returnValue(child));

        allowing(child).getNode();
        will(returnValue(childNode));

        oneOf(child).toModelValue(entity, VALUE_EVENT, events, viewContext);
        will(returnValue(MODEL_VALUE));
        oneOf(viewContext).push(0);
        oneOf(viewContext).pop();
        oneOf(entity).put(NAME, MODEL_VALUE, child);
      }
    });

    events.add(BEGIN_OBJECT_EVENT);
    events.add(VALUE_EVENT);
    events.add(END_OBJECT_EVENT);
    events.add(END_EVENT);
    assertThat((Collection<?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext),
        contains((Object) entity));
  }

  @Test
  public void testOnToModelValueWithNullValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).push(0);
        oneOf(viewContext).pop();
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity,
            Object.class, null, node, viewContext);
        will(returnValue(entity));
      }
    });

    events.add(NULL_VALUE_EVENT);
    events.add(END_EVENT);
    assertThat((Collection<?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext), contains((Object) entity));
  }

  @Test
  public void testOnToModelValueWhenNullTransformedToUndefinedValue()
      throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).push(0);
        oneOf(viewContext).pop();
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity,
            Object.class, null, node, viewContext);
        will(returnValue(UndefinedValue.INSTANCE));
      }
    });

    events.add(NULL_VALUE_EVENT);
    events.add(END_EVENT);
    assertThat((Collection<?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext), is(empty()));
  }


  @Test(expected = ViewApplicatorException.class)
  public void testOnToModelValueWhenMissingEndEvent() throws Exception {
    context.checking(new Expectations() { {} });
    applicator.onToModelValue(parentEntity, TRIGGER_EVENT, events, viewContext);
  }

  @Test(expected = ViewApplicatorException.class)
  public void testOnToModelValueWhenNonObjectEvent() throws Exception {
    context.checking(new Expectations() { {} });
    events.add(VALUE_EVENT);
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
        oneOf(node).getAllowedModes();
        will(returnValue(EnumSet.of(AccessMode.WRITE)));
        oneOf(node).getDefaultManager();
        will(returnValue(defaultManager));
        oneOf(associationUpdater).findManagerAndUpdate(node, MODEL, MODEL_ARRAY,
            defaultManager, viewContext);
      }
    });

    applicator.inject(MODEL, MODEL_ARRAY, viewContext);
  }

  @Test
  public void testInjectInContextWhenNotWritable() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getAllowedModes();
        will(returnValue(EnumSet.noneOf(AccessMode.class)));
      }
    });

    applicator.inject(MODEL, MODEL_ARRAY, viewContext);
  }

  private interface MockModel {}

}
