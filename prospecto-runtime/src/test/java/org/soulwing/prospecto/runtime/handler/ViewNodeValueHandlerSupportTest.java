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
import static org.soulwing.prospecto.runtime.handler.ViewNodeEventMatchers.viewNodeValueEvent;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.handler.ViewNodeValueEvent;
import org.soulwing.prospecto.api.handler.ViewNodeValueHandler;

/**
 * Unit tests for {@link ViewNodeValueHandlerSupport}.
 *
 * @author Carl Harris
 */
public class ViewNodeValueHandlerSupportTest {

  private static final Object VALUE = new Object();
  private static final Object HANDLER1_VALUE = new Object();
  private static final Object HANDLER2_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ViewContext viewContext;

  @Mock
  private ViewNodeValueHandler handler;

  @Mock
  private ViewNode node;

  private ViewNodeValueEvent event;

  @Before
  public void setUp() throws Exception {
    event = new ViewNodeValueEvent(node, VALUE, viewContext);
  }

  @Test
  public void testExtractValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getViewNodeValueHandlers();
        will(returnValue(Arrays.asList(handler, handler)));
        oneOf(handler).onExtractValue(with(
            viewNodeValueEvent(node, VALUE, viewContext)));
        will(returnValue(HANDLER1_VALUE));
        oneOf(handler).onExtractValue(with(
            viewNodeValueEvent(node, HANDLER1_VALUE, viewContext)));
        will(returnValue(HANDLER2_VALUE));
      }
    });

    assertThat(ViewNodeValueHandlerSupport.extractedValue(event),
        is(sameInstance(HANDLER2_VALUE)));
  }

  @Test
  public void testInjectValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getViewNodeValueHandlers();
        will(returnValue(Arrays.asList(handler, handler)));
        oneOf(handler).onInjectValue(with(
            viewNodeValueEvent(node, VALUE, viewContext)));
        will(returnValue(HANDLER1_VALUE));
        oneOf(handler).onInjectValue(with(
            viewNodeValueEvent(node, HANDLER1_VALUE, viewContext)));
        will(returnValue(HANDLER2_VALUE));
      }
    });

    assertThat(ViewNodeValueHandlerSupport.injectedValue(event),
        is(sameInstance(HANDLER2_VALUE)));
  }

}
