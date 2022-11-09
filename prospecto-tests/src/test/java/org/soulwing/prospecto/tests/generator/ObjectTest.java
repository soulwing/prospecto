/*
 * File created on Apr 1, 2016
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
package org.soulwing.prospecto.tests.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.META;
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withNoName;

import java.beans.Introspector;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.meta.MetadataHandler;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.api.template.MetaNode;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.api.url.UrlResolver;

/**
 * Tests for structures using object nodes.
 **
 * @author Carl Harris
 */
public class ObjectTest {

  public static final String NAME = "root";
  public static final String NAMESPACE = "namespace";
  public static final String CHILD = "child";
  public static final String CHILDREN = "children";
  public static final String STRING = "string";
  public static final String STRINGS = "strings";
  public static final String RESOLVED_URL = "resolvedUrl";
  public static final String META_NAME = "meta";
  public static final String META_VALUE = "metaValue";


  @SuppressWarnings("unused")
  public static class MockType1 {
    String string = STRING;
    String[] strings = new String[] { STRING };
    MockType2 child = new MockType2();
    List<MockType2> children = Collections.singletonList(new MockType2());
  }

  @SuppressWarnings("unused")
  public static class MockType2 {
    String string = STRING;
    MockType3 child = new MockType3();
  }

  @SuppressWarnings("unused")
  public static class MockType3 {
    String string = STRING;
  }


  private MockType1 model = new MockType1();

  ViewContext context = ViewContextProducer.newContext();

