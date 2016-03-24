/*
 * File created on Mar 24, 2016
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
package org.soulwing.prospecto.runtime.handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.handler.ViewListener;
import org.soulwing.prospecto.api.handler.ViewNodeAcceptor;
import org.soulwing.prospecto.api.handler.ViewNodeEntityListener;
import org.soulwing.prospecto.api.handler.ViewNodeEvent;
import org.soulwing.prospecto.api.handler.ViewNodeListener;
import org.soulwing.prospecto.api.handler.ViewNodePropertyAcceptor;
import org.soulwing.prospecto.api.handler.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.handler.ViewNodePropertyInterceptor;
import org.soulwing.prospecto.api.handler.ViewNodePropertyListener;

/**
 * Unit tests for {@link LinkedListNotifiableViewListeners}.
 *
 * @author Carl Harris
 */
public class LinkedListNotifiableViewListenersTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  private LinkedListNotifiableViewListeners listeners =
      new LinkedListNotifiableViewListeners();

  @Test
  public void testAppendPrependAndRemove() throws Exception {
    final ViewListener listener0 = context.mock(ViewListener.class, "listener0");
    final ViewListener listener1 = context.mock(ViewListener.class, "listener1");
    final ViewListener listener2 = context.mock(ViewListener.class, "listener2");

    listeners.append(listener1);
    listeners.prepend(listener0);
    listeners.append(listener2);
    assertThat(listeners.toList(), contains(listener0, listener1, listener2));
  }

  @Test
  public void testFireShouldVisitNode() throws Exception {
    final ViewNodeAcceptor acceptor0 =
        context.mock(ViewNodeAcceptor.class, "acceptor0");
    final ViewNodeAcceptor acceptor1 =
        context.mock(ViewNodeAcceptor.class, "acceptor1");

    final ViewNodeEvent event = new ViewNodeEvent(null, null, null);
    context.checking(new Expectations() {
      {
        oneOf(acceptor0).shouldVisitNode(event);
        will(returnValue(true));
        oneOf(acceptor1).shouldVisitNode(event);
        will(returnValue(true));
      }
    });

    listeners.append(acceptor0);
    listeners.append(acceptor1);
    assertThat(listeners.fireShouldVisitNode(event), is(true));
  }

  @Test
  public void testFireShouldVisitNodeShortCircuit() throws Exception {
    final ViewNodeAcceptor acceptor0 =
        context.mock(ViewNodeAcceptor.class, "acceptor0");
    final ViewNodeAcceptor acceptor1 =
        context.mock(ViewNodeAcceptor.class, "acceptor1");

    final ViewNodeEvent event = new ViewNodeEvent(null, null, null);
    context.checking(new Expectations() {
      {
        oneOf(acceptor0).shouldVisitNode(event);
        will(returnValue(false));
      }
    });

    listeners.append(acceptor0);
    listeners.append(acceptor1);
    assertThat(listeners.fireShouldVisitNode(event), is(false));
  }

  @Test
  public void testFireNodeVisited() throws Exception {
    final ViewNodeListener listener = context.mock(ViewNodeListener.class);
    final ViewNodeEvent event = new ViewNodeEvent(null, null, null);
    context.checking(new Expectations() {
      {
        oneOf(listener).nodeVisited(event);
      }
    });

    listeners.append(listener);
    listeners.fireNodeVisited(event);
  }

  @Test
  public void testFireShouldVisitProperty() throws Exception {
    final ViewNodePropertyAcceptor acceptor0 =
        context.mock(ViewNodePropertyAcceptor.class, "acceptor0");
    final ViewNodePropertyAcceptor acceptor1 =
        context.mock(ViewNodePropertyAcceptor.class, "acceptor1");

    final ViewNodePropertyEvent event =
        new ViewNodePropertyEvent(null, null, null, null);

    context.checking(new Expectations() {
      {
        oneOf(acceptor0).shouldVisitProperty(event);
        will(returnValue(true));
        oneOf(acceptor1).shouldVisitProperty(event);
        will(returnValue(true));
      }
    });

    listeners.append(acceptor0);
    listeners.append(acceptor1);
    assertThat(listeners.fireShouldVisitProperty(event), is(true));
  }

  @Test
  public void testFireShouldVisitPropertyShortCircuit() throws Exception {
    final ViewNodePropertyAcceptor acceptor0 =
        context.mock(ViewNodePropertyAcceptor.class, "acceptor0");
    final ViewNodePropertyAcceptor acceptor1 =
        context.mock(ViewNodePropertyAcceptor.class, "acceptor1");

    final ViewNodePropertyEvent event =
        new ViewNodePropertyEvent(null, null, null, null);

    context.checking(new Expectations() {
      {
        oneOf(acceptor0).shouldVisitProperty(event);
        will(returnValue(false));
      }
    });

    listeners.append(acceptor0);
    listeners.append(acceptor1);
    assertThat(listeners.fireShouldVisitProperty(event), is(false));
  }

  @Test
  public void testFireOnExtractProperty() throws Exception {
    final ViewNodePropertyInterceptor listener0 =
        context.mock(ViewNodePropertyInterceptor.class, "listener0");
    final ViewNodePropertyInterceptor listener1 =
        context.mock(ViewNodePropertyInterceptor.class, "listener1");

    final Object value0 = new Object();
    final Object value1 = new Object();
    final Object value2 = new Object();
    final ViewNodePropertyEvent event =
        new ViewNodePropertyEvent(null, null, value0, null);

    context.checking(new Expectations() {
      {
        oneOf(listener0).onExtractValue((ViewNodePropertyEvent)
            with(hasProperty("value", sameInstance(value0))));
        will(returnValue(value1));
        oneOf(listener1).onExtractValue((ViewNodePropertyEvent)
            with(hasProperty("value", sameInstance(value1))));
        will(returnValue(value2));
      }
    });

    listeners.append(listener0);
    listeners.append(listener1);
    assertThat(listeners.fireOnExtractValue(event), is(sameInstance(value2)));
  }

  @Test
  public void testFireOnInjectValue() throws Exception {
    final ViewNodePropertyInterceptor listener0 =
        context.mock(ViewNodePropertyInterceptor.class, "listener0");
    final ViewNodePropertyInterceptor listener1 =
        context.mock(ViewNodePropertyInterceptor.class, "listener1");

    final Object value0 = new Object();
    final Object value1 = new Object();
    final Object value2 = new Object();
    final ViewNodePropertyEvent event =
        new ViewNodePropertyEvent(null, null, value0, null);

    context.checking(new Expectations() {
      {
        oneOf(listener0).onInjectValue((ViewNodePropertyEvent)
            with(hasProperty("value", sameInstance(value0))));
        will(returnValue(value1));
        oneOf(listener1).onInjectValue((ViewNodePropertyEvent)
            with(hasProperty("value", sameInstance(value1))));
        will(returnValue(value2));
      }
    });

    listeners.append(listener0);
    listeners.append(listener1);
    assertThat(listeners.fireOnInjectValue(event), is(sameInstance(value2)));
  }

  @Test
  public void testFirePropertyVisited() throws Exception {
    final ViewNodePropertyListener listener =
        context.mock(ViewNodePropertyListener.class);

    final ViewNodePropertyEvent event =
        new ViewNodePropertyEvent(null, null, null, null);

    context.checking(new Expectations() {
      {
        oneOf(listener).propertyVisited(event);
      }
    });

    listeners.append(listener);
    listeners.firePropertyVisited(event);
  }

  @Test
  public void testFireEntityCreated() throws Exception {
    final ViewNodeEntityListener listener =
        context.mock(ViewNodeEntityListener.class);

    final ViewNodePropertyEvent event =
        new ViewNodePropertyEvent(null, null, null, null);

    context.checking(new Expectations() {
      {
        oneOf(listener).entityCreated(event);
      }
    });

    listeners.append(listener);
    listeners.fireEntityCreated(event);
  }

  @Test
  public void testFireEntityDiscarded() throws Exception {
    final ViewNodeEntityListener listener =
        context.mock(ViewNodeEntityListener.class);

    final ViewNodePropertyEvent event =
        new ViewNodePropertyEvent(null, null, null, null);

    context.checking(new Expectations() {
      {
        oneOf(listener).entityDiscarded(event);
      }
    });

    listeners.append(listener);
    listeners.fireEntityDiscarded(event);
  }

}