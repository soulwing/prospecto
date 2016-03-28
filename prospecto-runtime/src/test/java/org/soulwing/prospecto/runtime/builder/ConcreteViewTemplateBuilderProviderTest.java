/*
 * File created on Mar 14, 2016
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
package org.soulwing.prospecto.runtime.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.arrayViewNode;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.viewNode;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.runtime.node.RootArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.RootObjectNode;
import org.soulwing.prospecto.runtime.node.ValueNode;

/**
 * Tests for {@link ConcreteViewTemplateBuilderProvider}.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilderProviderTest {

  private static final String NAME = "name";
  private static final String ELEMENT_NAME = "elementName";
  private static final String NAMESPACE = "namespace";
  private static final Class<?> MODEL_TYPE = Object.class;

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ViewTemplateBuilderFactory builderFactory;

  @Mock
  private ViewTemplateBuilder builder;

  private ConcreteViewTemplateBuilderProvider provider;

  @Before
  public void setUp() throws Exception {
    provider = new ConcreteViewTemplateBuilderProvider(builderFactory);
  }

  @Test
  public void testObject() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(builderFactory).newBuilder(with(
            viewNode(ObjectNode.class, NAME, NAMESPACE)));
        will(returnValue(builder));
      }
    });

    assertThat(provider.object(NAME, NAMESPACE, MODEL_TYPE),
        is(sameInstance(builder)));
  }

  @Test
  public void testArrayOfObjects() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(builderFactory).newBuilder(with(
            arrayViewNode(ArrayOfObjectNode.class, NAME, ELEMENT_NAME,
                NAMESPACE)));
        will(returnValue(builder));
      }
    });

    assertThat(provider.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE,
        MODEL_TYPE), is(sameInstance(builder)));
  }

  @Test
  public void testArrayOfValuesTemplate() throws Exception {
    final ConcreteViewTemplate template = (ConcreteViewTemplate)
        provider.arrayOfValues(NAME, ELEMENT_NAME, NAMESPACE);
    assertThat((ArrayOfValueNode) template.getRoot(), is(
        arrayViewNode(ArrayOfValueNode.class, NAME, ELEMENT_NAME, NAMESPACE)));
  }

  @Test
  public void testArrayOfObjectsTemplate() throws Exception {
    final RootObjectNode templateRoot =
        new RootObjectNode(NAME, NAMESPACE, MODEL_TYPE);

    final ValueNode child = new ValueNode(NAME, NAMESPACE);
    templateRoot.addChild(child);

    final ConcreteViewTemplate template = (ConcreteViewTemplate)
        provider.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE,
            new ConcreteViewTemplate(templateRoot));

    assertThat((ArrayOfObjectNode) template.getRoot(), is(
        arrayViewNode(ArrayOfObjectNode.class, NAME, ELEMENT_NAME, NAMESPACE)));
    assertThat(((ArrayOfObjectNode) template.getRoot()).getChildren()
        .contains(child), is(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayOfObjectsTemplateWithUnrecognizedImpl() throws Exception {
    final ViewTemplate template = context.mock(ViewTemplate.class);
    provider.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE, template);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrayOfObjectsTemplateWithNonObjectNode() throws Exception {
    final ConcreteViewTemplate template = new ConcreteViewTemplate(
        new RootArrayOfObjectNode(null, null, null, null));
    provider.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE, template);
  }

}