  @Test
  @SuppressWarnings("unchecked")
  public void testObject() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT,
                withName(Introspector.decapitalize(MockType1.class.getSimpleName())),
                inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(END_OBJECT,
                withName(Introspector.decapitalize(MockType1.class.getSimpleName())),
                inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectName() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(NAME, MockType1.class)
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(END_OBJECT, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectNameNamespace() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(NAME, NAMESPACE, MockType1.class)
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT, withName(NAME), inNamespace(NAMESPACE),
                whereValue(is(nullValue()))),
            eventOfType(END_OBJECT, withName(NAME), inNamespace(NAMESPACE),
                whereValue(is(nullValue())))
        )
    );
  }

  @Test
  public void testObjectNull() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(NAME, NAMESPACE, MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, MockType2.class)
                .end()
            .end()
        .build();

    model.child = null;
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(VALUE, withName(CHILD), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  public void testObjectUrl() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(NAME, NAMESPACE, MockType1.class)
            .url()
        .end()
        .build();

    context.appendScope().put(MockUrlResolver.INSTANCE);
    model.child = null;
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(ViewDefaults.URL_NODE_NAME),
                inDefaultNamespace(),
                whereValue(is(equalTo(RESOLVED_URL)))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  public void testObjectMeta() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(NAME, NAMESPACE, MockType1.class)
          .meta(META_NAME, MockMetadataHandler.INSTANCE)
        .end()
        .build();

    model.child = null;
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inDefaultNamespace(),
                whereValue(is(equalTo(META_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
  }


  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObject() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, MockType2.class)
                .end()
            .end()
        .build();

    validateObjectObject(template);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectTemplate() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, childTemplate)
        .end()
        .build();

    validateObjectObject(template);
  }

  @SuppressWarnings("unchecked")
  private void validateObjectObject(ViewTemplate template) {
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(CHILD), inDefaultNamespace()),
            eventOfType(END_OBJECT, withName(CHILD), inDefaultNamespace()),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectNamespace() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
                .object(CHILD, NAMESPACE, MockType2.class)
                .end()
            .end()
        .build();

    validateObjectObjectNamespace(template);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectNamespaceTemplate() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, NAMESPACE, childTemplate)
            .end()
        .build();

    validateObjectObjectNamespace(template);
  }

  @SuppressWarnings("unchecked")
  private void validateObjectObjectNamespace(ViewTemplate template) {
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(CHILD), inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT, withName(CHILD), inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  public void testObjectArrayOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects(CHILDREN, MockType2.class)
                .end()
            .end()
        .build();

    validateObjectArrayOfObjects(template);
  }

  @Test
  public void testObjectArrayOfObjectsTemplate() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects(CHILDREN, childTemplate)
            .end()
        .build();

    validateObjectArrayOfObjects(template);
  }

  @SuppressWarnings("unchecked")
  private void validateObjectArrayOfObjects(ViewTemplate template) {
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName(CHILDREN), inDefaultNamespace()),
            eventOfType(BEGIN_OBJECT),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY, withName(CHILDREN), inDefaultNamespace()),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  public void testObjectArrayOfObjectsElementName() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
                .arrayOfObjects(CHILDREN, CHILD, MockType2.class)
                .end()
            .end()
        .build();

    validateObjectArrayOfObjectsElementName(template);
  }

  @Test
  public void testObjectArrayOfObjectsElementNameTemplate() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects(CHILDREN, CHILD, childTemplate)
        .end()
        .build();

    validateObjectArrayOfObjectsElementName(template);
  }

  @SuppressWarnings("unchecked")
  private void validateObjectArrayOfObjectsElementName(ViewTemplate template) {
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName(CHILDREN), inDefaultNamespace()),
            eventOfType(BEGIN_OBJECT, withName(CHILD), inDefaultNamespace()),
            eventOfType(END_OBJECT, withName(CHILD), inDefaultNamespace()),
            eventOfType(END_ARRAY, withName(CHILDREN), inDefaultNamespace()),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectArrayOfObjectsElementNamespace() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
                .arrayOfObjects(CHILDREN, CHILD, NAMESPACE, MockType2.class)
                .end()
            .end()
        .build();

    validateObjectArrayOfObjectsElementNamespace(template);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectArrayOfObjectsElementNamespaceTemplate() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects(CHILDREN, CHILD, NAMESPACE, childTemplate)
            .end()
        .build();

    validateObjectArrayOfObjectsElementNamespace(template);
  }

  @SuppressWarnings("unchecked")
  private void validateObjectArrayOfObjectsElementNamespace(ViewTemplate template) {
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName(CHILDREN), inNamespace(NAMESPACE)),
            eventOfType(BEGIN_OBJECT, withName(CHILD), inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT, withName(CHILD), inNamespace(NAMESPACE)),
            eventOfType(END_ARRAY, withName(CHILDREN), inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .value(STRING)
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(VALUE, withName(STRING),
                whereValue(is(equalTo(model.string)))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, MockType2.class)
                .value(STRING)
                .end()
            .end()
        .build();

    validateObjectObjectValue(template);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectTemplateValue() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
            .accessType(AccessType.FIELD)
            .value(STRING)
            .end()
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, childTemplate)
            .end()
        .build();

    validateObjectObjectValue(template);
  }


  @SuppressWarnings("unchecked")
  private void validateObjectObjectValue(ViewTemplate template) {
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(CHILD)),
            eventOfType(VALUE, withName(STRING),
                whereValue(is(equalTo(model.string)))),
            eventOfType(END_OBJECT, withName(CHILD)),
            eventOfType(END_OBJECT)
        )
    );
  }


  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectObjectValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, MockType2.class)
                .object(CHILD, MockType3.class)
                    .value(STRING)
                    .end()
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(CHILD)),
            eventOfType(BEGIN_OBJECT, withName(CHILD)),
            eventOfType(VALUE, withName(STRING),
                whereValue(is(equalTo(model.string)))),
            eventOfType(END_OBJECT, withName(CHILD)),
            eventOfType(END_OBJECT, withName(CHILD)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectArrayOfValues() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfValues(STRINGS, String.class)
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName(STRINGS), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(VALUE, withNoName(), inDefaultNamespace(),
                whereValue(is(equalTo(model.string)))),
            eventOfType(END_ARRAY, withName(STRINGS), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectArrayOfValuesElement() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .accessType(AccessType.FIELD)
        .arrayOfValues(STRINGS, STRING, String.class)
        .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName(STRINGS), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(VALUE, withName(STRING), inDefaultNamespace(),
                whereValue(is(equalTo(model.string)))),
            eventOfType(END_ARRAY, withName(STRINGS), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectArrayOfValuesElementNamespace() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .accessType(AccessType.FIELD)
        .arrayOfValues(STRINGS, STRING, NAMESPACE, String.class)
        .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName(STRINGS), inNamespace(NAMESPACE),
                whereValue(is(nullValue()))),
            eventOfType(VALUE, withName(STRING), inNamespace(NAMESPACE),
                whereValue(is(equalTo(model.string)))),
            eventOfType(END_ARRAY, withName(STRINGS), inNamespace(NAMESPACE),
                whereValue(is(nullValue()))),
            eventOfType(END_OBJECT)
        )
    );
  }

  private static class MockUrlResolver implements UrlResolver {

    static final MockUrlResolver INSTANCE = new MockUrlResolver();

    @Override
    public String resolve(ViewNode node, ViewContext context) {
      return RESOLVED_URL;
    }

  }

  private static class MockMetadataHandler implements MetadataHandler {

    static final MockMetadataHandler INSTANCE = new MockMetadataHandler();

    @Override
    public java.lang.Object produceValue(MetaNode node, Object parentModel,
        ViewContext context) throws Exception {
      return META_VALUE;
    }

    @Override
    public void consumeValue(MetaNode node, Object parentModel, Object value,
        ViewContext context) throws Exception {
    }

  }

}
