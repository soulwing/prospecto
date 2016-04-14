/*
 * File created on Apr 14, 2016
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
import static org.soulwing.prospecto.api.View.Event.Type.META;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.hasEventSequence;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inDefaultNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.inNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.withName;

import java.util.Collections;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.MetadataHandler;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.template.MetaNode;

/**
 * Tests for structures using envelopes.
 *
 * @author Carl Harris
 */
public class MetaTest {

  private static final String META_NAME = "metaName";
  private static final Object META_VALUE = "metaValue";
  private static final Object HANDLER_VALUE = "handlerValue";
  private static final String NAMESPACE = "namespace";

  MockType model = new MockType();

  MockMetadataHandler handler = new MockMetadataHandler();

  ViewContext context = ViewContextProducer.newContext();

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectMetaValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType.class)
        .meta(META_NAME, META_VALUE)
        .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inDefaultNamespace(), whereValue(is(equalTo(META_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectMetaNamespaceValue() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType.class)
        .meta(META_NAME, NAMESPACE, META_VALUE)
        .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inNamespace(NAMESPACE), whereValue(is(equalTo(META_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectMetaHandler() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType.class)
            .meta(META_NAME, handler)
            .end()
        .build();

    handler.setValue(HANDLER_VALUE);
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inDefaultNamespace(), whereValue(is(equalTo(HANDLER_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
    assertThat(handler.getValue(), is(nullValue()));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectMetaNamespaceHandler() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType.class)
        .meta(META_NAME, NAMESPACE, handler)
        .end()
        .build();

    handler.setValue(HANDLER_VALUE);
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inNamespace(NAMESPACE), whereValue(is(equalTo(HANDLER_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
    assertThat(handler.getValue(), is(nullValue()));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectMetaNamespaceValueHandler() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType.class)
        .meta(META_NAME, NAMESPACE, META_VALUE, handler)
        .end()
        .build();

    handler.setValue(HANDLER_VALUE);
    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inNamespace(NAMESPACE), whereValue(is(equalTo(HANDLER_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
    assertThat(handler.getValue(), is(equalTo(META_VALUE)));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectMetaNamespaceValueHandlerClass() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType.class)
        .meta(META_NAME, NAMESPACE, META_VALUE, MockMetadataHandler.class,
            "value", HANDLER_VALUE)
        .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inNamespace(NAMESPACE), whereValue(is(equalTo(HANDLER_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testObjectMetaNamespaceValueHandlerMap() throws Exception {
    final ViewTemplate template = ViewTemplateBuilderProducer
        .object(MockType.class)
        .meta(META_NAME, NAMESPACE, META_VALUE, MockMetadataHandler.class,
            Collections.singletonMap("value", HANDLER_VALUE))
        .end()
        .build();

    assertThat(template.generateView(model, context),
        hasEventSequence(
            eventOfType(BEGIN_OBJECT),
            eventOfType(META, withName(META_NAME),
                inNamespace(NAMESPACE), whereValue(is(equalTo(HANDLER_VALUE)))),
            eventOfType(END_OBJECT)
        )
    );
  }

  public static class MockType {}

  public static class MockMetadataHandler implements MetadataHandler {

    private Object value;

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }

    @Override
    public Object produceValue(MetaNode node, ViewContext context) throws Exception {
      final Object value = getValue();
      setValue(node.getValue());
      return value;
    }

    @Override
    public void consumeValue(MetaNode node, Object value, ViewContext context)
        throws Exception {
      setValue(value);
    }

  }
}
