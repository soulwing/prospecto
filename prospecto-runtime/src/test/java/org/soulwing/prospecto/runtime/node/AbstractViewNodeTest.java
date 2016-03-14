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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.handler.ViewNodeEvent;
import org.soulwing.prospecto.api.handler.ViewNodeHandler;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@link AbstractViewNode}.
 *
 * @author Carl Harris
 */
public class AbstractViewNodeTest {

  private static final String NAME = "name";
  private static final String NAMESPACE = "namespace";
  private static final Class<?> MODEL_TYPE = Object.class;

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private View.Event event;

  @Mock
  private ViewNodeHandler handler;

  private static final Object MODEL = new Object();

  private MockViewNode node = new MockViewNode();

  @Test
  public void testEvaluateWhenAllHandlersAccept() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).push(NAME, MODEL_TYPE);
        oneOf(viewContext).pop();
        oneOf(viewContext).getViewNodeHandlers();
        will(returnValue(Arrays.asList(handler, handler)));
        exactly(2).of(handler).beforeVisit(
            with(viewNodeEvent(node, MODEL, viewContext)));
        will(onConsecutiveCalls(returnValue(true), returnValue(true)));
        exactly(2).of(handler).afterVisit(
            with(viewNodeEvent(node, MODEL, viewContext)));
      }
    });

    assertThat(node.evaluate(MODEL, viewContext), contains(event));
  }

  @Test
  public void testEvaluateWhenAnyHandlerRejects() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).push(NAME, MODEL_TYPE);
        oneOf(viewContext).pop();
        oneOf(viewContext).getViewNodeHandlers();
        will(returnValue(Arrays.asList(handler, handler)));
        exactly(2).of(handler).beforeVisit(
            with(viewNodeEvent(node, MODEL, viewContext)));
        will(onConsecutiveCalls(returnValue(true), returnValue(false)));
      }
    });

    assertThat(node.evaluate(MODEL, viewContext), is(empty()));
  }


  private static Matcher<ViewNodeEvent> viewNodeEvent(MockViewNode node,
      Object model, ScopedViewContext viewContext) {
    return allOf(
        hasProperty("source", sameInstance(node)),
        hasProperty("model", sameInstance(model)),
        hasProperty("context", sameInstance(viewContext)));
  }


  class MockViewNode extends AbstractViewNode {

    MockViewNode() {
      super(NAME, NAMESPACE, MODEL_TYPE);
    }

    @Override
    protected List<View.Event> onEvaluate(Object source,
        ScopedViewContext context) throws Exception {
      assertThat(source, is(sameInstance(source)));
      assertThat(context, is(sameInstance(viewContext)));
      return Collections.singletonList(event);
    }
  }
}
