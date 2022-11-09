/*
 * File created on Apr 7, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.UpdatableValueNode;
import org.soulwing.prospecto.api.template.ValueNode;
import org.soulwing.prospecto.runtime.converter.ValueTypeConverterService;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * Unit tests for {@link ValueApplicator}.
 *
 * @author Carl Harris
 */
public class ValueApplicatorTest
    extends AbstractViewEventApplicatorTest<ValueNode> {

  private static final Object VIEW_VALUE = new Object();
  private static final Object MODEL_VALUE = new Object();

  private static final String JSON_STRING = "string";
  private static final int JSON_INTEGER = 42;
  private static final long JSON_LONG = 42L;
  private static final double JSON_DOUBLE = 2.71828;
  private static final float JSON_FLOAT = 3.14159f;
  private static final BigInteger JSON_BIG_INTEGER = BigInteger.ONE;
  private static final BigDecimal JSON_BIG_DECIMAL = BigDecimal.ONE;

  private static final View.Event VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, VIEW_VALUE);

  private static final View.Event NULL_VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, null);

  private static final List<View.Event> OBJECT = Arrays.asList(
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null, null),
      VALUE_EVENT,
      new ConcreteViewEvent(View.Event.Type.END_OBJECT, null, null, null)
  );

  private static final List<View.Event> ARRAY = Arrays.asList(
      new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, null, null, null),
      VALUE_EVENT,
      new ConcreteViewEvent(View.Event.Type.END_ARRAY, null, null, null)
  );

  private static final List<View.Event> JSON_OBJECT = Arrays.asList(
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null, null),
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, JSON_STRING),
      new ConcreteViewEvent(View.Event.Type.END_OBJECT, null, null, null)
  );

  private static final List<View.Event> JSON_ARRAY = Arrays.asList(
      new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, null, null, null),
      new ConcreteViewEvent(View.Event.Type.VALUE, null, null, JSON_STRING),
      new ConcreteViewEvent(View.Event.Type.END_ARRAY, null, null, null)
  );

  @Mock
  private TransformationService transformationService;

  @Mock
  ValueTypeConverterService valueTypeConverters;

  @Override
  ValueNode newNode() {
    return context.mock(UpdatableValueNode.class);
  }

  @Override
  AbstractViewEventApplicator<ValueNode> newApplicator(ValueNode node) {
    return new ValueApplicator(node, transformationService);
  }

  @Test
  public void testOnToModelValue() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(node).getDataType();
        will(returnValue(MockDataType.class));
        allowing(viewContext).getValueTypeConverters();
        will(returnValue(valueTypeConverters));
        oneOf(valueTypeConverters).toModelValue(MockDataType.class, VIEW_VALUE, node, viewContext);
        will(returnValue(VIEW_VALUE));
        oneOf(transformationService).valueToInject(parentEntity,
            MockDataType.class, VIEW_VALUE, node, viewContext);
        will(returnValue(MODEL_VALUE));
      }
    });

    assertThat(applicator.toModelValue(parentEntity, VALUE_EVENT, events,
        viewContext), is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testOnToModelValueWhenJsonNull() throws Exception {
    validateJsonValueEvent(JsonValue.class, null, JsonValue.NULL);
  }

  @Test
  public void testOnToModelValueWhenJsonString() throws Exception {
    validateJsonValueEvent(JsonString.class, JSON_STRING,
        Json.createValue(JSON_STRING));
  }

  @Test
  public void testOnToModelValueWhenJsonFalse() throws Exception {
    validateJsonValueEvent(JsonValue.class, false, JsonValue.FALSE);
  }

  @Test
  public void testOnToModelValueWhenJsonTrue() throws Exception {
    validateJsonValueEvent(JsonValue.class, true, JsonValue.TRUE);
  }

  @Test
  public void testOnToModelValueWhenJsonInteger() throws Exception {
    validateJsonValueEvent(JsonNumber.class, JSON_INTEGER,
        Json.createValue(JSON_INTEGER));
  }

  @Test
  public void testOnToModelValueWhenJsonLong() throws Exception {
    validateJsonValueEvent(JsonNumber.class, JSON_LONG,
        Json.createValue(JSON_LONG));
  }

  @Test
  public void testOnToModelValueWhenJsonFloat() throws Exception {
    validateJsonValueEvent(JsonNumber.class, JSON_FLOAT,
        Json.createValue(JSON_FLOAT));
  }

  @Test
  public void testOnToModelValueWhenJsonDouble() throws Exception {
    validateJsonValueEvent(JsonNumber.class, JSON_DOUBLE,
        Json.createValue(JSON_DOUBLE));
  }

  @Test
  public void testOnToModelValueWhenJsonBigInteger() throws Exception {
    validateJsonValueEvent(JsonNumber.class, JSON_BIG_INTEGER,
        Json.createValue(JSON_BIG_INTEGER));
  }

  @Test
  public void testOnToModelValueWhenJsonBigDecimal() throws Exception {
    validateJsonValueEvent(JsonNumber.class, JSON_BIG_DECIMAL,
        Json.createValue(JSON_BIG_DECIMAL));
  }

  @Test
  public void testOnToModelValueWhenJsonObject() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(node).getDataType();
        will(returnValue(JsonObject.class));
        oneOf(transformationService).valueToInject(
            with(parentEntity),
            with(JsonObject.class),
            with(equalTo(Json.createObjectBuilder()
                .add(NAME, JSON_STRING)
                .build())),
            with(node),
            with(viewContext));
        will(returnValue(MODEL_VALUE));
      }
    });

    events.addAll(JSON_OBJECT);

    assertThat(applicator.toModelValue(parentEntity, events.removeFirst(),
        events, viewContext), is(sameInstance(MODEL_VALUE)));
  }

  @Test
  public void testOnToModelValueWhenJsonArray() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(node).getDataType();
        will(returnValue(JsonArray.class));
        oneOf(transformationService).valueToInject(
            with(parentEntity),
            with(JsonArray.class),
            with(equalTo(Json.createArrayBuilder()
                .add(JSON_STRING)
                .build())),
            with(node),
            with(viewContext));
        will(returnValue(MODEL_VALUE));
      }
    });

    events.addAll(JSON_ARRAY);

    assertThat(applicator.toModelValue(parentEntity, events.removeFirst(),
        events, viewContext), is(sameInstance(MODEL_VALUE)));
  }

  private void validateJsonValueEvent(Class<? extends JsonValue> type, Object value,
      JsonValue expectedValue) throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(node).getDataType();
        will(returnValue(type));
        oneOf(transformationService).valueToInject(parentEntity,
            type, expectedValue, node, viewContext);
        will(returnValue(MODEL_VALUE));
      }
    });

    assertThat(applicator.toModelValue(parentEntity, valueEventWithValue(value), events,
        viewContext), is(sameInstance(MODEL_VALUE)));
  }

  private View.Event valueEventWithValue(Object value) {
    return new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, value);
  }

  @Test
  public void testOnToModelValueWhenNull() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(node).getDataType();
        will(returnValue(MockDataType.class));
        oneOf(transformationService).valueToInject(parentEntity,
            MockDataType.class, null, node, viewContext);
        will(returnValue(null));
      }
    });

    assertThat(applicator.toModelValue(parentEntity, NULL_VALUE_EVENT, events,
        viewContext), is(nullValue()));
  }

  @Test
  public void testOnToModelValueWhenObject() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(node).getDataType();
        will(returnValue(Map.class));
        oneOf(transformationService).valueToInject(
            with(parentEntity),
            with(Map.class),
            with(hasEntry(equalTo(NAME), sameInstance(VIEW_VALUE))),
            with(node),
            with(viewContext));
        will(returnValue(Collections.singletonMap(NAME, MODEL_VALUE)));
      }
    });

    events.addAll(OBJECT);

    assertThat((Map<?, ?>) applicator.toModelValue(parentEntity,
        events.removeFirst(), events, viewContext), hasEntry(NAME, MODEL_VALUE));
  }

  @Test
  public void testOnToModelValueWhenArray() throws Exception {
    context.checking(baseExpectations());
    context.checking(contextScopeExpectations());
    context.checking(new Expectations() {
      {
        allowing(node).getDataType();
        will(returnValue(List.class));
        oneOf(transformationService).valueToInject(
            with(parentEntity),
            with(List.class),
            with(contains(VIEW_VALUE)),
            with(node),
            with(viewContext));
        will(returnValue(Collections.singletonList(MODEL_VALUE)));
      }
    });

    events.addAll(ARRAY);

    assertThat((List<?>) applicator.toModelValue(parentEntity,
        events.removeFirst(), events, viewContext), contains(MODEL_VALUE));
  }

  @Test
  public void testInjectInContext() throws Exception {
    validateInject(EnumSet.of(AccessMode.WRITE));
  }

  @Test
  public void testInjectInContextWhenNotWritable() throws Exception {
    validateInject(EnumSet.noneOf(AccessMode.class));
  }

  private void validateInject(final EnumSet<AccessMode> allowedModes)
      throws Exception {
    context.checking(new Expectations() {
      {
        oneOf((UpdatableValueNode) node).getAllowedModes();
        will(returnValue(allowedModes));
        if (!allowedModes.isEmpty()) {
          oneOf((UpdatableValueNode) node).setValue(MODEL, MODEL_VALUE);
        }
      }
    });

    applicator.inject(MODEL, MODEL_VALUE, viewContext);
  }

  interface MockDataType {}

}
