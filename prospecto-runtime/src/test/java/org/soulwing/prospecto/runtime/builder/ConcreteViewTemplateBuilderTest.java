/*
 * File created on Mar 15, 2016
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
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.arrayViewNode;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.viewNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.Convertible;
import org.soulwing.prospecto.runtime.injector.BeanFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.node.DiscriminatorNode;
import org.soulwing.prospecto.runtime.node.EnvelopeNode;
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.runtime.node.SubtypeNode;
import org.soulwing.prospecto.runtime.node.UrlNode;
import org.soulwing.prospecto.runtime.node.ValueNode;
import org.soulwing.prospecto.runtime.testing.JUnitRuleClassImposterizingMockery;

/**
 * Units tests for {@link ConcreteViewTemplateBuilder}.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilderTest {

  private static final String NAME = "name";
  private static final String ELEMENT_NAME = "elementName";
  private static final String NAMESPACE = "namespace";
  private static final Class<?> MODEL_TYPE = Object.class;
  private static final String MODEL_NAME = "modelName";
  private static final AccessType ACCESS_TYPE = AccessType.PROPERTY;

  private static final Class<DiscriminatorStrategy> DISCRIMINATOR_CLASS =
      DiscriminatorStrategy.class;

  private static final Class<ValueTypeConverter> CONVERTER_CLASS =
      ValueTypeConverter.class;

  @Rule
  public final JUnitRuleMockery context =
      new JUnitRuleClassImposterizingMockery();

  @Mock
  private BeanFactory beanFactory;

  @Mock
  private ViewTemplateBuilderFactory builderFactory;

  @Mock
  private ContainerViewNode target;

  @Mock
  private ComposableViewTemplate template;

  @Mock
  private AbstractViewNode templateNode;

  @Mock
  private Cursor cursor;

  @Mock
  private ValueTypeConverter<?> converter;

  @Mock
  private DiscriminatorStrategy discriminatorStrategy;

  @Mock
  private ViewTemplateBuilder childBuilder;

  private ConcreteViewTemplateBuilder builder;

  @Before
  public void setUp() throws Exception {
    builder = new ConcreteViewTemplateBuilder(null, cursor, target, beanFactory,
        builderFactory);
  }

  @Test
  public void testValueName() throws Exception {
    context.checking(valueExpectations(NAME, null));
    assertThat(builder.value(NAME), is(sameInstance((Object) builder)));
  }

  @Test
  public void testValueNameNamespace() throws Exception {
    context.checking(valueExpectations(NAME, NAMESPACE));
    assertThat(builder.value(NAME, NAMESPACE),
        is(sameInstance((Object) builder)));
  }

  private Expectations valueExpectations(final String name,
      final String namespace) {
    return new Expectations() {
      {
        oneOf(target).addChild(with(viewNode(ValueNode.class, name, namespace)));
        oneOf(cursor).advance(with(viewNode(ValueNode.class, name, namespace)));
      }
    };
  }

  @Test
  public void testArrayOfValuesName() throws Exception {
    context.checking(arrayOfValuesExpectations(NAME, null, null));
    assertThat(builder.arrayOfValues(NAME), is(sameInstance((Object) builder)));
  }

  @Test
  public void testArrayOfValuesNameElementName() throws Exception {
    context.checking(arrayOfValuesExpectations(NAME, ELEMENT_NAME, null));
    assertThat(builder.arrayOfValues(NAME, ELEMENT_NAME),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testArrayOfValuesNameElementNameNamespace() throws Exception {
    context.checking(arrayOfValuesExpectations(NAME, ELEMENT_NAME, NAMESPACE));
    assertThat(builder.arrayOfValues(NAME, ELEMENT_NAME, NAMESPACE),
        is(sameInstance((Object) builder)));
  }

  private Expectations arrayOfValuesExpectations(final String name,
      final String elementName, final String namespace) {
    return new Expectations() {
      {
        oneOf(target).addChild(with(arrayViewNode(
            ArrayOfValueNode.class, name, elementName, namespace)));
        oneOf(cursor).advance(with(arrayViewNode(
            ArrayOfValueNode.class, name, elementName, namespace)));
      }
    };
  }

  @Test
  public void testObjectName() throws Exception {
    context.checking(objectExpectations(NAME, null));
    assertThat(builder.object(NAME, MODEL_TYPE),
        is(sameInstance(childBuilder)));
  }

  @Test
  public void testObjectNameNamespace() throws Exception {
    context.checking(objectExpectations(NAME, NAMESPACE));
    assertThat(builder.object(NAME, NAMESPACE, MODEL_TYPE),
        is(sameInstance(childBuilder)));
  }

  private Expectations objectExpectations(final String name,
      final String namespace) {
    return new Expectations() {
      {
        oneOf(target).addChild(with(viewNode(
            ObjectNode.class, name, namespace, MODEL_TYPE)));
        oneOf(cursor).advance(with(viewNode(
            ObjectNode.class, name, namespace, MODEL_TYPE)));
        oneOf(cursor).copy(MODEL_TYPE);
        will(returnValue(cursor));
        oneOf(builderFactory).newBuilder(
            with(builder),
            with(cursor),
            with(viewNode(ObjectNode.class, name, namespace, MODEL_TYPE)));
        will(returnValue(childBuilder));
      }
    };
  }

  @Test
  public void testObjectNameTemplate() throws Exception {
    context.checking(objectTemplateExpectations(NAME, null));
    assertThat(builder.object(NAME, template),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testObjectNameNamespaceTemplate() throws Exception {
    context.checking(objectTemplateExpectations(NAME, NAMESPACE));
    assertThat(builder.object(NAME, NAMESPACE, template),
        is(sameInstance((Object) builder)));
  }

  private Expectations objectTemplateExpectations(final String name,
      final String namespace) {
    return new Expectations() {
      {
        oneOf(template).object(name, namespace);
        will(returnValue(templateNode));
        oneOf(target).addChild(templateNode);
        oneOf(cursor).advance(templateNode);
      }
    };
  }

  @Test
  public void testArrayOfObjectsName() throws Exception {
    context.checking(arrayOfObjectsExpectations(NAME, null, null));
    assertThat(builder.arrayOfObjects(NAME, MODEL_TYPE),
        is(sameInstance(childBuilder)));
  }

  @Test
  public void testArrayOfObjectsNameElementName() throws Exception {
    context.checking(arrayOfObjectsExpectations(NAME, ELEMENT_NAME, null));
    assertThat(builder.arrayOfObjects(NAME, ELEMENT_NAME, MODEL_TYPE),
        is(sameInstance(childBuilder)));
  }

  @Test
  public void testArrayOfObjectsNameElementNameNamespace() throws Exception {
    context.checking(arrayOfObjectsExpectations(NAME, ELEMENT_NAME, NAMESPACE));
    assertThat(builder.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE, MODEL_TYPE),
        is(sameInstance(childBuilder)));
  }

  private Expectations arrayOfObjectsExpectations(final String name,
      final String elementName, final String namespace) {
    return new Expectations() {
      {
        oneOf(target).addChild(with(arrayViewNode(
            ArrayOfObjectNode.class, name, elementName, namespace, MODEL_TYPE)));
        oneOf(cursor).advance(with(arrayViewNode(
            ArrayOfObjectNode.class, name, elementName, namespace, MODEL_TYPE)));
        oneOf(cursor).copy(MODEL_TYPE);
        will(returnValue(cursor));
        oneOf(builderFactory).newBuilder(
            with(builder),
            with(cursor),
            with(arrayViewNode(ArrayOfObjectNode.class, name, elementName,
                namespace, MODEL_TYPE)));
        will(returnValue(childBuilder));
      }
    };
  }

  @Test
  public void testArrayOfObjectsNameTemplate() throws Exception {
    context.checking(arrayOfObjectsTemplateExpectations(NAME, null, null));
    assertThat(builder.arrayOfObjects(NAME, template),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testArrayOfObjectsNameElementNameTemplate() throws Exception {
    context.checking(arrayOfObjectsTemplateExpectations(NAME, ELEMENT_NAME,
        null));
    assertThat(builder.arrayOfObjects(NAME, ELEMENT_NAME, template),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testArrayOfObjectsNameElementNameNamespaceTemplate()
      throws Exception {
    context.checking(arrayOfObjectsTemplateExpectations(NAME, ELEMENT_NAME,
        NAMESPACE));
    assertThat(builder.arrayOfObjects(NAME, ELEMENT_NAME, NAMESPACE, template),
        is(sameInstance((Object) builder)));
  }

  private Expectations arrayOfObjectsTemplateExpectations(final String name,
      final String elementName, final String namespace) {
    return new Expectations() {
      {
        oneOf(template).arrayOfObjects(name, elementName, namespace);
        will(returnValue(templateNode));
        oneOf(target).addChild(templateNode);
        oneOf(cursor).advance(templateNode);
      }
    };
  }

  @Test
  public void testSubtype() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(cursor).getModelType();
        will(returnValue(MODEL_TYPE));
        oneOf(target).addChild(
            with(viewNode(SubtypeNode.class, null, null, MODEL_TYPE)));
        oneOf(cursor).copy(MODEL_TYPE);
        will(returnValue(cursor));
        oneOf(builderFactory).newBuilder(with(builder), with(cursor),
            with(viewNode(SubtypeNode.class, null, null, MODEL_TYPE)));
        will(returnValue(childBuilder));
      }
    });
    assertThat(builder.subtype(MODEL_TYPE), is(sameInstance(childBuilder)));
  }

  @Test(expected =  ViewTemplateException.class)
  public void testSubtypeWhenNotSubtype() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(cursor).getModelType();
        will(returnValue(String.class));
      }
    });

    builder.subtype(Object.class);
  }

  @Test
  public void testEnvelopeName() throws Exception {
    context.checking(envelopeExpectations(NAME, null));
    assertThat(builder.envelope(NAME), is(sameInstance(childBuilder)));
  }

  @Test
  public void testEnvelopeNameNamespace() throws Exception {
    context.checking(envelopeExpectations(NAME, NAMESPACE));
    assertThat(builder.envelope(NAME, NAMESPACE),
        is(sameInstance(childBuilder)));
  }

  private Expectations envelopeExpectations(final String name,
      final String namespace) {
    return new Expectations() {
      {
        oneOf(target).addChild(
            with(viewNode(EnvelopeNode.class, name, namespace)));
        oneOf(cursor).getModelName();
        will(returnValue(MODEL_NAME));
        oneOf(cursor).advance(
            with(viewNode(
                EnvelopeNode.class, name, namespace)),
            with(MODEL_NAME));
        oneOf(builderFactory).newBuilder(with(builder), with(cursor),
            with(viewNode(EnvelopeNode.class, name, namespace)));
        will(returnValue(childBuilder));
      }
    };
  }

  @Test
  public void testDiscriminatorStrategyClassArray() throws Exception {
    final Object[] args = new Object[0];

    context.checking(new Expectations() {
      {
        oneOf(beanFactory).construct(DISCRIMINATOR_CLASS, args);
        will(returnValue(discriminatorStrategy));
      }
    });

    context.checking(discriminatorExpectations());
    assertThat(builder.discriminator(DISCRIMINATOR_CLASS, args),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testDiscriminatorStrategyClassMap() throws Exception {
    final Map args = new HashMap();

    context.checking(new Expectations() {
      {
        oneOf(beanFactory).construct(DISCRIMINATOR_CLASS, args);
        will(returnValue(discriminatorStrategy));
      }
    });

    context.checking(discriminatorExpectations());
    assertThat(builder.discriminator(DISCRIMINATOR_CLASS, args),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testDiscriminatorStrategy() throws Exception {
    context.checking(discriminatorExpectations());
    assertThat(builder.discriminator(discriminatorStrategy),
        is(sameInstance((Object) builder)));
  }

  private Expectations discriminatorExpectations()
      throws Exception {
    return new Expectations() {
      {
        oneOf(target).addChild(
            with(viewNode(DiscriminatorNode.class, null, null)));
        oneOf(cursor).getModelType();
        will(returnValue(MODEL_TYPE));
        oneOf(cursor).advance(
            with(viewNode(DiscriminatorNode.class, null, null)),
            with(nullValue(String.class)));

      }
    };
  }

  @Test
  public void testUrlName() throws Exception {
    context.checking(urlExpectations(NAME, null));
    assertThat(builder.url(NAME), is(sameInstance((Object) builder)));
  }

  @Test
  public void testUrlNameNamespace() throws Exception {
    context.checking(urlExpectations(NAME, NAMESPACE));
    assertThat(builder.url(NAME, NAMESPACE),
        is(sameInstance((Object) builder)));
  }

  private Expectations urlExpectations(final String name,
      final String namespace) {
    return new Expectations() {
      {
        oneOf(target).addChild(
            with(viewNode(UrlNode.class, name, namespace)));
        oneOf(cursor).advance(with(viewNode(UrlNode.class, name, namespace)),
            with(nullValue(String.class)));
      }
    };
  }

  @Test
  public void testSource() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(cursor).setModelName(MODEL_NAME);
      }
    });

    assertThat(builder.source(MODEL_NAME), is(sameInstance((Object) builder)));
  }

  @Test
  public void testAccessType() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(cursor).setAccessType(ACCESS_TYPE);
      }
    });

    assertThat(builder.accessType(ACCESS_TYPE),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testConverterClassArray() throws Exception {
    final Object[] args = new Object[0];

    context.checking(new Expectations() {
      {
        oneOf(beanFactory).construct(CONVERTER_CLASS, args);
        will(returnValue(converter));
      }
    });

    context.checking(converterExpectations());

    assertThat(builder.converter(CONVERTER_CLASS, args),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testConverterClassMap() throws Exception {
    final Map map = new HashMap();

    context.checking(new Expectations() {
      {
        oneOf(beanFactory).construct(CONVERTER_CLASS, map);
        will(returnValue(converter));
      }
    });

    context.checking(converterExpectations());

    assertThat(builder.converter(CONVERTER_CLASS, map),
        is(sameInstance((Object) builder)));
  }


  @Test
  public void testConverter() throws Exception {
    context.checking(converterExpectations());
    assertThat(builder.converter(converter),
        is(sameInstance((Object) builder)));
  }

  @Test(expected = ViewTemplateException.class)
  public void testConverterWithNonConvertibleNode() throws Exception {
    final AbstractViewNode node = context.mock(AbstractViewNode.class);
    context.checking(new Expectations() {
      {
        oneOf(cursor).getNode();
        will(returnValue(node));
        oneOf(node).getName();
        will(returnValue(NAME));
      }
    });

    builder.converter(converter);
  }

  private Expectations converterExpectations() throws Exception {
    final MockConvertibleViewNode convertibleViewNode =
        context.mock(MockConvertibleViewNode.class);
    return new Expectations() {
      {
        oneOf(cursor).getNode();
        will(returnValue(convertibleViewNode));
        oneOf(convertibleViewNode).put(with(converter));
      }
    };
  }

  @Test
  public void testAttributeValue() throws Exception {
    final AbstractViewNode cursorNode = context.mock(AbstractViewNode.class);
    final Object value = new Object();
    context.checking(new Expectations() {
      {
        oneOf(cursor).getNode();
        will(returnValue(cursorNode));
        oneOf(cursorNode).put(value);
      }
    });

    assertThat(builder.attribute(value), is(sameInstance((Object) builder)));
  }

  @Test
  public void testAttributeNameValue() throws Exception {
    final AbstractViewNode cursorNode = context.mock(AbstractViewNode.class);
    final String name = "name";
    final Object value = new Object();
    context.checking(new Expectations() {
      {
        oneOf(cursor).getNode();
        will(returnValue(cursorNode));
        oneOf(cursorNode).put(name, value);
      }
    });

    assertThat(builder.attribute(name, value),
        is(sameInstance((Object) builder)));
  }

  @Test
  public void testEndRoot() throws Exception {
    assertThat(builder.end(), is(sameInstance((Object) builder)));
  }

  @Test
  public void testEndChild() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(cursor).advance();
      }
    });

    final ConcreteViewTemplateBuilder child = new ConcreteViewTemplateBuilder
        (builder, cursor, target, beanFactory, builderFactory);

    assertThat(child.end(), is(sameInstance((Object) builder)));
  }

  @Test
  public void testBuild() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(cursor).advance();
      }
    });

    assertThat(builder.build(), hasProperty("root", sameInstance(target)));
  }

  static class MockConvertibleViewNode extends AbstractViewNode
      implements Convertible {

    protected MockConvertibleViewNode() {
      super(null, null, null);
    }

    @Override
    protected List<View.Event> onEvaluate(Object source,
        ScopedViewContext context) throws Exception {
      return null;
    }
  }


}
