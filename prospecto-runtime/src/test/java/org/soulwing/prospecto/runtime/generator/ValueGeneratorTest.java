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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.inNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.withName;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.node.ValueNode;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * Unit tests for {@link ValueGenerator}.
 *
 * @author Carl Harris
 */
public class ValueGeneratorTest extends AbstractViewEventGeneratorTest<ValueNode> {

  private static final Object MODEL_VALUE = new Object();
  private static final Object TRANSFORMED_VALUE = new Object();

  @Mock
  private TransformationService transformationService;

  @Override
  ValueNode newNode() {
    return context.mock(ValueNode.class);
  }

  @Override
  AbstractViewEventGenerator<ValueNode> newGenerator(ValueNode node) {
    return new ValueGenerator(node, transformationService);
  }

  @Test
  public void testGenerate() throws Exception {
    context.checking(baseExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getValue(MODEL);
        will(returnValue(MODEL_VALUE));
        oneOf(transformationService).valueToExtract(MODEL, MODEL_VALUE, node,
            viewContext);
        will(returnValue(TRANSFORMED_VALUE));
      }
    });

    assertThat(generator.generate(MODEL, viewContext),
        contains(
            eventOfType(View.Event.Type.VALUE,
                withName(NAME),
                inNamespace(NAMESPACE),
                whereValue(is(sameInstance(TRANSFORMED_VALUE))))));
  }

  @Test
  public void testGenerateWhenUndefinedValue() throws Exception {
    context.checking(baseExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getValue(MODEL);
        will(returnValue(MODEL_VALUE));
        oneOf(transformationService).valueToExtract(MODEL, MODEL_VALUE,
             node, viewContext);
        will(returnValue(UndefinedValue.INSTANCE));
      }
    });

    assertThat(generator.generate(MODEL, viewContext), is(empty()));
  }

}
