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
package org.soulwing.prospecto.runtime.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.meta.MetadataHandler;
import org.soulwing.prospecto.api.template.MetaNode;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for unit tests of {@link AbstractMetaGenerator} subtypes.
 *
 * @param <N> meta node type
 * @author Carl Harris
 */
public abstract class AbstractMetaGeneratorTest<N extends MetaNode>
    extends AbstractViewEventGeneratorTest<N> {

  private static final Object MODEL_VALUE = new Object();
  private static final Object TRANSFORMED_VALUE = new Object();

  @Mock
  TransformationService transformationService;

  @Mock
  private MetadataHandler handler;

  @Test
  public void testGenerate() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        oneOf(node).getHandler();
        will(returnValue(handler));
        oneOf(handler).produceValue(node, MODEL, viewContext);
        will(returnValue(MODEL_VALUE));
        oneOf(transformationService).valueToExtract(MODEL, MODEL_VALUE, node,
            viewContext);
        will(returnValue(TRANSFORMED_VALUE));
      }
    });

    assertThat(generator.generate(MODEL, viewContext),
        contains(expectedEvent(TRANSFORMED_VALUE)));
  }

  abstract Matcher<View.Event> expectedEvent(Object value);

}
