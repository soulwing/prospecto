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
package org.soulwing.prospecto.tests.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.DISCRIMINATOR;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.reference.ReferenceResolver;
import org.soulwing.prospecto.runtime.view.ViewBuilder;

/**
 * Tests for editing an object.
 *
 * @author Carl Harris
 */
public class ViewApplicatorTest {

  private static final String TYPE = "type";
  private static final String CHILD = "child";
  private static final String CHILDREN_LIST = "childrenList";
  private static final String CHILDREN_MAP = "childrenMap";
  private static final String CHILDREN_ARRAY = "childrenArray";
  private static final String STRING = "string";
  private static final String STRINGS = "strings";
  private static final String STRING_KEY = "stringKey";
  private static final String STRING_KEY1 = "stringKey1";
  private static final String STRING_KEY2 = "stringKey2";
  private static final String STRING_MAP = "stringMap";
  private static final String SUBTYPE_STRING = "subtypeString";
  private static final String UPDATED_STRING = "updated " + STRING;
  private static final String UPDATED_SUBTYPE_STRING = "updated " + SUBTYPE_STRING;
  private static final String UPDATED_CHILD_STRING1 = "updated " + STRING + "1";
  private static final String UPDATED_CHILD_STRING2 = "updated " + STRING + "2";

  @SuppressWarnings("unused")
  public static class MockType1 {
    String string = STRING;
    String[] strings = new String[] { STRING };
    Map<String, String> stringMap = new LinkedHashMap<>();
    MockType2 child = new MockType2();
    List<MockType2> childrenList = new ArrayList<>();
    Map<String, MockType2> childrenMap = new LinkedHashMap<>();
    MockType2[] childrenArray = new MockType2[0];
  }

  @SuppressWarnings("unused")
  public static class MockSubType1 extends MockType1 {
    String subtypeString = SUBTYPE_STRING;
  }

  @SuppressWarnings("unused")
  public static class MockType2 {
    String string = STRING;
    MockType3 child = new MockType3();
    List<MockType3> childrenList = new ArrayList<>();
  }

  @SuppressWarnings("unused")
  public static class MockType3 {
    String string = STRING;
  }

  ViewContext context = ViewContextProducer.newContext();

  @Test
  public void testObjectValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .value(STRING)
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
            .type(BEGIN_OBJECT)
            .type(VALUE).name(STRING).value(UPDATED_STRING)
            .type(END_OBJECT)
        .end();

    final ViewApplicator editor = template.createApplicator(view, context);

