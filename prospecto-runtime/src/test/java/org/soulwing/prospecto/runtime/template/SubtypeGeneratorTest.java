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
package org.soulwing.prospecto.runtime.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.node.SubtypeNode;

/**
 * Unit tests for {@link SubtypeGeneratorTest}.
 *
 * @author Carl Harris
 */
public class SubtypeGeneratorTest
    extends AbstractViewEventGeneratorTest<SubtypeNode> {

  @Mock
  private View.Event childEvent;

  @Mock
  private ViewEventGenerator child;

  @Mock
  private MockSubModel1 model1;

  @Mock
  private MockSubModel2 model2;

  @Override
  SubtypeNode newNode() {
    return context.mock(SubtypeNode.class);
  }

  @Override
  AbstractViewEventGenerator<SubtypeNode> newGenerator(SubtypeNode node) {
    return new SubtypeGenerator(node, Collections.singletonList(child));
  }

  @Test
  public void testGenerate() throws Exception {
    context.checking(baseExpectations(model1, MockSubModel1.class));
    context.checking(new Expectations() {
      {
        oneOf(child).generate(model1, viewContext);
        will(returnValue(Collections.singletonList(childEvent)));
      }
    });
    final List<View.Event> events = generator.generate(model1, viewContext);
    assertThat(events.size(), is(equalTo(1)));
    assertThat(events.get(0), is(sameInstance(childEvent)));
  }

  @Test
  public void testGenerateWithDifferentSubtype() throws Exception {
    context.checking(baseExpectations(model2, MockSubModel1.class));
    final List<View.Event> events = generator.generate(model2, viewContext);
    assertThat(events.isEmpty(), is(true));
  }

  interface MockModel {}

  interface MockSubModel1 extends MockModel {}

  interface MockSubModel2 extends MockModel {}

}
