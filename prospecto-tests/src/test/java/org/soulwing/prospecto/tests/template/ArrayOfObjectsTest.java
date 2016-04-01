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
package org.soulwing.prospecto.tests.template;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.tests.template.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.tests.template.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.tests.template.ViewMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.tests.template.ViewMatchers.inNamespace;
import static org.soulwing.prospecto.tests.template.ViewMatchers.whereValue;
import static org.soulwing.prospecto.tests.template.ViewMatchers.withName;
import static org.soulwing.prospecto.tests.template.ViewMatchers.withNoName;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * Tests for structures using array-of-objects nodes.
 *
 * @author Carl Harris
 */
public class ArrayOfObjectsTest {

  public static final String NAME = "root";
  public static final String NAMESPACE = "namespace";
  public static final String CHILD = "child";
  public static final String CHILDREN = "children";

  @SuppressWarnings("unused")
  public static class MockType1 {
    MockType2 child = new MockType2();
    List<MockType2> children = Collections.singletonList(new MockType2());
  }

  @SuppressWarnings("unused")
  public static class MockType2 {
  }

  private MockType1 model = new MockType1();

  ViewContext context = ViewContextProducer.newContext();


  @Test
  public void testArrayOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(MockType1.class)
        .build();

    validateRootArrayOfObjects(template);
  }

  @Test
  public void testArrayOfObjectsName() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(NAME, MockType1.class)
        .build();

    validateRootArrayOfObjectsName(template);
  }

  @Test
  public void testArrayOfObjectsNameElement() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(NAME, CHILD, MockType1.class)
        .build();

    validateRootArrayOfObjectsNameElement(template);
  }

  @Test
  public void testArrayOfObjectsNameElementNamespace() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(NAME, CHILD, NAMESPACE, MockType1.class)
        .build();

    validateRootArrayOfObjectsNameElementNamespace(template);
  }

  @Test
  public void testArrayOfObjectsTemplate() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(childTemplate);

    validateRootArrayOfObjects(template);
  }

  @Test
  public void testArrayOfObjectsTemplateName() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(NAME, childTemplate);

    validateRootArrayOfObjectsName(template);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testArrayOfObjectsTemplateNameElement() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(NAME, CHILD, childTemplate);

    validateRootArrayOfObjectsNameElement(template);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testArrayOfObjectsTemplateNameElementNamespace() throws Exception {
    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(NAME, CHILD, NAMESPACE, childTemplate);

    validateRootArrayOfObjectsNameElementNamespace(template);
  }

  @SuppressWarnings("unchecked")
  private void validateRootArrayOfObjects(ViewTemplate template) {
    assertThat(template.generateView(Collections.singleton(model), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY, withNoName(), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(BEGIN_OBJECT, withNoName(), inDefaultNamespace()),
            eventOfType(END_OBJECT, withNoName(), inDefaultNamespace()),
            eventOfType(END_ARRAY, withNoName(), inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );
  }

  @SuppressWarnings("unchecked")
  private void validateRootArrayOfObjectsName(ViewTemplate template) {
    assertThat(template.generateView(Collections.singleton(model), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(BEGIN_OBJECT, withNoName(), inDefaultNamespace()),
            eventOfType(END_OBJECT, withNoName(), inDefaultNamespace()),
            eventOfType(END_ARRAY, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );
  }

  @SuppressWarnings("unchecked")
  private void validateRootArrayOfObjectsNameElement(ViewTemplate template) {
    assertThat(template.generateView(Collections.singleton(model), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(BEGIN_OBJECT, withName(CHILD), inDefaultNamespace()),
            eventOfType(END_OBJECT, withName(CHILD), inDefaultNamespace()),
            eventOfType(END_ARRAY, withName(NAME), inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );
  }

  @SuppressWarnings("unchecked")
  private void validateRootArrayOfObjectsNameElementNamespace(
      ViewTemplate template) {
    assertThat(template.generateView(Collections.singleton(model), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY, withName(NAME), inNamespace(NAMESPACE),
                whereValue(is(nullValue()))),
            eventOfType(BEGIN_OBJECT, withName(CHILD), inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT, withName(CHILD), inNamespace(NAMESPACE)),
            eventOfType(END_ARRAY, withName(NAME), inNamespace(NAMESPACE),
                whereValue(is(nullValue())))
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testArrayOfObjectsArrayOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects(CHILDREN, MockType2.class)
                .end()
            .end()
        .build();

    assertThat(template.generateView(Collections.singleton(model), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY),
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName(CHILDREN)),
            eventOfType(BEGIN_OBJECT),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY, withName(CHILDREN)),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY)
        )
    );

  }

}
