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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.handler.ViewNodeValueEvent;
import org.soulwing.prospecto.api.handler.ViewNodeValueHandler;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

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
  private ViewNodeValueHandler handler;

  @Mock
  private Accessor accessor;

  @Mock
  private ScopedViewContext viewContext;

  private MockViewNode node = new MockViewNode();

  @Before
  public void setUp() throws Exception {
    node.setAccessor(accessor);
  }

  @Test
  public void testOnEvaluate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getViewNodeValueHandlers();
        will(returnValue(Collections.singletonList(handler)));
        oneOf(handler).onExtractValue(
            with(viewNodeValueEvent(node, MODEL_VALUE, viewContext)));
        will(returnValue(MODEL_VALUE));
      }
    });

    final List<View.Event> events = node.onEvaluate(MODEL, viewContext);
    assertThat(events.size(), is(equalTo(1)));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.VALUE)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(sameInstance(VIEW_VALUE)));
  }

  private static Matcher<ViewNodeValueEvent> viewNodeValueEvent(
      MockViewNode node, Object value, ScopedViewContext viewContext) {
    return allOf(
        hasProperty("source", sameInstance(node)),
        hasProperty("value", sameInstance(value)),
        hasProperty("context", sameInstance(viewContext)));
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
