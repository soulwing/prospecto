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
package org.soulwing.prospecto.tests.editor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.DISCRIMINATOR;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for editing an object.
 *
 * @author Carl Harris
 */
public class EditorTest {

  private static final String TYPE = "type";
  private static final String CHILD = "child";
  private static final String CHILDREN = "children";
  private static final String STRING = "string";
  private static final String STRINGS = "strings";
  private static final String SUBTYPE_STRING = "subtypeString";
  private static final String UPDATED_STRING = "updated " + STRING;
  private static final String UPDATED_SUBTYPE_STRING = "updated " + SUBTYPE_STRING;

  @SuppressWarnings("unused")
  public static class MockType1 {
    String string = STRING;
    String[] strings = new String[] { STRING };
    MockType2 child = new MockType2();
    List<MockType2> children = Collections.singletonList(new MockType2());
  }

  @SuppressWarnings("unused")
  public static class MockSubType1 extends MockType1 {
    String subtypeString = SUBTYPE_STRING;
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

    final ModelEditor editor = template.generateEditor(view, context);

    final MockType1 model = new MockType1();
    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
  }

  @Test
  public void testObjectArrayOfValues() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfValues(STRINGS)
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

    final ModelEditor editor = template.generateEditor(view, context);

    final MockType1 model = new MockType1();
    editor.update(model);
    assertThat(model.strings, is(equalTo(new String[] { UPDATED_STRING })));
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
        .type(END_OBJECT)
        .type(END_OBJECT)
        .end();

    final ModelEditor editor = template.generateEditor(view, context);

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
            EditorTest.class.getSimpleName() + "$" + MockSubType1.class.getSimpleName())
        .type(VALUE).name(STRING).value(UPDATED_STRING)
        .type(VALUE).name(SUBTYPE_STRING).value(UPDATED_SUBTYPE_STRING)
        .type(END_OBJECT)
        .end();

    final ModelEditor editor = template.generateEditor(view, context);

    final MockSubType1 model = new MockSubType1();
    editor.update(model);
    assertThat(model.string, is(equalTo(UPDATED_STRING)));
    assertThat(model.subtypeString, is(equalTo(UPDATED_SUBTYPE_STRING)));
  }


}