    final MockType1 model = new MockType1();
    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
  }

  @Test
  public void testObjectArrayOfValues() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfValues(STRINGS, String.class)
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(BEGIN_ARRAY).name(STRINGS)
        .type(VALUE).value(UPDATED_STRING)
        .type(END_ARRAY).name(STRINGS)
        .type(END_OBJECT)
        .end();

    final ViewApplicator editor = template.createApplicator(view, context);

    final MockType1 model = new MockType1();
    editor.update(model);
    assertThat(model.strings, is(equalTo(new String[] { UPDATED_STRING })));
  }

  @Test
  public void testObjectMapOfValues() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .accessType(AccessType.FIELD)
        .mapOfValues(STRING_MAP, String.class, String.class)
        .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(BEGIN_OBJECT).name(STRING_MAP)
        .type(VALUE).name(STRING_KEY).value(UPDATED_STRING)
        .type(END_OBJECT).name(STRING_MAP)
        .type(END_OBJECT)
        .end();

    final ViewApplicator editor = template.createApplicator(view, context);

    final MockType1 model = new MockType1();
    editor.update(model);
    assertThat(model.stringMap,
        is(equalTo(Collections.singletonMap(STRING_KEY, UPDATED_STRING))));
  }

  @Test
  public void testObjectArrayOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects(CHILDREN_LIST, MockType2.class)
                .value(STRING)
                .end()
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(BEGIN_ARRAY).name(CHILDREN_LIST)
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(UPDATED_CHILD_STRING1)
        .type(END_OBJECT)
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(UPDATED_CHILD_STRING2)
        .type(END_OBJECT)
        .type(END_ARRAY).name(CHILDREN_LIST)
        .type(END_OBJECT)
        .end();

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();
    editor.update(model);

    assertThat(model.childrenList.size(), is(equalTo(2)));
    assertThat(model.childrenList.get(0).string, is(equalTo(UPDATED_CHILD_STRING1)));
    assertThat(model.childrenList.get(1).string, is(equalTo(UPDATED_CHILD_STRING2)));
  }

  @Test
  public void testObjectMapOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .accessType(AccessType.FIELD)
        .mapOfObjects(CHILDREN_MAP, String.class, MockType2.class)
            .value(STRING)
            .end()
        .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(BEGIN_OBJECT).name(CHILDREN_MAP)
        .type(BEGIN_OBJECT).name(STRING_KEY1)
        .type(VALUE).name(STRING).value(UPDATED_CHILD_STRING1)
        .type(END_OBJECT).name(STRING_KEY1)
        .type(BEGIN_OBJECT).name(STRING_KEY2)
        .type(VALUE).name(STRING).value(UPDATED_CHILD_STRING2)
        .type(END_OBJECT).name(STRING_KEY2)
        .type(END_OBJECT).name(CHILDREN_MAP)
        .type(END_OBJECT)
        .end();

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();
    editor.update(model);

    assertThat(model.childrenMap.size(), is(equalTo(2)));
    assertThat(model.childrenMap.get(STRING_KEY1).string, is(equalTo(UPDATED_CHILD_STRING1)));
    assertThat(model.childrenMap.get(STRING_KEY2).string, is(equalTo(UPDATED_CHILD_STRING2)));
  }

  @Test
  public void testObjectValueObjectValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .value(STRING)
            .object(CHILD, MockType2.class)
                .value(STRING)
                .end()
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(BEGIN_OBJECT).name(CHILD)
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(END_OBJECT).name(CHILD)
        .type(END_OBJECT)
        .end();

    final ViewApplicator editor = template.createApplicator(view, context);

    final MockType1 model = new MockType1();
    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
    assertThat(model.child.string, is(equalTo(UPDATED_STRING)));
  }

  @Test
  public void testObjectSubtypeValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .discriminator()
            .value(STRING)
            .subtype(MockSubType1.class)
                .value(SUBTYPE_STRING)
                .end()
            .end()
          .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(DISCRIMINATOR).name(TYPE).value(
            ViewApplicatorTest.class.getSimpleName() + "$" + MockSubType1.class.getSimpleName())
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(VALUE).name(SUBTYPE_STRING).value(UPDATED_SUBTYPE_STRING)
        .type(END_OBJECT)
        .end();

    final ViewApplicator editor = template.createApplicator(view, context);

    final MockSubType1 model = new MockSubType1();
    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
    assertThat(model.subtypeString, is(equalTo(UPDATED_SUBTYPE_STRING)));
  }

  @Test
  public void testObjectObjectReferenceValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, MockType2.class)
                .reference(CHILD, MockType3.class)
                    .value(STRING)
                    .end()
                .end()
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(BEGIN_OBJECT).name(CHILD)
        .type(BEGIN_OBJECT).name(CHILD)
        .type(VALUE).name(STRING).value(STRING)
        .type(END_OBJECT).name(CHILD)
        .type(END_OBJECT).name(CHILD)
        .type(END_OBJECT)
        .end();

    final MockType3 otherChild = new MockType3();
    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return MockType3.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return otherChild;
      }
    });

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();

    editor.update(model);
    assertThat(model.child.child, is(sameInstance(otherChild)));
  }

  @Test
  public void testObjectObjectArrayOfReferencesValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object(CHILD, MockType2.class)
                .arrayOfReferences(CHILDREN_LIST, MockType3.class)
                    .value(STRING)
                    .end()
                .end()
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(BEGIN_OBJECT).name(CHILD)
        .type(BEGIN_ARRAY).name(CHILDREN_LIST)
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(STRING)
        .type(END_OBJECT)
        .type(END_ARRAY).name(CHILDREN_LIST)
        .type(END_OBJECT).name(CHILD)
        .type(END_OBJECT)
        .end();

    final MockType3 otherChild = new MockType3();
    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return MockType3.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return otherChild;
      }
    });

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();

    editor.update(model);
    assertThat(model.child.childrenList, contains(otherChild));
  }

  @Test
  public void testObjectValueReferenceValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .value(STRING)
            .reference(CHILD, MockType2.class)
                .value(STRING)
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(BEGIN_OBJECT).name(CHILD)
        .type(VALUE).name(STRING).value(STRING)
        .type(END_OBJECT).name(CHILD)
        .type(END_OBJECT)
        .end();

    final MockType2 otherChild = new MockType2();
    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return MockType2.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return otherChild;
      }
    });

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();

    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
    assertThat(model.child, is(sameInstance(otherChild)));
  }

  @Test
  public void testObjectValueMapOfReferencesValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .value(STRING)
            .mapOfReferences(CHILDREN_MAP, String.class, MockType2.class)
                .value(STRING)
                .end()
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(BEGIN_OBJECT).name(CHILDREN_MAP)
        .type(BEGIN_OBJECT).name(STRING_KEY)
        .type(VALUE).name(STRING).value(STRING)
        .type(END_OBJECT).name(STRING_KEY)
        .type(END_OBJECT).name(CHILDREN_MAP)
        .type(END_OBJECT)
        .end();

    final MockType2 otherChild = new MockType2();
    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return MockType2.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return otherChild;
      }
    });

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();

    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
    assertThat(model.childrenMap, hasEntry(STRING_KEY, otherChild));
  }

  @Test
  public void testObjectValueArrayOfReferencesListValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .value(STRING)
            .arrayOfReferences(CHILDREN_LIST, MockType2.class)
                .value(STRING)
                .end()
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(BEGIN_ARRAY).name(CHILDREN_LIST)
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(STRING)
        .type(END_OBJECT)
        .type(END_ARRAY).name(CHILDREN_LIST)
        .type(END_OBJECT)
        .end();

    final MockType2 otherChild = new MockType2();
    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return MockType2.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return otherChild;
      }
    });

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();

    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
    assertThat(model.childrenList, contains(otherChild));
  }

  @Test
  public void testObjectValueArrayOfReferencesArrayValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .accessType(AccessType.FIELD)
        .value(STRING)
        .arrayOfReferences(CHILDREN_ARRAY, MockType2.class)
        .value(STRING)
        .end()
        .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(BEGIN_ARRAY).name(CHILDREN_ARRAY)
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(STRING)
        .type(END_OBJECT)
        .type(END_ARRAY).name(CHILDREN_ARRAY)
        .type(END_OBJECT)
        .end();

    final MockType2 otherChild = new MockType2();
    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return MockType2.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return otherChild;
      }
    });

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();

    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
    assertThat(model.childrenArray, is(arrayContaining(otherChild)));
  }

  @Test
  public void testObjectArrayOfObjectsArrayOfReferences() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects(CHILDREN_LIST, MockType2.class)
                .arrayOfReferences(CHILDREN_LIST, MockType3.class)
                    .value(STRING)
                    .end()
                .end()
            .end()
        .build();

    final View view = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT)
        .type(BEGIN_ARRAY).name(CHILDREN_LIST)
        .type(BEGIN_OBJECT)
        .type(BEGIN_ARRAY).name(CHILDREN_LIST)
        .type(BEGIN_OBJECT)
        .type(VALUE).name(STRING).value(STRING)
        .type(END_OBJECT)
        .type(END_ARRAY).name(CHILDREN_LIST)
        .type(END_OBJECT)
        .type(END_ARRAY).name(CHILDREN_LIST)
        .type(END_OBJECT)
        .end();

    final MockType3 otherChild = new MockType3();
    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return MockType3.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return otherChild;
      }
    });

    final ViewApplicator editor = template.createApplicator(view, context);
    final MockType1 model = new MockType1();

    editor.update(model);
    assertThat(model.childrenList.size(), is(equalTo(1)));
    assertThat(model.childrenList.get(0).childrenList, contains(otherChild));
  }


}
