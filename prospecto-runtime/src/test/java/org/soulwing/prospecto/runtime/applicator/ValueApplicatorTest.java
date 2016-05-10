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
import static org.hamcrest.Matchers.sameInstance;

import java.util.EnumSet;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.template.UpdatableValueNode;
import org.soulwing.prospecto.api.template.ValueNode;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * Unit tests for {@link ValueApplicator}.
 *
 * @author Carl Harris
 */
public class ValueApplicatorTest
    extends AbstractViewEventApplicatorTest<ValueNode> {

  private static final Object VIEW_VALUE = new Object();
  private static final Object MODEL_VALUE = new Object();

  @Mock
  private TransformationService transformationService;

  @Override
  ValueNode newNode() {
    return context.mock(UpdatableValueNode.class);
  }

  @Override
  AbstractViewEventApplicator<ValueNode> newApplicator(ValueNode node) {
    return new ValueApplicator(node, transformationService);
  }

  @Test
  public void testOnToModelValue() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getDataType();
        will(returnValue(MockDataType.class));
        oneOf(triggerEvent).getValue();
        will(returnValue(VIEW_VALUE));
        oneOf(transformationService).valueToInject(parentEntity,
            MockDataType.class, VIEW_VALUE, node, viewContext);
        will(returnValue(MODEL_VALUE));
      }
    });

    assertThat(applicator.toModelValue(parentEntity, triggerEvent, events,
        viewContext), is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testInjectInContext() throws Exception {
    validateInject(EnumSet.of(AccessMode.WRITE));
  }

  @Test
  public void testInjectInContextWhenNotWritable() throws Exception {
    validateInject(EnumSet.noneOf(AccessMode.class));
  }

  private void validateInject(final EnumSet<AccessMode> allowedModes)
      throws Exception {
    context.checking(new Expectations() {
      {
        oneOf((UpdatableValueNode) node).getAllowedModes();
        will(returnValue(allowedModes));
        if (!allowedModes.isEmpty()) {
          oneOf((UpdatableValueNode) node).setValue(MODEL, MODEL_VALUE);
        }
      }
    });

    applicator.inject(MODEL, MODEL_VALUE, viewContext);
  }

  interface MockDataType {}

}
