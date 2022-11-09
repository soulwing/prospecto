/*
 * File created on Apr 8, 2016
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
package org.soulwing.prospecto.runtime.listener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.eventDescribing;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.forModel;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.inContext;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.propertyValue;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.json.JsonValue;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.ValueTypeConverterService;

/**
 * Unit tests for {@link ConcreteTransformationService}.
 *
 * @author Carl Harris
 */
public class ConcreteTransformationServiceTest {

  private static final Object VIEW_VALUE = new Object();

  private static final Map<Object, Object> MAP_VALUE =
      Collections.singletonMap("key", VIEW_VALUE);

  private static final List<Object> LIST_VALUE =
      Collections.singletonList(VIEW_VALUE);

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  ViewEntity parentEntity;

  @Mock
  ViewNode node;

  @Mock
  ValueTypeConverterService converters;

  @Mock
  private NotifiableViewListeners listeners;

  @Mock
  MockModelValue convertedValue;

  @Mock
  MockModelValue transformedValue;

  @Test
  public void testSimpleValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getValueTypeConverters();
        will(returnValue(converters));
        oneOf(converters).toModelValue(MockModelValue.class, VIEW_VALUE, node,
            viewContext);
        will(returnValue(convertedValue));
      }
    });
    context.checking(listenerExpectations(convertedValue));
    assertThat(ConcreteTransformationService.INSTANCE.valueToInject(
        parentEntity, MockModelValue.class, VIEW_VALUE, node, viewContext),
        is(sameInstance(transformedValue)));
  }

  @Test
  public void testMapValue() throws Exception {
    context.checking(listenerExpectations(MAP_VALUE));
    assertThat(ConcreteTransformationService.INSTANCE.valueToInject(
            parentEntity, MockModelValue.class, MAP_VALUE, node, viewContext),
        is(sameInstance(transformedValue)));
  }

  @Test
  public void testCollectionValue() throws Exception {
    context.checking(listenerExpectations(MAP_VALUE));
    assertThat(ConcreteTransformationService.INSTANCE.valueToInject(
            parentEntity, MockModelValue.class, MAP_VALUE, node, viewContext),
        is(sameInstance(transformedValue)));
  }

  @Test
  public void testJsonValue() throws Exception {
    context.checking(listenerExpectations(JsonValue.NULL));
    assertThat(ConcreteTransformationService.INSTANCE.valueToInject(
            parentEntity, MockModelValue.class, JsonValue.NULL, node, viewContext),
        is(sameInstance(transformedValue)));
  }

  private Expectations listenerExpectations(Object value) throws Exception {
    return new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).willInjectValue((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node), forModel(parentEntity),
                propertyValue(value), inContext(viewContext))));
        will(returnValue(transformedValue));
        oneOf(listeners).propertyVisited((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node), forModel(parentEntity),
                propertyValue(transformedValue), inContext(viewContext))));
      }
    };
  }

  private interface MockModelValue {}

}
