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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.handler.ViewNodeEvent;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.handler.NotifiableViewListeners;

/**
 * Unit tests for {@link ObjectNode}.
 *
 * @author Carl Harris
 */
public class ObjectNodeTest {

  private static final String NAME = "name";
  private static final String NAMESPACE = "namespace";

  private static final String CHILD_NAME = "childName";
  private static final String CHILD_NAMESPACE = "childNamespace";

  private static final Class<Object> MODEL_TYPE = Object.class;

  private static final Object MODEL = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private Accessor accessor;

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private NotifiableViewListeners listeners;

  @Mock
  private View.Event childEvent;

  private MockViewNode child = new MockViewNode();

  private ObjectNode node = new ObjectNode(NAME, NAMESPACE, MODEL_TYPE);

  @Before
  public void setUp() throws Exception {
    node.setAccessor(accessor);
  }

  @Test
  public void testOnEvaluate() throws Exception {
    context.checking(onEvaluateExpectations());
    context.checking(new Expectations() {
      {
        oneOf(accessor).get(MODEL);
        will(returnValue(MODEL));

        allowing(viewContext).put(MODEL);
        allowing(viewContext).remove(MODEL);
        allowing(viewContext).getListeners();
        will(returnValue(listeners));
        allowing(listeners).fireShouldVisitNode(with(any(ViewNodeEvent.class)));
        will(returnValue(true));
        allowing(listeners).fireNodeVisited(with(any(ViewNodeEvent.class)));
        allowing(viewContext).push(CHILD_NAME, MODEL_TYPE);
        allowing(viewContext).pop();
      }
    });

    node.addChild(child);
    final List<View.Event> events = node.onEvaluate(MODEL, viewContext);
    assertThat(events.size(), is(3));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1), is(sameInstance(childEvent)));
    assertThat(events.get(2).getType(), is(equalTo(View.Event.Type.END_OBJECT)));
    assertThat(events.get(2).getName(), is(equalTo(NAME)));
    assertThat(events.get(2).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(2).getValue(), is(nullValue()));
  }

  @Test
  public void testOnEvaluateWhenNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(accessor).get(MODEL);
        will(returnValue(null));
      }
    });

    node.addChild(child);
    final List<View.Event> events = node.onEvaluate(MODEL, viewContext);
    assertThat(events.size(), is(1));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.VALUE)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
  }

  private Expectations onEvaluateExpectations() throws Exception {
    return new Expectations() {
      {
      }
    };
  }

  class MockViewNode extends AbstractViewNode {

    MockViewNode() {
      super(CHILD_NAME, CHILD_NAMESPACE, MODEL_TYPE);
    }

    @Override
    protected List<View.Event> onEvaluate(Object source,
        ScopedViewContext context) throws Exception {
      assertThat(source, is(sameInstance(MODEL)));
      assertThat(context, is(sameInstance(viewContext)));
      return Collections.singletonList(childEvent);
    }
  }

}
