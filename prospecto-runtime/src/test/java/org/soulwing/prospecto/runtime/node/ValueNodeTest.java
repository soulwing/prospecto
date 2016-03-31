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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@link ValueNode}.
 *
 * @author Carl Harris
 */
public class ValueNodeTest {

  private static final String NAME = "name";
  private static final String NAMESPACE = "namespace";
  private static final Object MODEL = new Object();
  private static final Object MODEL_VALUE = new Object();
  private static final Object VIEW_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private Accessor accessor;

  @Mock
  private ValueTypeConverter<?> converter;

  @Mock
  private ScopedViewContext viewContext;

  private ValueNode node = new ValueNode(NAME,  NAMESPACE);

  @Test
  public void testGetModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(accessor).get(MODEL);
        will(returnValue(MODEL_VALUE));
      }
    });

    node.setAccessor(accessor);
    assertThat(node.getModelValue(MODEL, viewContext),
        is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testToViewValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(converter).toValue(MODEL_VALUE);
        will(returnValue(VIEW_VALUE));
      }
    });

    node.put(converter);
    assertThat(node.toViewValue(MODEL_VALUE, viewContext),
        is(sameInstance(VIEW_VALUE)));
  }

  @Test
  public void testWithContextConverter() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getValueTypeConverters();
        will(returnValue(Collections.singletonList(converter)));
        oneOf(converter).supports(Object.class);
        will(returnValue(true));
        oneOf(converter).toValue(MODEL_VALUE);
        will(returnValue(VIEW_VALUE));
      }
    });

    assertThat(node.toViewValue(MODEL_VALUE, viewContext),
        is(sameInstance(VIEW_VALUE)));
  }

}


