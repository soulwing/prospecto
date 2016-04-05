/*
 * File created on Apr 2, 2016
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.eventDescribing;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.forModel;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.inContext;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.mode;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link ConcreteUpdatableViewNodeTemplate}.
 *
 * @author Carl Harris
 */
public class ConcreteUpdatableViewNodeTemplateTest {

  private static final String NAME = "name";
  private static final Object MODEL_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  NotifiableViewListeners listeners;

  @Mock
  ViewNode node;

  @Mock
  ViewEntity parentEntity;

  @Mock
  UpdatableViewNodeTemplate.Method method;

  @Test
  public void testToModelValue() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));

        oneOf(listeners).shouldVisitNode(with(
            eventDescribing(mode(ViewNodeEvent.Mode.MODEL_UPDATE),
                sourceNode(node), forModel(parentEntity),
                inContext(viewContext))));
        will(returnValue(true));

        oneOf(method).toModelValue();
        will(returnValue(MODEL_VALUE));

        oneOf(listeners).nodeVisited(with(
            eventDescribing(mode(ViewNodeEvent.Mode.MODEL_UPDATE),
                sourceNode(node), forModel(parentEntity),
                inContext(viewContext))));
      }
    });

    assertThat(ConcreteUpdatableViewNodeTemplate.INSTANCE.toModelValue(node,
        parentEntity, viewContext, method), is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testToModelValueWhenWillNotVisitNode() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));

        oneOf(listeners).shouldVisitNode(with(
            eventDescribing(mode(ViewNodeEvent.Mode.MODEL_UPDATE),
                sourceNode(node), forModel(parentEntity),
                inContext(viewContext))));
        will(returnValue(false));
      }
    });

    assertThat(ConcreteUpdatableViewNodeTemplate.INSTANCE.toModelValue(node,
        parentEntity, viewContext, method),
         is(equalTo((Object) UndefinedValue.INSTANCE)));
  }

  @Test
  public void testToModelValueWhenUndefinedValue() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));

        oneOf(listeners).shouldVisitNode(with(
            eventDescribing(mode(ViewNodeEvent.Mode.MODEL_UPDATE),
                sourceNode(node), forModel(parentEntity),
                inContext(viewContext))));
        will(returnValue(true));

        oneOf(method).toModelValue();
        will(returnValue(UndefinedValue.INSTANCE));

        oneOf(listeners).nodeVisited(with(
            eventDescribing(mode(ViewNodeEvent.Mode.MODEL_UPDATE),
                sourceNode(node), forModel(parentEntity),
                inContext(viewContext))));
      }
    });

    assertThat(ConcreteUpdatableViewNodeTemplate.INSTANCE.toModelValue(node,
        parentEntity, viewContext, method),
        is(equalTo((Object) UndefinedValue.INSTANCE)));
  }

  private Expectations viewContextExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(node).getName();
        will(returnValue(NAME));
        oneOf(parentEntity).getType();
        will(returnValue(MockModel.class));
        oneOf(viewContext).push(NAME, MockModel.class);
        oneOf(viewContext).pop();
      }
    };
  }

  interface MockModel {}

}
