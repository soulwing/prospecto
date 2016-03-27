/*
 * File created on Mar 17, 2016
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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link SubtypeNode}.
 *
 * @author Carl Harris
 */
public class SubtypeNodeTest {

  private static final String NAME = "name";
  private static final String NAMESPACE = "namespace";

  private static final String CHILD_NAME = "childName";
  private static final String CHILD_NAMESPACE = "childNamespace";
  private static final Object CHILD_MODEL = new Object();
  private static final Class<?> CHILD_TYPE = CHILD_MODEL.getClass();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private NotifiableViewListeners listeners;

  @Mock
  private View.Event childEvent;

  @Mock
  private MockSubModel1 model1;

  @Mock
  private MockSubModel2 model2;

  private MockViewNode child = new MockViewNode();

  private SubtypeNode node = new SubtypeNode(MockSubModel1.class);

  @Test
  public void testOnEvaluate() throws Exception {
    context.checking(onEvaluateExpectations());
    node.addChild(child);
    final List<View.Event> events = node.onEvaluate(model1, viewContext);
    assertThat(events.size(), is(equalTo(1)));
    assertThat(events.get(0), is(sameInstance(childEvent)));
  }

  @Test
  public void testOnEvaluateWithDifferentSubtype() throws Exception {
    context.checking(onEvaluateExpectations());
    node.addChild(child);
    final List<View.Event> events = node.onEvaluate(model2, viewContext);
    assertThat(events.isEmpty(), is(true));
  }

  private Expectations onEvaluateExpectations() {
    return new Expectations() {
      {
        allowing(viewContext).put(with(any(MockModel.class)));
        allowing(viewContext).remove(with(any(MockModel.class)));
        allowing(viewContext).getListeners();
        will(returnValue(listeners));
        allowing(listeners).shouldVisitNode(with(any(ViewNodeEvent.class)));
        will(returnValue(true));
        allowing(listeners).nodeVisited(with(any(ViewNodeEvent.class)));
        allowing(viewContext).push(CHILD_NAME, CHILD_TYPE);
        allowing(viewContext).pop();
      }
    };
  }

  class MockViewNode extends AbstractViewNode {

    MockViewNode() {
      super(CHILD_NAME, CHILD_NAMESPACE, CHILD_TYPE);
    }

    @Override
    protected List<View.Event> onEvaluate(Object source,
        ScopedViewContext context) throws Exception {
      assertThat(source, is(instanceOf(MockModel.class)));
      assertThat(context, is(sameInstance(viewContext)));
      return Collections.singletonList(childEvent);
    }
  }

  interface MockModel {
  }

  interface MockSubModel1 extends MockModel {
  }

  interface MockSubModel2 extends MockModel {
  }

}
