/*
 * File created on Mar 11, 2016
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
package org.soulwing.prospecto.runtime.context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
import java.util.List;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.MutableScope;
import org.soulwing.prospecto.api.Scope;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.handler.ViewNodeElementHandler;
import org.soulwing.prospecto.api.handler.ViewNodeHandler;
import org.soulwing.prospecto.api.handler.ViewNodeValueHandler;

/**
 * Unit tests for {@link ConcreteViewContext}.
 *
 * @author Carl Harris
 */
public class ConcreteViewContextTest {

  private static final String SCOPE0 = "scope0";
  private static final String SCOPE1 = "scope1";
  private static final String SCOPE2 = "scope2";

  private static final String SCOPE0_MOCK = "scope0Mock";
  private static final String SCOPE1_MOCK = "scope1Mock";
  private static final String SCOPE2_MOCK = "scope2Mock";

  @Rule
  public final JUnitRuleMockery mockery = new JUnitRuleMockery();

  private ConcreteViewContext context = new ConcreteViewContext();

  interface MockScope0Type {
  }

  interface MockScope1Type {
  }

  interface MockScope2Type {
  }

  @Test
  public void testAppendScope() throws Exception {
    final Scope scope0 = context.addScope();
    final Scope scope1 = context.addScope();
    final List<Scope> scopes = context.getScopes();
    assertThat(scopes.size(), is(equalTo(2)));
    assertThat(scopes.get(0), is(sameInstance(scope0)));
    assertThat(scopes.get(1), is(sameInstance(scope1)));
  }

  @Test
  public void testInsertScope() throws Exception {
    final Scope scope0 = context.addScope(0);
    final Scope scope1 = context.addScope(0);
    final List<Scope> scopes = context.getScopes();
    assertThat(scopes.size(), is(equalTo(2)));
    assertThat(scopes.get(0), is(sameInstance(scope1)));
    assertThat(scopes.get(1), is(sameInstance(scope0)));
  }


  @Test(expected = RuntimeException.class)
  public void testGetByTypeWhenNotFound() throws Exception {
    context.get(Object.class);
  }

  @Test(expected = RuntimeException.class)
  public void testGetByNameWhenNotFound() throws Exception {
    context.get("name", Object.class);
  }

  @Test
  public void testGetOptionalByTypeWhenNotFound() throws Exception {
    assertThat(context.getOptional(Object.class), is(nullValue()));
  }

  @Test
  public void testGetOptionalByNameWhenNotFound() throws Exception {
    assertThat(context.getOptional("name", Object.class), is(nullValue()));
  }

