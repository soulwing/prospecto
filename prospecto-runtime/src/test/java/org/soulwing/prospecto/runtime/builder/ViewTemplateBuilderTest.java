/*
 * File created on Mar 28, 2016
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.runtime.accessor.AccessorMatchers.onModelType;
import static org.soulwing.prospecto.runtime.accessor.AccessorMatchers.propertyNamed;
import static org.soulwing.prospecto.runtime.accessor.AccessorMatchers.usingAccessType;
import static org.soulwing.prospecto.runtime.accessor.AccessorMatchers.withModes;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.accessing;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.accessingNothing;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.containing;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.containingNothing;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.elementsNamed;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.forModelType;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.havingAttribute;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.inNamespace;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.named;
import static org.soulwing.prospecto.runtime.builder.ViewNodeMatchers.nodeOfType;

import java.util.Collections;

import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.runtime.accessor.RootAccessor;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;
import org.soulwing.prospecto.runtime.node.ConcreteArrayOfObjectsNode;
import org.soulwing.prospecto.runtime.node.ConcreteArrayOfValuesNode;
import org.soulwing.prospecto.runtime.node.ConcreteEnvelopeNode;
import org.soulwing.prospecto.runtime.node.ConcreteObjectNode;
import org.soulwing.prospecto.runtime.node.ConcreteSubtypeNode;
import org.soulwing.prospecto.runtime.node.ConcreteUrlNode;
import org.soulwing.prospecto.runtime.node.ConcreteValueNode;
import org.soulwing.prospecto.runtime.node.RootArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.RootObjectNode;
import org.soulwing.prospecto.runtime.template.ConcreteViewTemplate;

/**
 * Unit tests for the template builder.
 *
 * @author Carl Harris
 */
@SuppressWarnings("unchecked")
public class ViewTemplateBuilderTest {

  private static final String VIEW_NAME = "viewName";
  private static final String NAMESPACE = "namespace";
  private static final String MOCK_FIELD = "mockField";
  private static final String OTHER_MOCK_FIELD = "otherMockField";
  private static final String MOCK_PROPERTY = "mockProperty";
  private static final String OTHER_MOCK_PROPERTY = "otherMockProperty";
  private static final String MOCK_SUB_PROPERTY = "subProperty";
  private static final String MOCK_SUB_SUB_PROPERTY = "subSubProperty";
  private static final String MOCK_OTHER_SUB_PROPERTY = "otherSubProperty";
  private static final String VALUE_NAME = "valueName";
  private static final String MOCK_PROPERTY_VALUE = "mockValue";
  private static final String MOCK_ARRAY = "mockArray";
  private static final String MOCK_COLLECTION = "mockCollection";
  private static final String ELEMENT_NAME = "elementName";
  private static final String URL_NAME = "urlName";
  private static final String ENVELOPE_NAME = "envelopeName";

  private static final String ATTRIBUTE_NAME = "attributeName";
  private static final Object ATTRIBUTE_VALUE = new Object() { };

