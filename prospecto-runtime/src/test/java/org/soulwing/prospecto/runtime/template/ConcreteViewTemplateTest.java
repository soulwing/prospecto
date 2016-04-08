/*
 * File created on Apr 5, 2016
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
package org.soulwing.prospecto.runtime.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.context.ScopedViewContextFactory;
import org.soulwing.prospecto.runtime.generator.ViewEventGenerator;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link ConcreteViewTemplate}.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateTest {

  private static final Object MODEL = new Object();

  private static final String NAME = "name";
  private static final String ELEMENT_NAME = "elementName";
  private static final String NAMESPACE = "namespace";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContextFactory viewContextFactory;

  @Mock
  private NotifiableViewListeners listeners;

  @Mock
  private ViewContext viewContext;

  @Mock
  private ScopedViewContext scopedViewContext;

  @Mock
  private View.Event event;

  @Mock
  private ViewEventGenerator generator;

  private MockViewNode root = new MockViewNode();

  private ConcreteViewTemplate template;

  @Before
  public void setUp() throws Exception {
    template = new ConcreteViewTemplate(root, viewContextFactory);
  }

  @Test
  public void testGenerateView() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(generator).generate(MODEL, scopedViewContext);
        will(returnValue(Collections.singletonList(event)));
      }
    });

    View view = template.generateView(MODEL, viewContext);
    Iterator<View.Event> events = view.iterator();
    assertThat(events.hasNext(), is(true));
    assertThat(events.next(), is(sameInstance(event)));
  }

  @Test(expected = ViewException.class)
  public void testGenerateViewWhenEvaluateThrowsException() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(generator).generate(MODEL, scopedViewContext);
        will(throwException(new Exception()));
      }
    });

    template.generateView(MODEL, viewContext);
  }

  @Test
  public void testObjectSubView() throws Exception {
    AbstractViewNode child = new MockViewNode();
    root.addChild(child);
    AbstractViewNode node = template.object(NAME, NAMESPACE);
    assertThat(node, is(instanceOf(ConcreteObjectNode.class)));
    assertThat(node.getName(), is(equalTo(NAME)));
    assertThat(node.getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(((ConcreteContainerNode) node).getChildren(),
    contains((ViewNode) child));
  }

  @Test
  public void testArrayOfObjectsSubView() throws Exception {
    AbstractViewNode child = new MockViewNode();
    root.addChild(child);
    AbstractViewNode node = template.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE);
    assertThat(node, is(instanceOf(ConcreteArrayOfObjectsNode.class)));
    assertThat(node.getName(), is(equalTo(NAME)));
    assertThat(((ConcreteArrayOfObjectsNode) node).getElementName(),
        is(equalTo(ELEMENT_NAME)));
    assertThat(node.getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(((ConcreteContainerNode) node).getChildren(),
        contains((ViewNode) child));
  }

  private Expectations viewContextExpectations() {
    return new Expectations() {
      {
        oneOf(viewContextFactory).newContext(viewContext);
        will(returnValue(scopedViewContext));
        allowing(scopedViewContext).push(null, Object.class);
        allowing(scopedViewContext).pop();
        allowing(scopedViewContext).getListeners();
        will(returnValue(listeners));
        allowing(listeners).shouldVisitNode(with(any(ViewNodeEvent.class)));
        will(returnValue(true));
        allowing(listeners).nodeVisited(with(any(ViewNodeEvent.class)));
      }
    };
  }

  class MockViewNode extends ConcreteContainerNode {

    public MockViewNode() {
      super(null, null, Object.class);
    }

    @Override
    public Object accept(ViewNodeVisitor visitor, Object state) {
      return generator;
    }

  }

}
