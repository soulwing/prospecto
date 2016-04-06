/*
 * File created on Apr 1, 2016
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.eventDescribing;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.forModel;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.inContext;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.propertyValue;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.ValueTypeConverterService;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link ConcreteTransformationService}.
 *
 * @author Carl Harris
 */
public class ConcreteTransformationServiceTest {

  private final Object VIEW_VALUE = new Object();

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
  public void test() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getValueTypeConverters();
        will(returnValue(converters));
        oneOf(converters).toModelValue(MockModelValue.class, VIEW_VALUE, node);
        will(returnValue(convertedValue));
        allowing(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).willInjectValue((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node), forModel(parentEntity),
                propertyValue(convertedValue), inContext(viewContext))));
        will(returnValue(transformedValue));
        oneOf(listeners).propertyVisited((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node), forModel(parentEntity),
                propertyValue(transformedValue), inContext(viewContext))));
      }
    });

    assertThat(ConcreteTransformationService.INSTANCE.valueToInject(
        parentEntity, MockModelValue.class, VIEW_VALUE, node, viewContext),
        is(sameInstance((Object) transformedValue)));
  }

  private interface MockModelValue {}

}
