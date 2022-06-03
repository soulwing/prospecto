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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
import java.util.List;

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.association.AssociationManager;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.factory.ObjectFactory;
import org.soulwing.prospecto.api.listener.ViewListener;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.reference.ReferenceResolver;
import org.soulwing.prospecto.api.scope.MutableScope;
import org.soulwing.prospecto.api.scope.Scope;

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

  private static final String OPTION_KEY = "optionKey";
  private static final Object OPTION_VALUE = "optionValue";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private Options options;

  private ConcreteViewContext viewContext;

  interface MockScope0Type {
  }

  interface MockScope1Type {
  }

  interface MockScope2Type {
  }

  @Before
  public void setUp() throws Exception {
    viewContext = new ConcreteViewContext(options);
  }

  @Test
  public void testAppendScope() throws Exception {
    final Scope scope0 = viewContext.appendScope();
    final Scope scope1 = viewContext.appendScope();
    final List<Scope> scopes = viewContext.getScopes().toList();
    assertThat(scopes.size(), is(equalTo(2)));
    assertThat(scopes.get(0), is(sameInstance(scope0)));
    assertThat(scopes.get(1), is(sameInstance(scope1)));
  }

  @Test
  public void testInsertScope() throws Exception {
    final Scope scope0 = viewContext.prependScope();
    final Scope scope1 = viewContext.prependScope();
    final List<Scope> scopes = viewContext.getScopes().toList();
    assertThat(scopes.size(), is(equalTo(2)));
    assertThat(scopes.get(0), is(sameInstance(scope1)));
    assertThat(scopes.get(1), is(sameInstance(scope0)));
  }


  @Test(expected = RuntimeException.class)
  public void testGetByTypeWhenNotFound() throws Exception {
    viewContext.get(Object.class);
  }

  @Test(expected = RuntimeException.class)
  public void testGetByNameWhenNotFound() throws Exception {
    viewContext.get("name", Object.class);
  }

  @Test
  public void testGetOptionalByTypeWhenNotFound() throws Exception {
    assertThat(viewContext.getOptional(Object.class), is(nullValue()));
  }

  @Test
  public void testGetOptionalByNameWhenNotFound() throws Exception {
    assertThat(viewContext.getOptional("name", Object.class), is(nullValue()));
  }

  @Test
  public void testScopesPutAndGetByType() throws Exception {
    final MockScope0Type scope0Mock = new MockScope0Type() {};

    final MutableScope scope0 = viewContext.newScope();
    viewContext.getScopes().append(scope0);
    scope0.put(SCOPE0);
    scope0.put(scope0Mock);
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    final MockScope1Type scope1Mock = new MockScope1Type() {};

    viewContext.push(SCOPE1, null);
    viewContext.put(SCOPE1);
    viewContext.put(scope1Mock);
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(viewContext.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    final MockScope2Type scope2Mock = new MockScope2Type() {};

    viewContext.push(SCOPE2, null);
    viewContext.put(SCOPE2);
    viewContext.put(scope2Mock);
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE2)));
    assertThat(viewContext.get(MockScope2Type.class), is(sameInstance(scope2Mock)));
    assertThat(viewContext.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    viewContext.pop();
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(viewContext.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));

    viewContext.pop();
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
  }

  @Test
  public void testScopesPutAndGetByName() throws Exception {
    final MockScope0Type scope0Mock = new MockScope0Type() {};

    final MutableScope scope0 = viewContext.newScope();
    viewContext.getScopes().append(scope0);
    scope0.put(SCOPE0, SCOPE0);
    scope0.put(SCOPE0_MOCK, scope0Mock);
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(viewContext.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    final MockScope1Type scope1Mock = new MockScope1Type() {};

    viewContext.push(SCOPE1, null);
    viewContext.put(SCOPE1, SCOPE1);
    viewContext.put(SCOPE1_MOCK, scope1Mock);
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(viewContext.get(SCOPE1, String.class), is(sameInstance(SCOPE1)));
    assertThat(viewContext.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(SCOPE1_MOCK, MockScope1Type.class),
        is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(viewContext.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    final MockScope2Type scope2Mock = new MockScope2Type() {};

    viewContext.push(SCOPE2, null);
    viewContext.put(SCOPE2, SCOPE2);
    viewContext.put(SCOPE2_MOCK, scope2Mock);
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE2)));
    assertThat(viewContext.get(SCOPE2, String.class), is(sameInstance(SCOPE2)));
    assertThat(viewContext.get(MockScope2Type.class), is(sameInstance(scope2Mock)));
    assertThat(viewContext.get(SCOPE2_MOCK, MockScope2Type.class),
        is(sameInstance(scope2Mock)));
    assertThat(viewContext.get(SCOPE1, String.class), is(sameInstance(SCOPE1)));
    assertThat(viewContext.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(SCOPE1_MOCK, MockScope1Type.class),
        is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(viewContext.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    viewContext.pop();
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE1)));
    assertThat(viewContext.get(SCOPE1, String.class), is(sameInstance(SCOPE1)));
    assertThat(viewContext.get(MockScope1Type.class), is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(SCOPE1_MOCK, MockScope1Type.class),
        is(sameInstance(scope1Mock)));
    assertThat(viewContext.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(viewContext.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));

    viewContext.pop();
    assertThat(viewContext.get(String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(SCOPE0, String.class), is(sameInstance(SCOPE0)));
    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(scope0Mock)));
    assertThat(viewContext.get(SCOPE0_MOCK, MockScope0Type.class),
        is(sameInstance(scope0Mock)));
  }

  @Test
  public void testCurrentViewPath() throws Exception {
    assertThat(viewContext.currentViewPath(), is(empty()));
    viewContext.push(SCOPE0, null);
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0))));

    viewContext.push(SCOPE1, null);
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, SCOPE1))));

    viewContext.push(SCOPE2, null);
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, SCOPE1, SCOPE2))));

    viewContext.pop();
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, SCOPE1))));

    viewContext.pop();
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0))));

    viewContext.pop();
    assertThat(viewContext.currentViewPath(), is(empty()));
  }

  @Test
  public void testCurrentModelPathWithUnnamedNode() throws Exception {
    viewContext.push(SCOPE0, null);
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0))));

    viewContext.push(null, null);
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0))));

    viewContext.push(SCOPE2, null);
    assertThat(viewContext.currentViewPath(),
        is(equalTo(Arrays.asList(SCOPE0, SCOPE2))));
  }

  @Test
  public void testCurrentViewPathAsString() throws Exception {
    assertThat(viewContext.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + "")));

    viewContext.push(SCOPE0, null);
    assertThat(viewContext.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0)));

    viewContext.push(SCOPE1, null);
    assertThat(viewContext.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0
            + ViewContext.PATH_DELIMITER + SCOPE1)));

    viewContext.push(SCOPE2, null);
    assertThat(viewContext.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0
            + ViewContext.PATH_DELIMITER + SCOPE1
            + ViewContext.PATH_DELIMITER + SCOPE2)));
  }


  @Test
  public void testCurrentViewPathAsStringWithUnnamedNode() throws Exception {
    viewContext.push(SCOPE0, null);
    assertThat(viewContext.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0)));

    viewContext.push(null, null);
    assertThat(viewContext.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0)));

    viewContext.push(SCOPE2, null);
    assertThat(viewContext.currentViewPathAsString(),
        is(equalTo(ViewContext.PATH_DELIMITER + SCOPE0
            + ViewContext.PATH_DELIMITER + SCOPE2)));
  }

  @Test
  public void testCurrentModelPath() throws Exception {
    assertThat(viewContext.currentModelPath(), is(empty()));

    viewContext.push(null, MockScope0Type.class);
    assertThat(viewContext.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    viewContext.push(null, null);
    assertThat(viewContext.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    viewContext.push(null, MockScope2Type.class);
    assertThat(viewContext.currentModelPath(),
        is(equalTo(Arrays.asList(MockScope0Type.class,
            MockScope2Type.class))));

    viewContext.pop();
    assertThat(viewContext.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    viewContext.pop();
    assertThat(viewContext.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

    viewContext.pop();
    assertThat(viewContext.currentModelPath(), is(empty()));
  }

  @Test
  public void testPushFrame() throws Exception {
    final MockScope0Type model = new MockScope0Type() {};

    viewContext.pushFrame(MockScope0Type.class, model);
    assertThat(viewContext.currentModelPath(), is(not(empty())));

    viewContext.push(null, MockScope1Type.class);
    assertThat(viewContext.currentModelPath(),
        is(equalTo(Arrays.asList(MockScope0Type.class,
            MockScope1Type.class))));

    assertThat(viewContext.get(MockScope0Type.class), is(sameInstance(model)));

    viewContext.pop();
    assertThat(viewContext.currentModelPath(),
        is(equalTo(Arrays.<Class<?>>asList(MockScope0Type.class))));

  }

  @Test
  public void testCopy() throws Exception {
    final Scope scope =
        context.mock(Scope.class);
    final ViewListener listener =
        context.mock(ViewListener.class);
    final ValueTypeConverter valueTypeConverter =
        context.mock(ValueTypeConverter.class);
    final ReferenceResolver resolver =
        context.mock(ReferenceResolver.class);
    final AssociationManager associationManager =
        context.mock(AssociationManager.class);
    final ObjectFactory objectFactory =
        context.mock(ObjectFactory.class);


    viewContext.getScopes().append(scope);
    viewContext.getListeners().append(listener);
    viewContext.getValueTypeConverters().append(valueTypeConverter);
    viewContext.getReferenceResolvers().append(resolver);
    viewContext.getAssociationManagers().append(associationManager);
    viewContext.getObjectFactories().append(objectFactory);

    ViewContext contextCopy = new ConcreteViewContext(viewContext);
    viewContext.getScopes().toList().clear();
    viewContext.getListeners().toList().clear();
    viewContext.getValueTypeConverters().toList().clear();
    viewContext.getReferenceResolvers().toList().clear();
    viewContext.getAssociationManagers().toList().clear();
    viewContext.getObjectFactories().toList().clear();

    assertThat(contextCopy.getScopes().toList(), contains(scope));
    assertThat(contextCopy.getListeners().toList(), contains(listener));
    assertThat(contextCopy.getValueTypeConverters().toList(),
        contains(valueTypeConverter));
    assertThat(contextCopy.getReferenceResolvers().toList(),
        contains(resolver));
    assertThat(contextCopy.getAssociationManagers().toList(),
        contains(associationManager));
    assertThat(contextCopy.getObjectFactories().toList(),
        contains(objectFactory));
    assertThat(contextCopy.getOptions(), is(sameInstance(options)));
  }

}
