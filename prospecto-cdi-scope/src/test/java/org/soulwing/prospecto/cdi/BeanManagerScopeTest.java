/*
 * File created on Apr 15, 2016
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
package org.soulwing.prospecto.cdi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.jmock.Expectations.returnValue;
import static org.jmock.Expectations.throwException;

import javax.enterprise.inject.UnsatisfiedResolutionException;

import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cdi.BeanManagerLocator;
import org.soulwing.cdi.SimpleBeanManager;

/**
 * Unit tests for {@link BeanManagerScope}.
 *
 * @author Carl Harris
 */
public class BeanManagerScopeTest {

  public static final String BEAN_NAME = "beanName";
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private BeanManagerLocator locator;

  @Mock
  private SimpleBeanManager beanManager;

  @Mock
  private MockBean bean;

  private BeanManagerScope scope;

  @Before
  public void setUp() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(locator).getSimpleBeanManager();
        will(returnValue(beanManager));
      }
    });

    scope = BeanManagerScope.newInstance(locator);
  }

  @Test
  public void testGetBean() throws Exception {
    context.checking(getBeanByTypeExpectations(returnValue(bean)));
    assertThat(scope.get(MockBean.class), is(sameInstance(bean)));
  }

  @Test
  public void testGetBeanWhenNotFound() throws Exception {
    context.checking(getBeanByTypeExpectations(
        throwException(new UnsatisfiedResolutionException())));
    assertThat(scope.get(MockBean.class), is(nullValue()));
  }

  @Test
  public void testGetNamedBean() throws Exception {
    context.checking(getBeanByNameExpectations(returnValue(bean)));
    assertThat(scope.get(BEAN_NAME, MockBean.class), is(sameInstance(bean)));
  }

  @Test
  public void testGetNamedBeanWhenNotFound() throws Exception {
    context.checking(getBeanByNameExpectations(
        throwException(new UnsatisfiedResolutionException())));
    assertThat(scope.get(BEAN_NAME, MockBean.class), is(nullValue()));
  }

  private Expectations getBeanByTypeExpectations(final Action outcome) {
    return new Expectations() {
      {
        oneOf(beanManager).getBean(MockBean.class);
        will(outcome);
      }
    };
  }

  private Expectations getBeanByNameExpectations(final Action outcome) {
    return new Expectations() {
      {
        oneOf(beanManager).getBean(BEAN_NAME, MockBean.class);
        will(outcome);
      }
    };
  }

  private interface MockBean {}


}
