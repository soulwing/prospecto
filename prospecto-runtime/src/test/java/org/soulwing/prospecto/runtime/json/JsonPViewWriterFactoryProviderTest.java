/*
 * File created on Aug 29, 2019
 *
 * Copyright (c) 2019 Carl Harris, Jr
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
package org.soulwing.prospecto.runtime.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.ByteArrayOutputStream;

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;

/**
 * Unit tests for {@link JsonPViewWriterFactoryProvider}.
 *
 * @author Carl Harris
 */
public class JsonPViewWriterFactoryProviderTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private View view;

  private JsonPViewWriterFactoryProvider provider =
      new JsonPViewWriterFactoryProvider();

  private final Options options = new OptionsMap();

  @Test
  public void testGetName() throws Exception {
    assertThat(provider.getName(), is(equalTo(JsonPViewWriterFactoryProvider.NAME)));
  }

  @Test
  public void testObtainFromJsonViewWriterFactoryProducer() throws Exception {
    assertThat(ViewWriterFactoryProducer.getFactory(JsonPViewWriterFactoryProvider.NAME),
        is(not(nullValue())));
  }


  @Test
  public void testNewWriter() throws Exception {
    final ViewWriterFactory factory = provider.newFactory(options);
    assertThat(factory, is(not(nullValue())));
    assertThat(factory.newWriter(view), is(instanceOf(JsonPViewWriter.class)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewWriterOutputStream() throws Exception {
    final ViewWriterFactory factory = provider.newFactory(options);
    assertThat(factory, is(not(nullValue())));
    factory.newWriter(view, new ByteArrayOutputStream());
  }

}