  @Test
  public void testRootObject() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            forModelType(MockModel.class),
            accessing(sameInstance(RootAccessor.INSTANCE)),
            containingNothing()
        )
    ));
  }

  @Test
  public void testArrayOfObjects() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.arrayOfObjects(VIEW_NAME, ELEMENT_NAME,
            NAMESPACE, MockModel.class)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootArrayOfObjectNode.class,
            named(VIEW_NAME),
            inNamespace(NAMESPACE),
            elementsNamed(ELEMENT_NAME),
            accessingNothing(),
            containingNothing()
        )
    ));
  }

  @Test(expected = ViewTemplateException.class)
  public void testRootSource() throws Exception {
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .source("some source")
        .build();
  }

  @Test(expected = ViewTemplateException.class)
  public void testRootAccessModes() throws Exception {
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .allow(AccessMode.READ)
        .build();
  }

  @Test(expected = ViewTemplateException.class)
  public void testRootConverter() throws Exception {
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .converter(new MockConverter())
        .build();
  }

  @Test
  public void testRootAttribute() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .attribute(ATTRIBUTE_VALUE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            forModelType(MockModel.class),
            accessing(sameInstance(RootAccessor.INSTANCE)),
            containingNothing(),
            havingAttribute(ATTRIBUTE_VALUE.getClass(),
                sameInstance(ATTRIBUTE_VALUE))
        )
    ));
  }

  @Test
  public void testRootNamedAttribute() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .attribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            forModelType(MockModel.class),
            accessing(sameInstance(RootAccessor.INSTANCE)),
            containingNothing(),
            havingAttribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE.getClass(),
                sameInstance(ATTRIBUTE_VALUE))
        )
    ));
  }


  @Test
  public void testRootDiscriminator() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .discriminator()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            havingAttribute(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY,
                Boolean.class, equalTo(true))
        )
    ));

  }

  @Test
  public void testRootDiscriminatorStrategy() throws Exception {
    final MockDiscriminatorStrategy strategy = new MockDiscriminatorStrategy();
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .discriminator(strategy)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            havingAttribute(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY,
                Boolean.class, equalTo(true)),
            havingAttribute(DiscriminatorStrategy.class,
                sameInstance(strategy))
        )
    ));

  }

  @Test
  public void testRootDiscriminatorStrategyClass() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .discriminator(MockDiscriminatorStrategy.class, MOCK_PROPERTY,
                MOCK_PROPERTY_VALUE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            havingAttribute(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY,
                Boolean.class, equalTo(true)),
            havingAttribute(DiscriminatorStrategy.class,
                hasProperty(MOCK_PROPERTY, equalTo(MOCK_PROPERTY_VALUE)))
        )
    ));

  }

  @Test
  public void testRootDiscriminatorStrategyMap() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .discriminator(MockDiscriminatorStrategy.class,
                Collections.singletonMap(MOCK_PROPERTY, MOCK_PROPERTY_VALUE))
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            havingAttribute(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY,
                Boolean.class, equalTo(true)),
            havingAttribute(DiscriminatorStrategy.class,
                hasProperty(MOCK_PROPERTY, equalTo(MOCK_PROPERTY_VALUE)))
        )
    ));

  }

  @Test(expected = ViewTemplateException.class)
  public void testRootDiscriminatorDiscriminator() throws Exception {
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .discriminator()
        .discriminator()
        .build();
  }


  @Test
  public void testObjectValue() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY)
                    )
                )
            )
        )
    ));

  }

  @Test
  public void testObjectValueNamespace() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY, NAMESPACE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inNamespace(NAMESPACE),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY)
                    )
                )
            )
        )
    ));

  }
  
  @Test
  public void testObjectValueEnd() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueValue() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
            .value(OTHER_MOCK_PROPERTY)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY)
                    )
                ),
                nodeOfType(ConcreteValueNode.class,
                    named(OTHER_MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(OTHER_MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY)
                    )
                )
            )
        )
    ));

  }

  @Test
  public void testObjectValueSource() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(VALUE_NAME)
                .source(MOCK_PROPERTY)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(VALUE_NAME), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueConverter() throws Exception {
    final MockConverter converter = new MockConverter();
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
                .converter(converter)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    ),
                    havingAttribute(MockConverter.class,
                        sameInstance(converter))
                )
            )
        )
    ));
  }

  @Test(expected = ViewTemplateException.class)
  public void testObjectValueConverterConverter() throws Exception {
    final MockConverter converter = new MockConverter();
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .value(MOCK_PROPERTY)
            .converter(converter)
            .converter(converter)
        .build();
  }

  @Test
  public void testObjectValueConverterClass() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
                .converter(MockConverter.class, MOCK_PROPERTY, MOCK_PROPERTY_VALUE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    ),
                    havingAttribute(MockConverter.class,
                        hasProperty(MOCK_PROPERTY, equalTo(MOCK_PROPERTY_VALUE)))
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueConverterMap() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
            .converter(MockConverter.class,
                Collections.singletonMap(MOCK_PROPERTY, MOCK_PROPERTY_VALUE))
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    ),
                    havingAttribute(MockConverter.class,
                        hasProperty(MOCK_PROPERTY, equalTo(MOCK_PROPERTY_VALUE)))
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueAttribute() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
                .attribute(ATTRIBUTE_VALUE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    ),
                    havingAttribute(ATTRIBUTE_VALUE.getClass(),
                        sameInstance(ATTRIBUTE_VALUE))
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueNamedAttribute() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
                .attribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    ),
                    havingAttribute(ATTRIBUTE_NAME, ATTRIBUTE_VALUE.getClass(),
                        sameInstance(ATTRIBUTE_VALUE))
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValuePropertyReadOnly() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
                .accessType(AccessType.PROPERTY)
                .allow(AccessMode.READ)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY),
                        withModes(AccessMode.READ)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValuePropertyWriteOnly() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_PROPERTY)
            .accessType(AccessType.PROPERTY)
            .allow(AccessMode.WRITE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY),
                        withModes(AccessMode.WRITE)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueField() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_FIELD)
              .accessType(AccessType.FIELD)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME),
            inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_FIELD), inDefaultNamespace(),
                    accessing(
                      propertyNamed(MOCK_FIELD),
                      onModelType(MockModel.class),
                      usingAccessType(AccessType.FIELD)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueFieldReadOnly() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_FIELD)
                .allow(AccessMode.READ)
                .accessType(AccessType.FIELD)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME),
            inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_FIELD), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_FIELD),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.FIELD),
                        withModes(AccessMode.READ)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectValueFieldWriteOnly() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .value(MOCK_FIELD)
                .allow(AccessMode.WRITE)
                .accessType(AccessType.FIELD)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME),
            inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_FIELD), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_FIELD),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.FIELD),
                        withModes(AccessMode.WRITE)
                    )
                )
            )
        )
    ));
  }

  @Test(expected = ViewTemplateException.class)
  public void testObjectValueDiscriminator() throws Exception {
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .value(MOCK_PROPERTY)
            .discriminator()
        .build();
  }

  @Test
  public void testObjectArrayOfValuesArray() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfValues(MOCK_ARRAY, Object.class)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfValuesNode.class,
                    named(MOCK_ARRAY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_ARRAY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectArrayOfValuesCollection() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfValues(MOCK_COLLECTION, Object.class)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfValuesNode.class,
                    named(MOCK_COLLECTION), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_COLLECTION),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectArrayOfValuesElementName() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfValues(MOCK_ARRAY, ELEMENT_NAME, Object.class)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfValuesNode.class,
                    named(MOCK_ARRAY), inDefaultNamespace(),
                    elementsNamed(ELEMENT_NAME),
                    accessing(
                        propertyNamed(MOCK_ARRAY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectArrayOfValuesElementNameNamespace() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfValues(MOCK_ARRAY, ELEMENT_NAME, NAMESPACE, Object.class)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfValuesNode.class,
                    named(MOCK_ARRAY),
                    elementsNamed(ELEMENT_NAME),
                    inNamespace(NAMESPACE),
                    accessing(
                        propertyNamed(MOCK_ARRAY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test(expected = ViewTemplateException.class)
  public void testObjectArrayOfValuesWithIncompatibleProperty() throws Exception {
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .arrayOfValues(MOCK_PROPERTY, Object.class)
        .build();
  }

  @Test
  public void testObjectUrl() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .url()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteUrlNode.class,
                    named(ViewDefaults.URL_NAME), inDefaultNamespace()
                )
            )
        )
    ));
  }

  @Test
  public void testObjectUrlName() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .url(URL_NAME)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteUrlNode.class,
                    named(URL_NAME), inDefaultNamespace()
                )
            )
        )
    ));
  }

  @Test
  public void testObjectUrlNameNamespace() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .url(URL_NAME, NAMESPACE)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteUrlNode.class,
                    named(URL_NAME), inNamespace(NAMESPACE)
                )
            )
        )
    ));
  }

  @Test
  public void testObjectObject() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .object(MOCK_PROPERTY, MockChildModel.class)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteObjectNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                      propertyNamed(MOCK_PROPERTY),
                      onModelType(MockModel.class)
                    ),
                    containingNothing()
                )
            )
        )
    ));

  }

  @Test
  public void testObjectObjectValue() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .object(MOCK_PROPERTY, MockChildModel.class)
                .value(OTHER_MOCK_PROPERTY)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteObjectNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    ),
                    containing(
                      nodeOfType(ConcreteValueNode.class,
                          named(OTHER_MOCK_PROPERTY), inDefaultNamespace(),
                          accessing(
                            propertyNamed(OTHER_MOCK_PROPERTY),
                            onModelType(MockChildModel.class)
                          )
                      )
                    )
                )
            )
        )
    ));

  }

  @Test
  public void testObjectObjectFieldValue() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .object(MOCK_PROPERTY, MockChildModel.class)
                .accessType(AccessType.FIELD)
                .value(OTHER_MOCK_FIELD)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteObjectNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.PROPERTY)
                    ),
                    containing(
                        nodeOfType(ConcreteValueNode.class,
                            named(OTHER_MOCK_FIELD), inDefaultNamespace(),
                            accessing(
                                propertyNamed(OTHER_MOCK_FIELD),
                                onModelType(MockChildModel.class),
                                usingAccessType(AccessType.FIELD)
                            )
                        )
                    )
                )
            )
        )
    ));

  }

  @Test
  public void testObjectFieldObjectFieldValue() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .accessType(AccessType.FIELD)
            .object(MOCK_FIELD, MockChildModel.class)
              .value(OTHER_MOCK_FIELD)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteObjectNode.class,
                    named(MOCK_FIELD), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_FIELD),
                        onModelType(MockModel.class),
                        usingAccessType(AccessType.FIELD)
                    ),
                    containing(
                        nodeOfType(ConcreteValueNode.class,
                            named(OTHER_MOCK_FIELD), inDefaultNamespace(),
                            accessing(
                                propertyNamed(OTHER_MOCK_FIELD),
                                onModelType(MockChildModel.class),
                                usingAccessType(AccessType.FIELD)
                            )
                        )
                    )
                )
            )
        )
    ));

  }

  @Test
  public void testObjectSubtype() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .discriminator()
            .value(MOCK_PROPERTY)
            .subtype(MockSubModel.class)
                .value(MOCK_SUB_PROPERTY)
            .end()
            .subtype(MockOtherSubModel.class)
                .value(MOCK_OTHER_SUB_PROPERTY)
            .end()
            .value(OTHER_MOCK_PROPERTY)
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            forModelType(MockModel.class),
            havingAttribute(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY,
                  Boolean.class, equalTo(true)),
            containing(
                nodeOfType(ConcreteValueNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    )
                ),
                nodeOfType(ConcreteSubtypeNode.class,
                    forModelType(MockSubModel.class),
                    containing(
                        nodeOfType(ConcreteValueNode.class,
                            named(MOCK_SUB_PROPERTY), inDefaultNamespace(),
                            accessing(
                                propertyNamed(MOCK_SUB_PROPERTY),
                                onModelType(MockSubModel.class)
                            )
                        ))
                ),
                nodeOfType(ConcreteSubtypeNode.class,
                    forModelType(MockOtherSubModel.class),
                    containing(
                        nodeOfType(ConcreteValueNode.class,
                            named(MOCK_OTHER_SUB_PROPERTY), inDefaultNamespace(),
                            accessing(
                                propertyNamed(MOCK_OTHER_SUB_PROPERTY),
                                onModelType(MockOtherSubModel.class)
                            )
                        ))
                ),
                nodeOfType(ConcreteValueNode.class,
                    named(OTHER_MOCK_PROPERTY),
                    accessing(
                        propertyNamed(OTHER_MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectSubtypeSubtype() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .discriminator()
            .subtype(MockSubModel.class)
                .value(MOCK_SUB_PROPERTY)
                .subtype(MockSubSubModel.class)
                    .value(MOCK_SUB_SUB_PROPERTY)
                .end()
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            forModelType(MockModel.class),
            havingAttribute(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY,
                Boolean.class, equalTo(true)),
            containing(
                nodeOfType(ConcreteSubtypeNode.class,
                    forModelType(MockSubModel.class),
                    containing(
                        nodeOfType(ConcreteValueNode.class,
                            named(MOCK_SUB_PROPERTY), inDefaultNamespace(),
                            accessing(
                                propertyNamed(MOCK_SUB_PROPERTY),
                                onModelType(MockSubModel.class)
                            )
                        ),
                        nodeOfType(ConcreteSubtypeNode.class,
                            forModelType(MockSubSubModel.class),
                            containing(
                              nodeOfType(ConcreteValueNode.class,
                                  named(MOCK_SUB_SUB_PROPERTY),
                                  inDefaultNamespace(),
                                  accessing(
                                      propertyNamed(MOCK_SUB_SUB_PROPERTY),
                                      onModelType(MockSubSubModel.class)
                                  )
                              )
                            )
                        )
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectObjectSubtype() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .object(MOCK_PROPERTY, MockChildModel.class)
                .discriminator()
                .value(OTHER_MOCK_PROPERTY)
                .subtype(MockChildSubModel.class)
                    .value(MOCK_SUB_PROPERTY)
                .end()
            .end()
            .build();


    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            forModelType(MockModel.class),
            containing(
                nodeOfType(ConcreteObjectNode.class,
                    named(MOCK_PROPERTY), inDefaultNamespace(),
                    forModelType(MockChildModel.class),
                    accessing(
                        propertyNamed(MOCK_PROPERTY),
                        onModelType(MockModel.class)
                    ),
                    havingAttribute(DiscriminatorEventService.DISCRIMINATOR_FLAG_KEY,
                        Boolean.class, equalTo(true)),
                    containing(
                        nodeOfType(ConcreteValueNode.class,
                            named(OTHER_MOCK_PROPERTY), inDefaultNamespace(),
                            accessing(
                                propertyNamed(OTHER_MOCK_PROPERTY),
                                onModelType(MockChildModel.class)
                            )
                        ),
                        nodeOfType(ConcreteSubtypeNode.class,
                            forModelType(MockChildSubModel.class),
                            containing(
                                nodeOfType(ConcreteValueNode.class,
                                    named(MOCK_SUB_PROPERTY),
                                    inDefaultNamespace(),
                                    accessing(
                                        propertyNamed(MOCK_SUB_PROPERTY),
                                        onModelType(MockChildSubModel.class)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectArrayOfObjectsArray() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfObjects(MOCK_ARRAY, Object.class)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfObjectsNode.class,
                    named(MOCK_ARRAY), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_ARRAY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectArrayOfObjectsCollection() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfObjects(MOCK_COLLECTION, Object.class)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfObjectsNode.class,
                    named(MOCK_COLLECTION), inDefaultNamespace(),
                    accessing(
                        propertyNamed(MOCK_COLLECTION),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectArrayOfObjectsElementName() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfObjects(MOCK_ARRAY, ELEMENT_NAME, Object.class)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfObjectsNode.class,
                    named(MOCK_ARRAY), inDefaultNamespace(),
                    elementsNamed(ELEMENT_NAME),
                    accessing(
                        propertyNamed(MOCK_ARRAY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test
  public void testObjectArrayOfObjectsElementNameNamespace() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .arrayOfObjects(MOCK_ARRAY, ELEMENT_NAME, NAMESPACE, Object.class)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteArrayOfObjectsNode.class,
                    named(MOCK_ARRAY),
                    elementsNamed(ELEMENT_NAME),
                    inNamespace(NAMESPACE),
                    accessing(
                        propertyNamed(MOCK_ARRAY),
                        onModelType(MockModel.class)
                    )
                )
            )
        )
    ));
  }

  @Test(expected = ViewTemplateException.class)
  public void testObjectArrayOfObjectsWithIncompatibleProperty() throws Exception {
    ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
        .arrayOfObjects(MOCK_PROPERTY, Object.class)
        .end()
        .build();
  }

  @Test
  public void testObjectEnvelope() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .envelope(ENVELOPE_NAME)
                .value(MOCK_PROPERTY)
                .value(OTHER_MOCK_PROPERTY)
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteEnvelopeNode.class,
                    named(ENVELOPE_NAME), inDefaultNamespace(),
                    accessingNothing(),
                    containing(
                        nodeOfType(ConcreteValueNode.class,
                            named(MOCK_PROPERTY), inDefaultNamespace(),
                            accessing(
                                propertyNamed(MOCK_PROPERTY),
                                onModelType(MockModel.class)
                            )
                        ),
                        nodeOfType(ConcreteValueNode.class,
                            named(OTHER_MOCK_PROPERTY), inDefaultNamespace(),
                            accessing(
                                propertyNamed(OTHER_MOCK_PROPERTY),
                                onModelType(MockModel.class)
                            )
                        )
                    )
                )
            )
        )
    ));


  }

  @Test
  public void testObjectEnvelopeEnvelope() throws Exception {
    ConcreteViewTemplate template = (ConcreteViewTemplate)
        ViewTemplateBuilderProducer.object(VIEW_NAME, NAMESPACE, MockModel.class)
            .envelope(ENVELOPE_NAME)
                .envelope(ENVELOPE_NAME)
                    .value(MOCK_PROPERTY)
                    .value(OTHER_MOCK_PROPERTY)
                .end()
            .end()
            .build();

    assertThat(template.getRoot(), is(
        nodeOfType(RootObjectNode.class,
            named(VIEW_NAME), inNamespace(NAMESPACE),
            containing(
                nodeOfType(ConcreteEnvelopeNode.class,
                    named(ENVELOPE_NAME), inDefaultNamespace(),
                    accessingNothing(),
                    containing(
                        nodeOfType(ConcreteEnvelopeNode.class,
                            named(ENVELOPE_NAME), inDefaultNamespace(),
                            accessingNothing(),
                            containing(
                                nodeOfType(ConcreteValueNode.class,
                                    named(MOCK_PROPERTY), inDefaultNamespace(),
                                    accessing(
                                        propertyNamed(MOCK_PROPERTY),
                                        onModelType(MockModel.class)
                                    )
                                ),
                                nodeOfType(ConcreteValueNode.class,
                                    named(OTHER_MOCK_PROPERTY),
                                    inDefaultNamespace(),
                                    accessing(
                                        propertyNamed(OTHER_MOCK_PROPERTY),
                                        onModelType(MockModel.class)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    ));

  }

}
