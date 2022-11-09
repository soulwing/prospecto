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
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.DISCRIMINATOR;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;

import java.util.Collections;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for use of subtype.
 *
 * @author Carl Harris
 */
public class SubtypeTest {

  private static final String SUBTYPE1_STRING = "subtype1String";
  private static final String SUBTYPE2_STRING = "subtype2String";
  private static final String SUBTYPE3_STRING = "subtype3String";

  @SuppressWarnings("unused")
  public static class MockType1 {
  }

  @SuppressWarnings("unused")
  public static class MockSubType1 extends MockType1 {
    Object subtype1String = SUBTYPE1_STRING;
  }

  @SuppressWarnings("unused")
  public static class MockSubType2 extends MockType1 {
    Object subtype2String = SUBTYPE2_STRING;
  }

  @SuppressWarnings("unused")
  public static class MockSubType3 extends MockSubType2 {
    Object subtype3String = SUBTYPE3_STRING;
  }

  ViewContext context = ViewContextProducer.newContext();

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectSubtype() throws Exception {

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .discriminator()
            .subtype(MockSubType1.class)
                .value(SUBTYPE1_STRING)
                .end()
            .subtype(MockSubType2.class)
                .value(SUBTYPE2_STRING)
                .subtype(MockSubType3.class)
                    .value(SUBTYPE3_STRING)
                    .end()
                .end()
            .end()
        .build();

    assertThat(template.generateView(new MockSubType1(), context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockSubType1.class.getSimpleName())))),
            eventOfType(VALUE, withName(SUBTYPE1_STRING),
                whereValue(is(equalTo(SUBTYPE1_STRING)))),
            eventOfType(END_OBJECT)
        )
    );

    assertThat(template.generateView(new MockSubType2(), context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockSubType2.class.getSimpleName())))),
            eventOfType(VALUE, withName(SUBTYPE2_STRING),
                whereValue(is(equalTo(SUBTYPE2_STRING)))),
            eventOfType(END_OBJECT)
        )
    );

    assertThat(template.generateView(new MockSubType3(), context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockSubType3.class.getSimpleName())))),
            eventOfType(VALUE, withName(SUBTYPE2_STRING),
                whereValue(is(equalTo(SUBTYPE2_STRING)))),
            eventOfType(VALUE, withName(SUBTYPE3_STRING),
                whereValue(is(equalTo(SUBTYPE3_STRING)))),
            eventOfType(END_OBJECT)
        )
    );

  }

  @Test
  public void testArrayOfObjectsSubtype() throws Exception {

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(MockType1.class)
            .accessType(AccessType.FIELD)
            .discriminator()
            .subtype(MockSubType1.class)
                .value(SUBTYPE1_STRING)
                .end()
            .subtype(MockSubType2.class)
              .value(SUBTYPE2_STRING)
              .subtype(MockSubType3.class)
                .value(SUBTYPE3_STRING)
                .end()
              .end()
            .end()
        .build();

    validateArrayOfObjectsSubtype(template);

  }

  @Test
  public void testArrayOfObjectsTemplateSubtype() throws Exception {

    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .discriminator()
            .subtype(MockSubType1.class)
                .value(SUBTYPE1_STRING)
                .end()
            .subtype(MockSubType2.class)
                .value(SUBTYPE2_STRING)
                .subtype(MockSubType3.class)
                    .value(SUBTYPE3_STRING)
                    .end()
                .end()
            .end()
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(childTemplate);

    validateArrayOfObjectsSubtype(template);
  }


  @SuppressWarnings("unchecked")
  private void validateArrayOfObjectsSubtype(ViewTemplate template) {
    assertThat(
        template.generateView(Collections.singleton(new MockSubType1()), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY),
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockSubType1.class.getSimpleName())))),
            eventOfType(VALUE, withName(SUBTYPE1_STRING),
                whereValue(is(equalTo(SUBTYPE1_STRING)))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY)
        )
    );

    assertThat(
        template.generateView(Collections.singleton(new MockSubType2()), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY),
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockSubType2.class.getSimpleName())))),
            eventOfType(VALUE, withName(SUBTYPE2_STRING),
                whereValue(is(equalTo(SUBTYPE2_STRING)))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY)
        )
    );

    assertThat(
        template.generateView(Collections.singleton(new MockSubType3()), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY),
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockSubType3.class.getSimpleName())))),
            eventOfType(VALUE, withName(SUBTYPE2_STRING),
                whereValue(is(equalTo(SUBTYPE2_STRING)))),
            eventOfType(VALUE, withName(SUBTYPE3_STRING),
                whereValue(is(equalTo(SUBTYPE3_STRING)))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY)
        )
    );
  }

}


