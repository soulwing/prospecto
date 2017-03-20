/*
 * File created on Apr 11, 2016
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
package org.soulwing.prospecto.runtime.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@link ConcreteInjectableViewEntity}.
 *
 * @author Carl Harris
 */
public class ConcreteInjectableViewEntityTest {

  private static final Object VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private InjectableViewEntity childEntity;

  @Mock
  private InjectableViewEntity.Injector childInjector;

  @Mock
  private InjectableViewEntity.ValueInjector valueInjector;

  @Mock
  private MockModel target;

  private ConcreteInjectableViewEntity entity =
      new ConcreteInjectableViewEntity(MockModel.class);

  @Before
  public void setUp() throws Exception {
    entity.put("child", childEntity, childInjector);
    entity.put("value", VALUE, valueInjector);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNavigateToNullPath() throws Exception {
    entity.navigateTo(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNavigateToEmptyPath() throws Exception {
    entity.navigateTo("");
  }

  @Test
  public void testNavigateToChild() throws Exception {
    assertThat(entity.navigateTo("child"),
        is(sameInstance((Object) childEntity)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNavigateToValue() throws Exception {
    entity.navigateTo("value");
  }


  @Test
  public void testNavigateToGrandchild() throws Exception {
    final ViewEntity grandchild = context.mock(ViewEntity.class, "grandchild");
    context.checking(new Expectations() {
      {
        oneOf(childEntity).get("grandchild");
        will(returnValue(grandchild));
      }
    });

    assertThat(entity.navigateTo("child.grandchild"),
        is(sameInstance((Object) grandchild)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNavigateToNestedValue() throws Exception {
    final InjectableViewEntity.ValueInjector grandchild = context.mock(
        InjectableViewEntity.ValueInjector.class, "grandchild");
    context.checking(new Expectations() {
      {
        oneOf(childEntity).get("grandchild");
        will(returnValue(grandchild));
      }
    });

    entity.navigateTo("child.grandchild");
  }

  @Test
  public void testInjectTarget() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(valueInjector).inject(target, VALUE);
      }
    });

    entity.inject(target);
  }

  @Test
  public void testInjectTargetContext() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(valueInjector).inject(target, VALUE, viewContext);
        oneOf(childInjector).inject(target, childEntity, viewContext);
      }
    });

    entity.inject(target, viewContext);
  }

  private interface MockModel {}

}
