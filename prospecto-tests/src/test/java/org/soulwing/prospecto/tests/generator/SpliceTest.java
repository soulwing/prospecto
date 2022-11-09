/*
 * File created on Mar 24, 2017
 *
 * Copyright (c) 2017 Carl Harris, Jr
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
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.splice.ViewGeneratingSpliceHandler;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 * Tests for generating a view at a splice.
 *
 * @author Carl Harris
 */
public class SpliceTest {

  public static final String NAME = "root";
  public static final String NAMESPACE = "namespace";
  public static final String STRING = "string";
  public static final String STRING_VALUE = "aString";


  @SuppressWarnings("unused")
  public static class MockType1 {
    String string = STRING_VALUE;
  }

  @SuppressWarnings("unused")
  public static class MockType2 {
    String string = STRING_VALUE;
  }

  static final ViewGeneratingSpliceHandler SPLICE_HANDLER =
      new ViewGeneratingSpliceHandler() {
        @Override
        protected Object getRoot(SpliceNode node, ViewContext context) {
          return new MockType2();
        }
      };

  static final ViewTemplate SPLICE_TEMPLATE = ViewTemplateBuilderProducer
      .object(NAME, NAMESPACE, MockType2.class)
          .accessType(AccessType.FIELD)
          .value(STRING)
          .end()
      .build();

  private static final String SPLICE = "splice";
  static final ViewTemplate ROOT_TEMPLATE = ViewTemplateBuilderProducer
      .object(MockType1.class)
          .accessType(AccessType.FIELD)
          .value(STRING)
          .splice(SPLICE, NAMESPACE, SPLICE_HANDLER)
              .attribute(SPLICE_TEMPLATE)
          .end()
      .build();

  private ViewContext context = ViewContextProducer.newContext();

  @Test
  public void testGenerateView() throws Exception {
    assertThat(ROOT_TEMPLATE.generateView(new MockType1(), context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(VALUE, withName(STRING),
                inDefaultNamespace(), whereValue(is(equalTo(STRING_VALUE)))),
            eventOfType(BEGIN_OBJECT, withName(SPLICE),
                inNamespace(NAMESPACE)),
            eventOfType(VALUE, withName(STRING),
                inDefaultNamespace(), whereValue(is(equalTo(STRING_VALUE)))),
            eventOfType(END_OBJECT, withName(SPLICE),
                inNamespace(NAMESPACE)),
            eventOfType(END_OBJECT)
        )
    );

  }

}
