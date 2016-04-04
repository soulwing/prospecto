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
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.DISCRIMINATOR;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;

/**
 * Tests for use of discriminator.
 *
 * @author Carl Harris
 */
public class DiscriminatorTest {

  private static final Discriminator MOCK_DISCRIMINATOR =
      new Discriminator(new Object());

  @SuppressWarnings("unused")
  public static class MockType1 {
    MockType2 child = new MockType2();
    List<MockType2> children = Collections.singletonList(new MockType2());
  }

  public static class MockType2 {
  }

  static class MockDiscriminatorStrategy implements DiscriminatorStrategy {

    @Override
    public Discriminator toDiscriminator(Class<?> base, Class<?> subtype) {
      return MOCK_DISCRIMINATOR;
    }

    @Override
    public <T> Class<T> toSubtype(Class<T> base, Discriminator discriminator)
        throws ClassNotFoundException {
      return null;
    }

  }

  MockType1 model = new MockType1();

  ViewContext context = ViewContextProducer.newContext();

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectDiscriminator() throws Exception {

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .discriminator()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockType1.class.getSimpleName())))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectCustomDiscriminator() throws Exception {

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
        .discriminator(new MockDiscriminatorStrategy())
        .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(sameInstance(MOCK_DISCRIMINATOR.getValue())))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectDiscriminator() throws Exception {

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object("child", MockType2.class)
                .discriminator()
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName("child")),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockType2.class.getSimpleName())))),
            eventOfType(END_OBJECT, withName("child")),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectObjectTemplateDiscriminator() throws Exception {

    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
            .discriminator()
            .end()
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .object("child", childTemplate)
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_OBJECT, withName("child")),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockType2.class.getSimpleName())))),
            eventOfType(END_OBJECT, withName("child")),
            eventOfType(END_OBJECT)
        )
    );
  }


  @Test
  @SuppressWarnings("unchecked")
  public void testArrayOfObjectsDiscriminator() throws Exception {

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(MockType1.class)
            .discriminator()
            .end()
        .build();

    assertThat(template.generateView(Collections.singleton(model), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY),
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockType1.class.getSimpleName())))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectArrayOfObjectsDiscriminator() throws Exception {

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects("children", MockType2.class)
                .discriminator()
                .end()
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName("children")),
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockType2.class.getSimpleName())))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY, withName("children")),
            eventOfType(END_OBJECT)
          )
      );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectArrayOfObjectsTemplateDiscriminator() throws Exception {

    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType2.class)
            .discriminator()
            .end()
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .accessType(AccessType.FIELD)
            .arrayOfObjects("children", childTemplate)
            .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(BEGIN_ARRAY, withName("children")),
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockType2.class.getSimpleName())))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY, withName("children")),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testArrayOfObjectsTemplateDiscriminator() throws Exception {

    final ViewTemplate childTemplate = ViewTemplateBuilderProducer
        .object(MockType1.class)
            .discriminator()
            .end()
        .build();

    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(childTemplate);

    assertThat(template.generateView(Collections.singleton(model), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY),
            eventOfType(BEGIN_OBJECT),
            eventOfType(DISCRIMINATOR,
                whereValue(is(equalTo(MockType1.class.getSimpleName())))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY)
        )
    );
  }

}


