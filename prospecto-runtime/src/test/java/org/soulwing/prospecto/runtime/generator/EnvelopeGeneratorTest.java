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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.EnvelopeNode;

/**
 * Unit tests for {@link EnvelopeGenerator}.
 *
 * @author Carl Harris
 */
public class EnvelopeGeneratorTest
    extends AbstractViewEventGeneratorTest<EnvelopeNode> {

  @Mock
  private View.Event childEvent;

  @Mock
  private ViewEventGenerator child;

  @Override
  EnvelopeNode newNode() {
    return context.mock(EnvelopeNode.class);
  }

  @Override
  AbstractViewEventGenerator<EnvelopeNode> newGenerator(EnvelopeNode node) {
    return new EnvelopeGenerator(node, Collections.singletonList(child));
  }

  @Test
  public void testGenerate() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations(NAME, MODEL, null));
    context.checking(new Expectations() {
      {
        oneOf(child).generate(MODEL, viewContext);
        will(returnValue(Collections.singletonList(childEvent)));
      }
    });

    final List<View.Event> events = generator.generate(MODEL, viewContext);
    assertThat(events.size(), is(3));
    assertThat(events.get(0).getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.get(0).getName(), is(equalTo(NAME)));
    assertThat(events.get(0).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(0).getValue(), is(nullValue()));
    assertThat(events.get(1), is(sameInstance(childEvent)));
    assertThat(events.get(2).getType(), is(equalTo(View.Event.Type.END_OBJECT)));
    assertThat(events.get(2).getName(), is(equalTo(NAME)));
    assertThat(events.get(2).getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(events.get(2).getValue(), is(nullValue()));
  }

}
