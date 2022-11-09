/*
 * File created on May 10, 2016
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
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;

import java.beans.Introspector;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.options.ViewDefaults;

/**
 * Tests for use of {@link ViewTemplateBuilder#name()} to include the name
 * of an instance in an enumeration.
 *
 * @author Carl Harris
 */
public class EnumNameTest {

  public enum MockEnum {
    MOCK;


    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }

  ViewContext context = ViewContextProducer.newContext();

  @Test
  public void testObject() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockEnum.class)
            .name()
            .end()
        .build();

    assertThat(template.generateView(MockEnum.MOCK, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT,
                withName(Introspector.decapitalize(MockEnum.class.getSimpleName())),
                inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(VALUE,
                withName(ViewDefaults.ENUM_NODE_NAME),
                inDefaultNamespace(),
                whereValue(is(equalTo(MockEnum.MOCK.name())))),
            eventOfType(END_OBJECT,
                withName(Introspector.decapitalize(MockEnum.class.getSimpleName())),
                inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );

  }

  @Test
  public void testArrayOfObjects() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .arrayOfObjects(MockEnum.class)
            .name()
            .end()
        .build();

    assertThat(template.generateView(MockEnum.values(), context),
        hasEventSequence(
            eventOfType(BEGIN_ARRAY),
            eventOfType(BEGIN_OBJECT),
            eventOfType(VALUE,
                withName(ViewDefaults.ENUM_NODE_NAME),
                inDefaultNamespace(),
                whereValue(is(equalTo(MockEnum.MOCK.name())))),
            eventOfType(END_OBJECT),
            eventOfType(END_ARRAY)
        )
    );

  }

}
