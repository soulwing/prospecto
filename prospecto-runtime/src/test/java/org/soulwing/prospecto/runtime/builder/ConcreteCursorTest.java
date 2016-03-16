/*
 * File created on Mar 16, 2016
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
package org.soulwing.prospecto.runtime.builder;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.testing.JUnitRuleClassImposterizingMockery;

/**
 * Unit tests for {@link ConcreteCursor}.
 *
 * @author Carl Harris
 */
public class ConcreteCursorTest {

  private static final Class<?> MODEL_TYPE = Object.class;
  private static final String MODEL_NAME = "modelName";
  private static final String NODE_NAME = "nodeName";

  @Rule
  public final JUnitRuleMockery context =
      new JUnitRuleClassImposterizingMockery();

  @Mock
  private AccessorFactory accessorFactory;

  @Mock
  private Accessor accessor;

  @Mock
  private AbstractViewNode node;

  private ConcreteCursor cursor;

  @Before
  public void setUp() throws Exception {
    cursor = new ConcreteCursor(MODEL_TYPE, accessorFactory);
  }

  @Test
  public void testAdvanceWithDefaultNameAndAccessType() throws Exception {
    context.checking(advanceExpectations(MODEL_TYPE, NODE_NAME,
        AccessType.PROPERTY));
    cursor.advance(node);
    cursor.advance();
  }

  @Test
  public void testAdvanceWithModelName() throws Exception {
    context.checking(advanceExpectations(MODEL_TYPE, MODEL_NAME,
        AccessType.PROPERTY));
    cursor.advance(node);
    cursor.setModelName(MODEL_NAME);
    cursor.advance();
  }

  @Test
  public void testAdvanceWithAccessType() throws Exception {
    context.checking(advanceExpectations(MODEL_TYPE, NODE_NAME,
        AccessType.FIELD));
    cursor.advance(node);
    cursor.setAccessType(AccessType.FIELD);
    cursor.advance();
  }

  @Test
  public void testAdvanceWithModelNameAndAccessType() throws Exception {
    context.checking(advanceExpectations(MODEL_TYPE, MODEL_NAME,
        AccessType.FIELD));
    cursor.advance(node);
    cursor.setModelName(MODEL_NAME);
    cursor.setAccessType(AccessType.FIELD);
    cursor.advance();
  }

  @Test
  public void testAdvanceWithNull() throws Exception {
    cursor.advance();
    cursor.advance();
  }

  private Expectations advanceExpectations(final Class<?> modelType,
      final String modelName, final AccessType accessType) throws Exception {
    return new Expectations() {
      {
        allowing(node).getName();
        will(returnValue(NODE_NAME));
        oneOf(accessorFactory).newAccessor(modelType, modelName, accessType);
        will(returnValue(accessor));
        oneOf(node).setAccessor(accessor);
      }
    };
  }

}
