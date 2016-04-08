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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.testing.JUnitRuleClassImposterizingMockery;

/**
 * Unit tests for {@link HierarchicalContainerApplicatorLocator}.
 *
 * @author Carl Harris
 */
public class HiearchicalContainerApplicatorLocatorTest {

  private static final String NAME = "name";

  private static final String OTHER_NAME = "otherName";

  private static final HierarchicalContainerApplicatorLocator locator =
      HierarchicalContainerApplicatorLocator.INSTANCE;

  @Rule
  public final JUnitRuleMockery context =
      new JUnitRuleClassImposterizingMockery();

  @Mock
  private ContainerApplicator container;

  @Mock
  private ViewEventApplicator child;

  @Mock
  private ViewNode childNode;

  @Mock
  private ViewEventApplicator grandchild;

  @Mock
  private ViewNode grandchildNode;

  @Mock
  private SubtypeApplicator subtypeContainer;

  @Mock
  private ViewNode subtypeNode;

  private List<ViewEventApplicator> children = new LinkedList<>();

  private List<ViewEventApplicator> grandchildren = new LinkedList<>();

  @Before
  public void setUp() throws Exception {
    children.addAll(Arrays.asList(subtypeContainer, child));
    grandchildren.add(grandchild);
  }

  @Test
  public void testFoundInRootContainer() throws Exception {
    context.checking(rootContainerExpectations());
    context.checking(subtypeContainerExpectations());
    assertThat(locator.findApplicator(NAME, Base.class, container),
        is(sameInstance(child)));
  }

  @Test
  public void testFoundInRootContainerUsingSubtype() throws Exception {
    context.checking(rootContainerExpectations());
    context.checking(subtypeContainerExpectations());
    assertThat(locator.findApplicator(NAME, B.class, container),
        is(sameInstance(child)));
  }

  @Test
  public void testNotFound() throws Exception {
    context.checking(rootContainerExpectations());
    context.checking(subtypeContainerExpectations());
    assertThat(locator.findApplicator(OTHER_NAME, Base.class, container),
        is(nullValue()));
  }

  @Test
  public void testFoundInSubtypeContainer() throws Exception {
    context.checking(rootContainerExpectations());
    context.checking(subtypeContainerExpectations());
    assertThat(locator.findApplicator(NAME, A.class, container),
        is(grandchild));
  }

  private Expectations rootContainerExpectations() throws Exception {
    return new Expectations() {
      {
        allowing(container).getChildren();
        will(returnValue(children));
        allowing(child).getNode();
        will(returnValue(childNode));
        allowing(childNode).getName();
        will(returnValue(NAME));
      }
    };
  }


  private Expectations subtypeContainerExpectations() {
    return new Expectations() {
      {
        allowing(subtypeContainer).getChildren();
        will(returnValue(grandchildren));
        allowing(subtypeContainer).getNode();
        will(returnValue(subtypeNode));
        allowing(subtypeNode).getModelType();
        will(returnValue(A.class));
        allowing(grandchild).getNode();
        will(returnValue(grandchildNode));
        allowing(grandchildNode).getName();
        will(returnValue(NAME));
        allowing(subtypeNode).getName();
        will(returnValue(null));
      }
    };
  }

  private interface Base {}

  private interface A extends Base {}

  private interface B extends Base {}

}
