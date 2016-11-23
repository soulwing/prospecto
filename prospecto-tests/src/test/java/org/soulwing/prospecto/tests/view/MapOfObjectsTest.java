/*
 * File created on Nov 23, 2016
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withNoName;

import java.beans.Introspector;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for structures using map-of-objects nodes.
 *
 * @author Carl Harris
 */
public class MapOfObjectsTest {

  public static final String NAME = "root";
  public static final String ROOT_KEY = "rootKey";
  public static final String CHILD_KEY = "childKey";
  public static final String DEFAULT_NAME =
      Introspector.decapitalize(MockType1.class.getSimpleName());

  public static final String NAMESPACE = "namespace";
  public static final String CHILD = "child";
  public static final String CHILDREN = "children";

  @SuppressWarnings("unused")
  public static class MockType1 {
    MockType2 child = new MockType2();
    Map<String, MockType2> children = Collections.singletonMap(CHILD_KEY,
        new MockType2());
  }

  @SuppressWarnings("unused")
  public static class MockType2 {
  }

  private MockType1 model = new MockType1();

  ViewContext context = ViewContextProducer.newContext();


  @Test
  public void testMapOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .mapOfObjects(String.class, MockType1.class)
        .build();

    validateRootMapOfObjects(template);
  }

  @Test
  public void testMapOfObjectsName() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .mapOfObjects(NAME, String.class, MockType1.class)
        .build();

    validateRootMapOfObjectsName(template);
  }

  @Test
  public void testMapOfObjectsNameNamespace() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .mapOfObjects(NAME, NAMESPACE, String.class, MockType1.class)
        .build();

    validateRootMapOfObjectsNameNamespace(template);
  }

  @Test
  public void testMapOfObjectsTemplate() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .mapOfObjects(String.class, childTemplate);

    validateRootMapOfObjects(template);
  }

  @Test
  public void testMapOfObjectsTemplateName() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .mapOfObjects(NAME, String.class, childTemplate);

    validateRootMapOfObjectsName(template);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testArrayOfObjectsTemplateNameNamespace() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .mapOfObjects(NAME, NAMESPACE, String.class, childTemplate);

    validateRootMapOfObjectsNameNamespace(template);
  }

  @SuppressWarnings("unchecked")
  private void validateRootMapOfObjects(ViewTemplate template) {
    assertThat(template.generateView(Collections.singletonMap(ROOT_KEY, model),
        context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT, withNoName(), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(BEGIN_OBJECT, withName(ROOT_KEY),
                inDefaultNamespace()),
            eventOfType(END_OBJECT, withName(ROOT_KEY),
                inDefaultNamespace()),
            eventOfType(END_OBJECT, withNoName(), inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );
  }

  @SuppressWarnings("unchecked")
  private void validateRootMapOfObjectsName(ViewTemplate template) {
    assertThat(template.generateView(Collections.singletonMap(ROOT_KEY, model), context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(BEGIN_OBJECT, withName(ROOT_KEY), inDefaultNamespace()),
            eventOfType(END_OBJECT, withName(ROOT_KEY), inDefaultNamespace()),
            eventOfType(END_OBJECT, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );
  }

  @SuppressWarnings("unchecked")
  private void validateRootMapOfObjectsNameNamespace(
      ViewTemplate template) {
    assertThat(template.generateView(Collections.singletonMap(ROOT_KEY, model),
        context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT, withName(NAME), inNamespace(NAMESPACE),
                whereValue(is(nullValue()))),
            eventOfType(BEGIN_OBJECT, withName(ROOT_KEY), inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT, withName(ROOT_KEY), inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT, withName(NAME), inNamespace(NAMESPACE),
                whereValue(is(nullValue())))
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testMapOfObjectsMapOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .mapOfObjects(String.class, MockType1.class)
            .accessType(AccessType.FIELD)
            .mapOfObjects(CHILDREN, String.class, MockType2.class)
                .end()
            .end()
        .build();

    assertThat(template.generateView(Collections.singletonMap(ROOT_KEY, model),
        context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName(ROOT_KEY)),
            eventOfType(BEGIN_OBJECT, withName(CHILDREN)),
            eventOfType(BEGIN_OBJECT, withName(CHILD_KEY)),
            eventOfType(END_OBJECT, withName(CHILD_KEY)),
            eventOfType(END_OBJECT, withName(CHILDREN)),
            eventOfType(END_OBJECT, withName(ROOT_KEY)),
            eventOfType(END_OBJECT)
        )
    );

  }

}
