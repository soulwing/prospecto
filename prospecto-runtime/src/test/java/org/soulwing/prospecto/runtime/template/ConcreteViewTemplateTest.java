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
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.Iterator;

import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.listener.ViewMode;
import org.soulwing.prospecto.api.listener.ViewTraversalEvent;
import org.soulwing.prospecto.api.scope.MutableScope;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.applicator.ViewApplicatorFactory;
import org.soulwing.prospecto.runtime.applicator.ViewEventApplicator;
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
  private static final String DATA_KEY = "dataKey";

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

  @Mock
  private ViewEventApplicator applicator;

  @Mock
  private ViewApplicator viewApplicator;

  @Mock
  private MutableScope mutableScope;

  @Mock
  private View view;

  @Mock
  private ViewApplicatorFactory viewApplicatorFactory;

  private MockGeneratorViewNode generatorRoot = new MockGeneratorViewNode();

  private MockApplicatorViewNode applicatorRoot = new MockApplicatorViewNode();

  private ConcreteViewTemplate generatorTemplate;

  private ConcreteViewTemplate applicatorTemplate;

  @Before
  public void setUp() throws Exception {
    generatorTemplate = new ConcreteViewTemplate(generatorRoot,
        viewContextFactory);
    applicatorTemplate = new ConcreteViewTemplate(applicatorRoot,
        viewContextFactory, viewApplicatorFactory);
  }

  @Test
  public void testGenerateView() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(scopedViewContext).appendScope();
        will(returnValue(mutableScope));
        oneOf(mutableScope).put(MODEL);
        oneOf(listeners).beforeTraversing(
            with(Matchers.<ViewTraversalEvent>allOf(
                hasProperty("mode", equalTo(ViewMode.GENERATE)),
                hasProperty("source", sameInstance(generatorTemplate)))));
        oneOf(generator).generate(MODEL, scopedViewContext);
        will(returnValue(Collections.singletonList(event)));
        oneOf(listeners).afterTraversing(
            with(Matchers.<ViewTraversalEvent>allOf(
                hasProperty("mode", equalTo(ViewMode.GENERATE)),
                hasProperty("source", sameInstance(generatorTemplate)))));
      }
    });

    View view = generatorTemplate.generateView(MODEL, viewContext);
    Iterator<View.Event> events = view.iterator();
    assertThat(events.hasNext(), is(true));
    assertThat(events.next(), is(sameInstance(event)));
  }

  @Test(expected = ViewException.class)
  public void testGenerateViewWhenEvaluateThrowsException() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(scopedViewContext).appendScope();
        will(returnValue(mutableScope));
        oneOf(mutableScope).put(MODEL);
        oneOf(listeners).beforeTraversing(
            with(Matchers.<ViewTraversalEvent>allOf(
                hasProperty("mode", equalTo(ViewMode.GENERATE)),
                hasProperty("source", sameInstance(generatorTemplate)))));
        oneOf(generator).generate(MODEL, scopedViewContext);
        will(throwException(new Exception()));
      }
    });

    generatorTemplate.generateView(MODEL, viewContext);
  }

  @Test
  public void testCreateApplicator() throws Exception {
    context.checking(viewContextExpectations());
    context.checking(new Expectations() {
      {
        oneOf(listeners).beforeTraversing(
            with(Matchers.<ViewTraversalEvent>allOf(
                hasProperty("mode", equalTo(ViewMode.APPLY)),
                hasProperty("source", sameInstance(applicatorTemplate)))));
        oneOf(viewApplicatorFactory).newApplicator(
            with(Object.class), with(applicator), with(view),
            with(scopedViewContext), with(DATA_KEY),
            with(Matchers.<ViewTraversalEvent>allOf(
                hasProperty("mode", equalTo(ViewMode.APPLY)),
                hasProperty("source", sameInstance(applicatorTemplate)))));
        will(returnValue(viewApplicator));
      }
    });

    assertThat(applicatorTemplate.createApplicator(view, viewContext, DATA_KEY),
        is(sameInstance(viewApplicator)));
  }

  @Test
  public void testObjectSubView() throws Exception {
    AbstractViewNode child = new MockGeneratorViewNode();
    generatorRoot.addChild(child);
    AbstractViewNode node = generatorTemplate.object(NAME, NAMESPACE);
    assertThat(node, is(instanceOf(ConcreteObjectNode.class)));
    assertThat(node.getName(), is(equalTo(NAME)));
    assertThat(node.getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(((AbstractContainerNode) node).getChildren(),
        contains((ViewNode) child));
  }

  @Test
  public void testArrayOfObjectsSubView() throws Exception {
    AbstractViewNode child = new MockGeneratorViewNode();
    generatorRoot.addChild(child);
    AbstractViewNode node = generatorTemplate.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE);
    assertThat(node, is(instanceOf(ConcreteArrayOfObjectsNode.class)));
    assertThat(node.getName(), is(equalTo(NAME)));
    assertThat(((ConcreteArrayOfObjectsNode) node).getElementName(),
        is(equalTo(ELEMENT_NAME)));
    assertThat(node.getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(((AbstractContainerNode) node).getChildren(),
        contains((ViewNode) child));
  }

  private Expectations viewContextExpectations() {
    return new Expectations() {
      {
        oneOf(viewContextFactory).newContext(viewContext);
        will(returnValue(scopedViewContext));
        allowing(scopedViewContext).getListeners();
        will(returnValue(listeners));
      }
    };
  }

  class MockGeneratorViewNode extends AbstractContainerNode {

    public MockGeneratorViewNode() {
      super(null, null, null, Object.class);
    }

    @Override
    public Object accept(ViewNodeVisitor visitor, Object state) {
      return generator;
    }

  }

  class MockApplicatorViewNode extends AbstractContainerNode {

    public MockApplicatorViewNode() {
      super(null, null, null, Object.class);
    }

    @Override
    public Object accept(ViewNodeVisitor visitor, Object state) {
      return applicator;
    }

  }


}
