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
package org.soulwing.prospecto.tests.view;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;
import static org.soulwing.prospecto.tests.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.tests.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.tests.matcher.ViewMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.tests.matcher.ViewMatchers.inNamespace;
import static org.soulwing.prospecto.tests.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.tests.matcher.ViewMatchers.withName;
import static org.soulwing.prospecto.tests.matcher.ViewMatchers.withNoName;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for structures using envelopes.
 *
 * @author Carl Harris
 */
public class EnvelopeTest {

  private static final String ENVELOPE = "envelope";
  private static final String CHILD = "child";
  private static final String CHILDREN = "children";
  private static final String NAMESPACE = "namespace";
  private static final String STRING = "string";
  private static final String STRINGS = "strings";

  @SuppressWarnings("unused")
  public static class MockType1 {
    Object string = STRING;
    Object[] strings = new Object[] { STRING };
    MockType2 child = new MockType2();
    List<MockType2> children = Collections.singletonList(new MockType2());
  }

  @SuppressWarnings("unused")
  public static class MockType2 {
    Object string = STRING;
  }

  MockType1 model = new MockType1();

  ViewContext context = ViewContextProducer.newContext();

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectEnvelope() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .envelope(ENVELOPE)
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE),
                inDefaultNamespace(), whereValue(is(nullValue()))),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectEnvelopeNamespace() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .envelope(ENVELOPE, NAMESPACE)
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE),
                inNamespace(NAMESPACE), whereValue(is(nullValue()))),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectEnvelopeValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .envelope(ENVELOPE)
                .value(STRING)
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE)),
            eventOfType(VALUE, withName(STRING),
                whereValue(is(equalTo(STRING)))),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectEnvelopeObject() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .envelope(ENVELOPE)
                .object(CHILD, MockType2.class)
                    .end()
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE)),
            eventOfType(BEGIN_OBJECT, withName(CHILD)),
            eventOfType(END_OBJECT, withName(CHILD)),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectEnvelopeArrayOfValues() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
                .envelope(ENVELOPE)
                    .arrayOfValues(STRINGS, Object.class)
                        .end()
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE)),
            eventOfType(BEGIN_ARRAY, withName(STRINGS)),
            eventOfType(VALUE, withNoName(), whereValue(is(equalTo(STRING)))),
            eventOfType(END_ARRAY, withName(STRINGS)),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectEnvelopeArrayOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .envelope(ENVELOPE)
                .arrayOfObjects(CHILDREN, CHILD, MockType2.class)
                    .value(STRING)
                    .end()
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE)),
            eventOfType(BEGIN_ARRAY, withName(CHILDREN)),
            eventOfType(BEGIN_OBJECT, withName(CHILD)),
            eventOfType(VALUE, withName(STRING),
                whereValue(is(equalTo(STRING)))),
            eventOfType(END_OBJECT, withName(CHILD)),
            eventOfType(END_ARRAY, withName(CHILDREN)),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectEnvelopeEnvelopeValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .envelope(ENVELOPE)
                .envelope(ENVELOPE)
                    .value(STRING)
                    .end()
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE)),
            eventOfType(BEGIN_OBJECT, withName(ENVELOPE)),
            eventOfType(VALUE, withName(STRING),
                whereValue(is(equalTo(STRING)))),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT, withName(ENVELOPE)),
            eventOfType(END_OBJECT)
        )
    );
  }

}
