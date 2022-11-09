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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.MapOfValuesNode;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * Unit tests for {@link MapOfValuesApplicator}.
 *
 * @author Carl Harris
 */
public class MapOfValuesApplicatorTest
    extends AbstractViewEventApplicatorTest<MapOfValuesNode> {

  private static final Object VIEW_VALUE = new Object();
  private static final Object MODEL_VALUE = new Object();
  private static final Map<?, ?> MODEL_MAP = Collections.singletonMap(NAME, MODEL_VALUE);
  private static final String MAP_KEY = "key";

  private static final View.Event TRIGGER_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null, null);

  private static final View.Event VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, VIEW_VALUE);

  private static final List<View.Event> OBJECT = Arrays.asList(
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, MAP_KEY, null, null),
      VALUE_EVENT,
      new ConcreteViewEvent(View.Event.Type.END_OBJECT, MAP_KEY, null, null)
  );

  private static final List<View.Event> ARRAY = Arrays.asList(
      new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, MAP_KEY, null, null),
      VALUE_EVENT,
      new ConcreteViewEvent(View.Event.Type.END_ARRAY, MAP_KEY, null, null)
  );

  private static final View.Event NULL_VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, null);

  private static final View.Event END_EVENT =
      new ConcreteViewEvent(View.Event.Type.END_OBJECT, null, null, null);

  @Mock
  private TransformationService transformationService;

  @Mock
  private ToManyAssociationManager<?, ?> defaultManager;

  @Mock
  private ToManyAssociationUpdater associationUpdater;

  private Deque<View.Event> events = new LinkedList<>();

  @Override
  MapOfValuesNode newNode() {
    return context.mock(MapOfValuesNode.class);
  }

  @Override
  AbstractViewEventApplicator<MapOfValuesNode> newApplicator(
      MapOfValuesNode node) {
    return new MapOfValuesApplicator(node, transformationService,
        associationUpdater);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOnToModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity,
            Object.class, Collections.singletonMap(NAME, VIEW_VALUE), node, viewContext);
        will(returnValue(Collections.singletonMap(NAME, MODEL_VALUE)));
      }
    });

    events.add(VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(((Map<?, ?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext)).entrySet(),
        contains(new AbstractMap.SimpleEntry<>(NAME, MODEL_VALUE)));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOnToModelValueWithNullValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity,
            Object.class, Collections.singletonMap(NAME, null), node, viewContext);
        will(returnValue(Collections.singletonMap(NAME, null)));
      }
    });

    events.add(NULL_VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(((Map<?, ?>) applicator.onToModelValue(parentEntity,
            TRIGGER_EVENT, events, viewContext)).entrySet(),
        contains(new AbstractMap.SimpleEntry<>(NAME, null)));
  }

  @Test(expected = ViewApplicatorException.class)
  public void testOnToModelValueWhenMissingEndEvent() throws Exception {
    context.checking(new Expectations() { {} });
    applicator.onToModelValue(parentEntity, TRIGGER_EVENT, events, viewContext);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOnToModelValueObject() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(with(parentEntity),
            with(Object.class),
            with(hasEntry(equalTo(MAP_KEY), hasEntry(equalTo(NAME), sameInstance(VIEW_VALUE)))),
            with(node), with(viewContext));
        will(returnValue(Collections.singletonMap(MAP_KEY, MODEL_VALUE)));
      }
    });
    events.addAll(OBJECT);
    events.add(END_EVENT);
    assertThat(((Map<?, ?>) applicator.onToModelValue(parentEntity,
            TRIGGER_EVENT, events, viewContext)).entrySet(),
        contains(new AbstractMap.SimpleEntry<>(MAP_KEY, MODEL_VALUE)));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOnToModelValueArray() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(with(parentEntity),
            with(Object.class),
            with(hasKey(equalTo(MAP_KEY))),
            with(node), with(viewContext));
        will(returnValue(Collections.singletonMap(MAP_KEY, MODEL_VALUE)));
      }
    });
    events.addAll(ARRAY);
    events.add(END_EVENT);
    assertThat(((Map<?, ?>) applicator.onToModelValue(parentEntity,
            TRIGGER_EVENT, events, viewContext)).entrySet(),
        contains(new AbstractMap.SimpleEntry<>(MAP_KEY, MODEL_VALUE)));
  }

  @Test
  public void testOnToModelValueWhenEmptyMap() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getComponentType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(with(parentEntity),
            with(Object.class),
            with(Collections.emptyMap()),
            with(node), with(viewContext));
        will(returnValue(Collections.emptyMap()));
      }
    });
    events.add(END_EVENT);
    assertThat(((Map<?, ?>) applicator.onToModelValue(parentEntity,
        TRIGGER_EVENT, events, viewContext)).entrySet(), is(empty()));
  }

  @Test
  public void testInjectInContext() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getDefaultManager();
        will(returnValue(defaultManager));
        oneOf(associationUpdater).findManagerAndUpdate(node, MODEL, MODEL_MAP.entrySet(),
            defaultManager, viewContext);
      }
    });

    applicator.inject(MODEL, MODEL_MAP, viewContext);
  }

}
