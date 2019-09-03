/*
 * File created on Sep 3, 2019
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

import java.io.ByteArrayInputStream;
import javax.json.Json;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.json.JsonPSource;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;

/**
 * Unit tests for {@link JsonPViewReaderFactoryProvider}.
 *
 * @author Carl Harris
 */
public class JsonPViewReaderFactoryProviderTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  private JsonPViewReaderFactoryProvider provider =
      new JsonPViewReaderFactoryProvider();

  private final Options options = new OptionsMap();

  @Test
  public void testGetName() throws Exception {
    assertThat(provider.getName(), is(equalTo(JsonPViewReaderFactoryProvider.NAME)));
  }

  @Test
  public void testObtainFromViewReaderFactoryProducer() throws Exception {
    assertThat(ViewReaderFactoryProducer.getFactory(JsonPViewReaderFactoryProvider.NAME),
      is(not(nullValue())));
  }

  @Test
  public void testNewWriter() throws Exception {
    final ViewReaderFactory factory = provider.newFactory(options);
    assertThat(factory, is(not(nullValue())));
    assertThat(factory.newReader(new JsonPSource(Json.createObjectBuilder().build())),
        is(instanceOf(JsonPViewReader.class)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewWriterOutputStream() throws Exception {
    final ViewReaderFactory factory = provider.newFactory(options);
    assertThat(factory, is(not(nullValue())));
    factory.newReader(new ByteArrayInputStream(new byte[0]));
  }


}
