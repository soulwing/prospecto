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
package org.soulwing.prospecto.tests.view;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
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
public class ToStringValueTest {

  public class MockModel {
    @Override
    public String toString() {
      return MockModel.class.getSimpleName();
    }
  }

  ViewContext context = ViewContextProducer.newContext();

  @Test
  public void testObject() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockModel.class)
            .toStringValue()
            .end()
        .build();

    assertThat(template.generateView(new MockModel(), context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT,
                withName(Introspector.decapitalize(MockModel.class.getSimpleName())),
                inDefaultNamespace(),
                whereValue(is(nullValue()))),
            eventOfType(VALUE,
                withName(ViewDefaults.TO_STRING_NODE_NAME),
                inDefaultNamespace(),
                whereValue(is(equalTo(MockModel.class.getSimpleName())))),
            eventOfType(END_OBJECT,
                withName(Introspector.decapitalize(MockModel.class.getSimpleName())),
                inDefaultNamespace(),
                whereValue(is(nullValue())))
        )
    );

  }

}
