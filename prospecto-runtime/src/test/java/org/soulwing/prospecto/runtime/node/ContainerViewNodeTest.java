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
import static org.hamcrest.Matchers.contains;
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
import org.soulwing.prospecto.api.handler.ViewNodeHandler;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@link ContainerViewNode}.
 *
 * @author Carl Harris
 */
public class ContainerViewNodeTest {

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

  private MockViewNode child = new MockViewNode();


  @Test
  public void testEvaluateChildren() throws Exception {
    context.checking(new Expectations() {
      {
        allowing(viewContext).getViewNodeHandlers();
        will(returnValue(Collections.emptyList()));
        allowing(viewContext).push(NAME, MODEL_TYPE);
        allowing(viewContext).pop();
        oneOf(viewContext).put(MODEL);
        oneOf(viewContext).remove(MODEL);
      }
    });

    node.addChild(child);
    assertThat(node.evaluateChildren(MODEL, viewContext), contains(event));
  }


  class MockViewNode extends ContainerViewNode {

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
