/*
 * File created on Mar 11, 2016
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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.handler.ViewNodeValueHandler;
import org.soulwing.prospecto.runtime.accessor.Accessor;

/**
 * Unit tests for {@link ValueNode}.
 *
 * @author Carl Harris
 */
public class ValueNodeTest {

  private static final String NAME = "name";
  private static final String NAMESPACE = "namespace";

  private static final Object MODEL_VALUE = new Object();
  private static final Object VIEW_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery mockery = new JUnitRuleMockery();

  @Mock
  private ViewNodeValueHandler handler;

  @Mock
  private Accessor accessor;

  @Mock
  private ViewContext context;

  private ValueNode node = new ValueNode(NAME, NAMESPACE);

  @Before
  public void setUp() throws Exception {
    node.setAccessor(accessor);
  }

//  @Test
//  public void testEvaluate() throws Exception {
//    context.checking(new Expectations() {
//      {
//        oneOf(context).getViewNodeValueHandlers();
//        will(returnValue(Arrays.asList(handler)));
//        oneOf(handler).onExtractValue(allOf(
//          hasProperty("source", sameInstance(node)),
//          hasProperty("value", sameInstance(MODEL_VALUE))
//
//      }
//    });
//  }

}
