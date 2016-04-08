/*
 * File created on Apr 5, 2016
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
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.BEGIN_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.END_ARRAY;
import static org.soulwing.prospecto.api.View.Event.Type.END_OBJECT;
import static org.soulwing.prospecto.api.View.Event.Type.META;
import static org.soulwing.prospecto.api.View.Event.Type.VALUE;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.sameView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.runtime.view.ViewBuilder;

/**
 * A base type for writer-reader round trip testing.
 * <p>
 * This test writes views and then reads them back to verify the
 * integrity of the representation.
 *
 * @author Carl Harris
 */
public abstract class WriterReaderRoundTripTestBase {

  private static final String CHILD = "child";
  private static final String CHILDREN = "children";

  private final String providerName;
  private final String name;
  private final String namespace;
  private final ViewWriterFactory writerFactory;
  private final ViewReaderFactory readerFactory;

  public WriterReaderRoundTripTestBase(String providerName) {
    this(providerName, null, null);
  }

  public WriterReaderRoundTripTestBase(String providerName,
        String name, String namespace) {
    this.providerName = providerName;
    this.name = name;
    this.namespace = namespace;
    this.writerFactory = ViewWriterFactoryProducer.getFactory(providerName);
    this.readerFactory = ViewReaderFactoryProducer.getFactory(providerName);
  }

  private File file;

  @Before
  public void setUp() throws Exception {
    file = createTempFile(providerName);
  }

  @After
  public void tearDown() throws Exception {
    System.out.println(file);
//    assertThat(file.delete(), is(true));
  }

  @Test
  public void testObject() throws Exception {
    final View expected = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT).name(name).namespace(namespace)
        .with(metas())
        .with(namedValues())
        .type(END_OBJECT).name(name).namespace(namespace)
        .end();
    writeView(expected);
    final View actual = readView();
    assertThat(actual, is(sameView(expected)));
  }

  @Test
  public void testObjectObject() throws Exception {
    final View expected = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT).name(name).namespace(namespace)
        .type(BEGIN_OBJECT).name(CHILD).namespace(namespace)
        .with(metas())
        .with(namedValues())
        .type(END_OBJECT).name(CHILD).namespace(namespace)
        .type(END_OBJECT).name(name).namespace(namespace)
        .end();

    writeView(expected);
    final View actual = readView();
    assertThat(actual, is(sameView(expected)));
  }

  @Test
  public void testArrayOfObjects() throws Exception {
    final View expected = ViewBuilder
        .begin()
        .type(BEGIN_ARRAY).name(name).namespace(namespace)
        .type(BEGIN_OBJECT)
        .with(metas())
        .with(namedValues())
        .type(END_OBJECT)
        .type(END_ARRAY).name(name).namespace(namespace)
        .end();

    writeView(expected);
    final View actual = readView();
    assertThat(actual, is(sameView(expected)));
  }


  @Test
  public void testArrayOfObjectsArrayOfObjects() throws Exception {
    final View expected = ViewBuilder
        .begin()
        .type(BEGIN_ARRAY).name(name).namespace(namespace)
        .type(BEGIN_OBJECT)
        .type(BEGIN_ARRAY).name(CHILDREN).namespace(namespace)
        .type(BEGIN_OBJECT)
        .with(metas())
        .with(namedValues())
        .type(END_OBJECT)
        .type(END_ARRAY).name(CHILDREN).namespace(namespace)
        .type(END_OBJECT)
        .type(END_ARRAY).name(name).namespace(namespace)
        .end();

    writeView(expected);
    final View actual = readView();
    assertThat(actual, is(sameView(expected)));
  }

  @Test
  public void testArrayOfValues() throws Exception {
    final View expected = ViewBuilder
        .begin()
        .type(BEGIN_ARRAY).name(name).namespace(namespace)
        .with(values())
        .type(END_ARRAY).name(name).namespace(namespace)
        .end();

    writeView(expected);
    final View actual = readView();
    assertThat(actual, is(sameView(expected)));
  }

  @Test
  public void testObjectArrayOfValues() throws Exception {
    final View expected = ViewBuilder
        .begin()
        .type(BEGIN_OBJECT).name(name).namespace(namespace)
        .type(BEGIN_ARRAY).name(CHILDREN).namespace(namespace)
        .with(values())
        .type(END_ARRAY).name(CHILDREN).namespace(namespace)
        .type(END_OBJECT).name(name).namespace(namespace)
        .end();

    writeView(expected);
    final View actual = readView();
    assertThat(actual, is(sameView(expected)));
  }

  @Test
  public void testArrayOfObjectsArrayOfValues() throws Exception {
    final View expected = ViewBuilder
        .begin()
        .type(BEGIN_ARRAY).name(name).namespace(namespace)
        .type(BEGIN_OBJECT)
        .type(BEGIN_ARRAY).name(CHILDREN).namespace(namespace)
        .with(values())
        .type(END_ARRAY).name(CHILDREN).namespace(namespace)
        .type(END_OBJECT)
        .type(END_ARRAY).name(name).namespace(namespace)
        .end();

    writeView(expected);
    final View actual = readView();
    assertThat(actual, is(sameView(expected)));
  }

  private ViewBuilder metas() {
    return ViewBuilder
        .begin()
        .type(META).name("objectMeta").value("metaValue");
  }

  private ViewBuilder namedValues() {
    return ViewBuilder
        .begin()
        .type(VALUE).name("stringValue").value("string")
        .type(VALUE).name("booleanValue").value(true)
        .type(VALUE).name("byteValue").value((byte) -1)
        .type(VALUE).name("shortValue").value((short) -1)
        .type(VALUE).name("intValue").value(-1)
        .type(VALUE).name("longValue").value(-1L)
        .type(VALUE).name("bigIntegerValue").value(BigInteger.valueOf(-1))
        .type(VALUE).name("floatValue").value((float) -1.0)
        .type(VALUE).name("doubleValue").value(-1.0)
        .type(VALUE).name("bigDecimalValue").value(BigDecimal.valueOf(-1.0))
        .type(VALUE).name("dateValue").value(new Date())
        .type(VALUE).name("calendarValue").value(Calendar.getInstance())
        .type(VALUE).name("uuidValue").value(UUID.randomUUID());
  }

  private ViewBuilder values() {
    return ViewBuilder
        .begin()
        .type(VALUE).value("string")
        .type(VALUE).value(true)
        .type(VALUE).value((byte) -1)
        .type(VALUE).value((short) -1)
        .type(VALUE).value(-1)
        .type(VALUE).value(-1L)
        .type(VALUE).value(BigInteger.valueOf(-1))
        .type(VALUE).value((float) -1.0)
        .type(VALUE).value(-1.0)
        .type(VALUE).value(BigDecimal.valueOf(-1.0))
        .type(VALUE).value(new Date())
        .type(VALUE).value(Calendar.getInstance())
        .type(VALUE).value(UUID.randomUUID());
  }

  private void writeView(View view) throws IOException {
    try (final OutputStream outputStream = new FileOutputStream(file)) {
      writerFactory.newWriter(view, outputStream).writeView();
    }
  }

  private View readView() throws IOException {
    try (final InputStream inputStream = new FileInputStream(file)) {
      return readerFactory.newReader(inputStream).readView();
    }
  }

  private File createTempFile(String providerName) throws IOException {
    return Files.createTempFile(getClass().getSimpleName(),
        '.' + providerName).toFile();
  }

}
