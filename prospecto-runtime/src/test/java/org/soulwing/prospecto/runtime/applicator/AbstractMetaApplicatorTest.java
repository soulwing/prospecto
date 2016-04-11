/*
 * File created on Apr 8, 2016
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

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.MetadataHandler;
import org.soulwing.prospecto.api.template.MetaNode;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for tests of {@link AbstractMetaApplicator} subtypes.
 *
 * @param <N> meta node type
 * @author Carl Harris
 */
public abstract class AbstractMetaApplicatorTest<N extends MetaNode>
    extends AbstractViewEventApplicatorTest<N> {

  private static final Object VIEW_VALUE = new Object();
  private static final Object TRANSFORMED_VALUE = new Object();

  @Mock
  TransformationService transformationService;

  @Mock
  private MetadataHandler handler;


  @Test
  public void testOnToModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(triggerEvent).getValue();
        will(returnValue(VIEW_VALUE));
        oneOf(transformationService).valueToInject(parentEntity,
            Object.class, VIEW_VALUE, node, viewContext);
        will(returnValue(TRANSFORMED_VALUE));
      }
    });

    assertThat(applicator.onToModelValue(parentEntity, triggerEvent,
        events, viewContext), is(sameInstance(TRANSFORMED_VALUE)));
  }

  @Test
  public void testInjectInContext() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).getHandler();
        will(returnValue(handler));
        oneOf(handler).consumeValue(node, TRANSFORMED_VALUE, viewContext);
      }
    });

    applicator.inject(parentEntity, TRANSFORMED_VALUE, viewContext);
  }

}
