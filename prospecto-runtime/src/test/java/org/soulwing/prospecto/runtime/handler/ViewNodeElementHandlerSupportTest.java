/*
 * File created on Mar 15, 2016
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.handler.ViewNodeEventMatchers.viewNodeElementEvent;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.handler.ViewNodeElementEvent;
import org.soulwing.prospecto.api.handler.ViewNodeElementHandler;

/**
 * Unit tests for {@link ViewNodeElementHandlerSupport}.
 *
 * @author Carl Harris
 */
public class ViewNodeElementHandlerSupportTest {

  private static final Object MODEL = new Object();
  private static final Object ELEMENT = new Object();
  private static final Object HANDLER1_ELEMENT = new Object();
  private static final Object HANDLER2_ELEMENT = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ViewContext viewContext;

  @Mock
  private ViewNodeElementHandler handler;

  @Mock
  private ViewNode node;

  private ViewNodeElementEvent event;

  @Before
  public void setUp() throws Exception {
    event = new ViewNodeElementEvent(node, MODEL, ELEMENT, viewContext);
  }

  @Test
  public void testWillVisitElement() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getViewNodeElementHandlers();
        will(returnValue(Arrays.asList(handler, handler, handler)));
        exactly(2).of(handler).beforeVisitElement(event);
        will(onConsecutiveCalls(returnValue(true), returnValue(false)));
      }
    });

    assertThat(ViewNodeElementHandlerSupport.willVisitElement(event), is(false));
  }


  @Test
  public void testExtractValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getViewNodeElementHandlers();
        will(returnValue(Arrays.asList(handler, handler)));
        oneOf(handler).onExtractElement(with(
            viewNodeElementEvent(node, MODEL, ELEMENT, viewContext)));
        will(returnValue(HANDLER1_ELEMENT));
        oneOf(handler).onExtractElement(with(
            viewNodeElementEvent(node, MODEL, HANDLER1_ELEMENT, viewContext)));
        will(returnValue(HANDLER2_ELEMENT));
      }
    });

    assertThat(ViewNodeElementHandlerSupport.extractedElement(event),
        is(sameInstance(HANDLER2_ELEMENT)));
  }



}
