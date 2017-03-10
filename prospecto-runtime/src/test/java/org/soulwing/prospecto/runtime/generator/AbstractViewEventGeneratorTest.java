/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.runtime.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.eventDescribing;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.forModel;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.inContext;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link AbstractViewEventGenerator}.
 *
 * @author Carl Harris
 */
public abstract class AbstractViewEventGeneratorTest<N extends ViewNode> {

  static final String NAME = "name";
  static final String NAMESPACE = "namespace";
  static final Object MODEL = new Object();
  static final Class<?> MODEL_TYPE = MODEL.getClass();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  NotifiableViewListeners listeners;

  @Mock
  private View.Event event;

  N node;

  AbstractViewEventGenerator<N> generator;

  @Before
  public void setUp() throws Exception {
    node = newNode();
    generator = newGenerator(node);
  }

  abstract N newNode();

  abstract AbstractViewEventGenerator<N> newGenerator(N node);

  @Test
  public void testGenerateWhenShouldNotVisit() throws Exception {

    context.checking(new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));

        oneOf(viewContext).put(MODEL);
        oneOf(listeners).shouldVisitNode(with(
            eventDescribing(sourceNode(node),
                forModel(MODEL), inContext(viewContext))));
        will(returnValue(false));
      }
    });

    assertThat(generator.generate(MODEL, viewContext),
        is(emptyCollectionOf(View.Event.class)));
  }

  Expectations baseExpectations() {
    return baseExpectations(MODEL, MODEL_TYPE);
  }

  Expectations baseExpectations(final Object model, final Class<?> modelType) {
    return new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));

        oneOf(listeners).shouldVisitNode(with(
            eventDescribing(sourceNode(node),
                forModel(model), inContext(viewContext))));
        will(returnValue(true));

        oneOf(listeners).nodeVisited(with(
            eventDescribing(sourceNode(node),
                forModel(model), inContext(viewContext))));

        allowing(node).getName();
        will(returnValue(NAME));
        allowing(node).getModelType();
        will(returnValue(modelType));
        allowing(node).getNamespace();
        will(returnValue(NAMESPACE));

      }
    };
  }

  Expectations contextScopeExpectations() throws Exception {
    return contextScopeExpectations(NAME, MODEL, MODEL_TYPE);
  }

  Expectations contextScopeExpectations(final String name,
      final Object model, final Class<?> modelType) throws Exception {
    return new Expectations() {
      {
        oneOf(viewContext).push(name, modelType);
        if (model != null) {
          oneOf(viewContext).put(model);
        }
        oneOf(viewContext).pop();
      }
    };
  }

  class MockGenerator extends AbstractViewEventGenerator<ViewNode> {

    MockGenerator(ViewNode node) {
      super(node);
    }

    @Override
    List<View.Event> onGenerate(Object model, ScopedViewContext context)
        throws Exception {
      return Collections.singletonList(event);
    }

  }
}
