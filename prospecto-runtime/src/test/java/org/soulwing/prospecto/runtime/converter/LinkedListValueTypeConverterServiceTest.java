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
package org.soulwing.prospecto.runtime.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.template.ViewNode;

/**
 * Unit tests for {@link LinkedListValueTypeConverterService}.
 *
 * @author Carl Harris
 */
public class LinkedListValueTypeConverterServiceTest {

  private static final Object MODEL_VALUE = new Object();

  private static final Object VIEW_VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ViewNode node;

  @Mock
  ValueTypeConverter<Object> converter;

  private LinkedListValueTypeConverterService service =
      new LinkedListValueTypeConverterService();

  @Test
  public void testToViewValueWithLocalConverter() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(ValueTypeConverter.class);
        will(returnValue(converter));
        oneOf(converter).toValue(MODEL_VALUE);
        will(returnValue(VIEW_VALUE));
      }
    });

    assertThat(service.toViewValue(MODEL_VALUE, node), is(equalTo(VIEW_VALUE)));
  }

  @Test
  public void testToViewValueWithContextConverter() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(ValueTypeConverter.class);
        will(returnValue(null));
        oneOf(converter).supports(MODEL_VALUE.getClass());
        will(returnValue(true));
        oneOf(converter).toValue(MODEL_VALUE);
        will(returnValue(VIEW_VALUE));
      }
    });

    service.append(converter);
    assertThat(service.toViewValue(MODEL_VALUE, node), is(equalTo(VIEW_VALUE)));
  }

}