  @Test
  public void testScopesPutAndGetByType() throws Exception {
    final MockScope0Type scope0Mock = new MockScope0Type() {};

    final MutableScope scope0 = context.newScope();
    context.getScopes().add(scope0);
    scope0.put(SCOPE0);
    scope0.put(scope0Mock);
    assertThat(context.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    final MockScope1Type scope1Mock = new MockScope1Type() {};

    context.push(SCOPE1, null);
    context.put(SCOPE1);
    context.put(scope1Mock);
    assertThat(context.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(context.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    final MockScope2Type scope2Mock = new MockScope2Type() {};

    context.push(SCOPE2, null);
    context.put(SCOPE2);
    context.put(scope2Mock);
    assertThat(context.get(String.class), is(sameInstance(SCOPE2)));
    assertThat(context.get(MockScope2Type.class), is(sameInstance(scope2Mock)));
    assertThat(context.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    context.pop();
    assertThat(context.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(context.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    context.pop();
    assertThat(context.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
  }

  @Test
  public void testScopesPutAndGetByName() throws Exception {
    final MockScope0Type scope0Mock = new MockScope0Type() {};

    final MutableScope scope0 = context.newScope();
    context.getScopes().add(scope0);
    scope0.put(SCOPE0, SCOPE0);
    scope0.put(SCOPE0_MOCK, scope0Mock);
    assertThat(context.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(context.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    final MockScope1Type scope1Mock = new MockScope1Type() {};

    context.push(SCOPE1, null);
    context.put(SCOPE1, SCOPE1);
    context.put(SCOPE1_MOCK, scope1Mock);
    assertThat(context.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(context.get(SCOPE1, String.class), is(sameInstance(SCOPE1)));
    assertThat(context.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(context.get(SCOPE1_MOCK, MockScope1Type.class),
        is(sameInstance(scope1Mock)));
    assertThat(context.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(context.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    final MockScope2Type scope2Mock = new MockScope2Type() {};

    context.push(SCOPE2, null);
    context.put(SCOPE2, SCOPE2);
    context.put(SCOPE2_MOCK, scope2Mock);
    assertThat(context.get(String.class), is(sameInstance(SCOPE2)));
    assertThat(context.get(SCOPE2, String.class), is(sameInstance(SCOPE2)));
    assertThat(context.get(MockScope2Type.class), is(sameInstance(scope2Mock)));
    assertThat(context.get(SCOPE2_MOCK, MockScope2Type.class),
        is(sameInstance(scope2Mock)));
    assertThat(context.get(SCOPE1, String.class), is(sameInstance(SCOPE1)));
    assertThat(context.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(context.get(SCOPE1_MOCK, MockScope1Type.class),
        is(sameInstance(scope1Mock)));
    assertThat(context.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(context.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    context.pop();
    assertThat(context.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(context.get(SCOPE1, String.class), is(sameInstance(SCOPE1)));
    assertThat(context.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(context.get(SCOPE1_MOCK, MockScope1Type.class),
        is(sameInstance(scope1Mock)));
    assertThat(context.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(context.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    context.pop();
    assertThat(context.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(context.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(context.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));
  }

  @Test
  public void testCurrentViewPath() throws Exception {
    assertThat(context.currentViewPath(), is(empty()));
    context.push(SCOPE0, null);
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0))));

    context.push(SCOPE1, null);
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, SCOPE1))));

    context.push(SCOPE2, null);
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, SCOPE1, SCOPE2))));

    context.pop();
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, SCOPE1))));

    context.pop();
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0))));

    context.pop();
    assertThat(context.currentViewPath(), is(empty()));
  }

  @Test
  public void testCurrentModelPathWithUnnamedNode() throws Exception {
    context.push(SCOPE0, null);
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0))));

    context.push(null, null);
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, null))));

    context.push(SCOPE2, null);
    assertThat(context.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, null, SCOPE2))));
  }

  @Test
  public void testCurrentViewPathAsString() throws Exception {
    assertThat(context.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + "")));

    context.push(SCOPE0, null);
    assertThat(context.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0)));

    context.push(SCOPE1, null);
    assertThat(context.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0
            + ViewContext.PATH_DELIMITER + SCOPE1)));

    context.push(SCOPE2, null);
    assertThat(context.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0
            + ViewContext.PATH_DELIMITER + SCOPE1
            + ViewContext.PATH_DELIMITER + SCOPE2)));
  }


  @Test
  public void testCurrentViewPathAsStringWithUnnamedNode() throws Exception {
    context.push(SCOPE0, null);
    assertThat(context.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0)));

    context.push(null, null);
    assertThat(context.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0
            + ViewContext.PATH_DELIMITER)));

    context.push(SCOPE2, null);
    assertThat(context.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0
            + ViewContext.PATH_DELIMITER
            + ViewContext.PATH_DELIMITER + SCOPE2)));
  }

  @Test
  public void testCurrentModelPath() throws Exception {
    assertThat(context.currentModelPath(), is(empty()));

    context.push(null, MockScope0Type.class);
    assertThat(context.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    context.push(null, null);
    assertThat(context.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    context.push(null, MockScope2Type.class);
    assertThat(context.currentModelPath(),
        is(equalTo(Arrays.asList(MockScope0Type.class,
            MockScope2Type.class))));

    context.pop();
    assertThat(context.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    context.pop();
    assertThat(context.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    context.pop();
    assertThat(context.currentModelPath(), is(empty()));
  }

  @Test
  public void testCopy() throws Exception {
    final Scope scope =
        mockery.mock(Scope.class);
    final ViewNodeHandler viewNodeHandler =
        mockery.mock(ViewNodeHandler.class);
    final ViewNodeElementHandler viewNodeElementHandler =
        mockery.mock(ViewNodeElementHandler.class);
    final ViewNodeValueHandler viewNodeValueHandler =
        mockery.mock(ViewNodeValueHandler.class);
    final ValueTypeConverter valueTypeConverter =
        mockery.mock(ValueTypeConverter.class);

    context.getScopes().add(scope);
    context.getViewNodeHandlers().add(viewNodeHandler);
    context.getViewNodeElementHandlers().add(viewNodeElementHandler);
    context.getViewNodeValueHandlers().add(viewNodeValueHandler);
    context.getValueTypeConverters().add(valueTypeConverter);

    ViewContext contextCopy = new ConcreteViewContext(context);
    context.getScopes().clear();
    context.getViewNodeHandlers().clear();
    context.getViewNodeElementHandlers().clear();
    context.getViewNodeValueHandlers().clear();
    context.getValueTypeConverters().clear();

    assertThat(contextCopy.getScopes(), contains(scope));
    assertThat(contextCopy.getViewNodeHandlers(), contains(viewNodeHandler));
    assertThat(contextCopy.getViewNodeElementHandlers(),
        contains(viewNodeElementHandler));
    assertThat(contextCopy.getViewNodeValueHandlers(),
        contains(viewNodeValueHandler));
    assertThat(contextCopy.getValueTypeConverters(),
        contains(valueTypeConverter));
  }

}
