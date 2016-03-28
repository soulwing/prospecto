/*
 * File created on Mar 11, 2016
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
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.viewNodePropertyEvent;

import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link ValueViewNode}.
 *
 * @author Carl Harris
 */
public class ValueViewNodeTest {

  private static final String NAME = "name";
  private static final String NAMESPACE = "namespace";

  private static final Object MODEL = new Object();
  private static final Object MODEL_VALUE = new Object();
  private static final Object VIEW_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private NotifiableViewListeners listeners;

  @Mock
  private ScopedViewContext viewContext;

  private MockViewNode node = new MockViewNode();

  @Test
  public void testOnEvaluate() throws Exception {
    context.checking(new Expectations() {
      {
        exactly(2).of(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).didExtractValue(
            with(viewNodePropertyEvent(node, MODEL, MODEL_VALUE, viewContext)));
        will(returnValue(MODEL_VALUE));
        oneOf(listeners).propertyVisited(
            with(viewNodePropertyEvent(node, MODEL, VIEW_VALUE, viewContext)));
      }
    });

    final List<View.Event> events = node.onEvaluate(MODEL, viewContext);
    assertThat(events.size(), is(equalTo(1)));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.VALUE)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(sameInstance(VIEW_VALUE)));
  }

  class MockViewNode extends ValueViewNode {

    MockViewNode() {
      super(NAME, NAMESPACE);
    }

    @Override
    protected Object getModelValue(Object source,
        ScopedViewContext context) throws Exception {
      assertThat(source, is(sameInstance(MODEL)));
      assertThat(context, is(sameInstance(viewContext)));
      return MODEL_VALUE;
    }

    @Override
    protected Object toViewValue(Object model, ScopedViewContext context)
        throws Exception {
      assertThat(model, is(sameInstance(MODEL_VALUE)));
      assertThat(context, is(sameInstance(viewContext)));
      return VIEW_VALUE;
    }

  }

}
