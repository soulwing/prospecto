/*
 * File created on Mar 21, 2016
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
package org.soulwing.prospecto.runtime.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.view.ConcreteView;

/**
 * Common infrastructure and tests for view writers.
 *
 * @author Carl Harris
 */
public abstract class ViewWriterTestBase {

  private final String fileSuffix;

  protected final Options options = new OptionsMap();

  public ViewWriterTestBase(String fileSuffix) {
    this.fileSuffix = fileSuffix;
    options.put(WriterKeys.INCLUDE_NULL_PROPERTIES, true);
    options.put(WriterKeys.INCLUDE_XML_XSI_TYPE, true);
  }

  private ViewWriter newViewWriter(View view, OutputStream outputStream) {
    return newViewWriter(view, outputStream, options);
  }

  protected abstract ViewWriter newViewWriter(View view,
      OutputStream outputStream, Options options);


  protected View.Event newEvent(View.Event.Type type) {
    return newEvent(type, null);
  }

  protected View.Event newEvent(View.Event.Type type, String name) {
    return newEvent(type, name, null);
  }

  protected View.Event newEvent(View.Event.Type type,
      String name, Object value) {
    return new ConcreteViewEvent(type, name, Constants.NAMESPACE, value);
  }

  protected void writeAndValidateView(String viewName,
      List<View.Event> events) throws Exception {

    final View view = new ConcreteView(events);
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final ViewWriter writer = newViewWriter(view, outputStream);
    writer.writeView();

    System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName());
    System.out.write(outputStream.toByteArray());
    System.out.println();
    System.out.flush();

    final InputStream actual = new ByteArrayInputStream(
        outputStream.toByteArray());

    final InputStream expected = getTestResource(viewName);

    validateView(actual, expected);
  }

  protected abstract void validateView(InputStream actual,
      InputStream expected) throws Exception;

  protected InputStream getTestResource(String resourceName) {
    final InputStream inputStream = getClass().getResourceAsStream(
        resourceName + fileSuffix);
    assertThat(inputStream, is(not(nullValue())));
    return inputStream;
  }

  @Test
  public void testFlatObjectView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT));

    writeAndValidateView("flatObjectView", events);
  }

  @Test
  public void testNestedObjectView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT,
        Constants.OBJECT_NAME));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT,
        Constants.OBJECT_NAME));
    events.add(newEvent(View.Event.Type.END_OBJECT));

    writeAndValidateView("nestedObjectView", events);
  }

  @Test
  public void testArrayOfObjectsView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_ARRAY));
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT));
    events.add(newEvent(View.Event.Type.END_ARRAY));

    writeAndValidateView("arrayOfObjectsView", events);
  }

  @Test
  public void testArrayOfValuesView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_ARRAY));
    addArrayValues(events);
    events.add(newEvent(View.Event.Type.END_ARRAY));

    writeAndValidateView("arrayOfValuesView", events);
  }

  protected void addArrayValues(List<View.Event> events) {
    events.add(newEvent(View.Event.Type.VALUE, null,
        Constants.STRING_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, null,
        Constants.INTEGRAL_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, null,
        Constants.DECIMAL_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, null,
        Constants.BOOLEAN_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, null,
        null));
  }

  @Test
  public void testOmitNullValueView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    addObjectProperties(events);
    events.add(newEvent(View.Event.Type.END_OBJECT));
    options.put(WriterKeys.INCLUDE_NULL_PROPERTIES, false);
    writeAndValidateView("omitNullView", events);
  }


  protected void addObjectProperties(List<View.Event> events) {
    events.add(newEvent(View.Event.Type.VALUE, Constants.STRING_NAME,
        Constants.STRING_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, Constants.INTEGRAL_NAME,
        Constants.INTEGRAL_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, Constants.DECIMAL_NAME,
        Constants.DECIMAL_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, Constants.BOOLEAN_NAME,
        Constants.BOOLEAN_VALUE));
    events.add(newEvent(View.Event.Type.VALUE, Constants.NULL_NAME,
        null));
  }

  @Test
  public void testDiscriminatorView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    events.add(newEvent(View.Event.Type.DISCRIMINATOR,
        ViewDefaults.DISCRIMINATOR_NAME,
        Constants.DISCRIMINATOR_VALUE));
    events.add(newEvent(View.Event.Type.END_OBJECT));

    writeAndValidateView("defaultDiscriminatorView", events);
  }

  @Test
  public void testUrlView() throws Exception {
    final List<View.Event> events = new ArrayList<>();
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
    events.add(newEvent(View.Event.Type.META,
        ViewDefaults.URL_NAME, Constants.URL_VALUE));
    events.add(newEvent(View.Event.Type.END_OBJECT));

    writeAndValidateView("defaultUrlView", events);
  }

}
