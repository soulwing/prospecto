/*
 * File created on Mar 17, 2016
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@link DiscriminatorNode}.
 *
 * @author Carl Harris
 */
public class DiscriminatorNodeTest {

  private static final Discriminator DISCRIMINATOR = new Discriminator(
      new Object());

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private DiscriminatorStrategy discriminatorStrategy;

  @Mock
  private MockSubModel model;

  private DiscriminatorNode node = new DiscriminatorNode();

  @Before
  public void setUp() throws Exception {
    node.setBase(MockModel.class);
    node.put(discriminatorStrategy);
  }

  @Test
  public void testGetModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(discriminatorStrategy).toDiscriminator(
            with(MockModel.class), with(subtypeOf(MockModel.class)));
        will(returnValue(DISCRIMINATOR));
      }
    });

    final Object modelValue = node.getModelValue(model, viewContext);
    assertThat(modelValue, is(sameInstance((Object) DISCRIMINATOR)));
  }

  private static Matcher<Class<?>> subtypeOf(final Class<?> base) {
    return new BaseMatcher<Class<?>>() {
      @Override
      public boolean matches(Object item) {
        return base.isAssignableFrom((Class<?>) item);
      }

      @Override
      public void describeTo(Description description) {
      }
    };
  }

  @Test
  public void testGetEventName() throws Exception {
    assertThat(node.getEventName(DISCRIMINATOR, viewContext),
        is(equalTo(DISCRIMINATOR.getName())));
  }

  @Test
  public void testToViewValue() throws Exception {
    assertThat(node.toViewValue(DISCRIMINATOR, viewContext),
        is(equalTo(DISCRIMINATOR.getValue())));
  }

  interface MockModel {
  }

  interface MockSubModel extends MockModel {
  }

}
