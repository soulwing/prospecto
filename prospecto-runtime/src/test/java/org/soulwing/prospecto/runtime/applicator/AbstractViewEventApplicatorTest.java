/*
 * File created on Apr 7, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.eventDescribing;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.forModel;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.inContext;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import java.util.Deque;
import java.util.LinkedList;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * An abstract base for tests of {@link AbstractViewEventApplicator}
 * implementations.
 *
 * @param <N> view node type
 * @author Carl Harris
 */
public abstract class AbstractViewEventApplicatorTest<N extends ViewNode> {

  static final String NAME = "name";
  static final Object MODEL = new Object();
  static final Class<?> MODEL_TYPE = MODEL.getClass();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  NotifiableViewListeners listeners;

  @Mock
  MutableViewEntity parentEntity;

  @Mock
  View.Event triggerEvent;

  Deque<View.Event> events = new LinkedList<>();

  N node;

  AbstractViewEventApplicator<N> applicator;

  @Before
  public void setUp() throws Exception {
    node = newNode();
    applicator = newApplicator(node);
  }

  abstract AbstractViewEventApplicator<N> newApplicator(N node);

  abstract N newNode();

  @Test
  public void testGenerateWhenShouldNotVisit() throws Exception {
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));

        oneOf(listeners).shouldVisitNode(with(
            eventDescribing(sourceNode(node),
                forModel(parentEntity), inContext(viewContext))));
        will(returnValue(false));
      }
    });

    assertThat(applicator.toModelValue(parentEntity, triggerEvent, events,
        viewContext), is((Object) UndefinedValue.INSTANCE));
  }

  Expectations baseExpectations() throws Exception {
    return baseExpectations(true);
  }

  Expectations baseExpectations(boolean successful) throws Exception {
    return baseExpectations(NAME, parentEntity, successful);
  }

  Expectations baseExpectations(final String name,
      final MutableViewEntity parentEntity, final boolean successful)
      throws Exception {
    return new Expectations() {
      {
        allowing(viewContext).getListeners();
        will(returnValue(listeners));

        oneOf(listeners).shouldVisitNode(with(
            eventDescribing(sourceNode(node),
                forModel(parentEntity), inContext(viewContext))));
        will(returnValue(true));

        if (successful) {
          oneOf(listeners).nodeVisited(with(
              eventDescribing(sourceNode(node),
                  forModel(parentEntity), inContext(viewContext))));
        }
      }
    };
  }

  Expectations contextScopeExpectations() throws Exception {
    return contextScopeExpectations(true);
  }

  Expectations contextScopeExpectations(boolean successful) throws Exception {
    return contextScopeExpectations(NAME, parentEntity, successful);
  }

  Expectations contextScopeExpectations(final String name,
      final MutableViewEntity parentEntity, final boolean successful)
      throws Exception {
    return new Expectations() {
      {
        allowing(node).getName();
        will(returnValue(name));
        allowing(parentEntity).getType();
        will(returnValue(MODEL_TYPE));
        oneOf(viewContext).push(name, MODEL_TYPE);
        oneOf(viewContext).put(parentEntity);
        if (successful) {
          oneOf(viewContext).pop();
        }
      }
    };
  }

}
