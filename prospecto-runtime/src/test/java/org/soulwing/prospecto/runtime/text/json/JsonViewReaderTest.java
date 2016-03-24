/*
 * File created on Mar 19, 2016
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
package org.soulwing.prospecto.runtime.text.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.runtime.text.Constants;
import org.soulwing.prospecto.runtime.text.ReaderKeys;
import org.soulwing.prospecto.runtime.text.ViewReaderTestBase;

/**
 * Tests for {@link JsonViewReader}.
 *
 * @author Carl Harris
 */
public class JsonViewReaderTest extends ViewReaderTestBase {

  public JsonViewReaderTest() {
    super(".json");
  }

  @Override
  protected ViewReader newViewReader(InputStream inputStream,
      Map<String, Object> properties) {
    return new JsonViewReader(inputStream, properties);
  }

  @Test
  public void testCustomDiscriminatorView() throws Exception {
    final JsonViewReader reader = new JsonViewReader(
        getTestResource("customDiscriminatorView"),
        Collections.<String, Object>singletonMap(
            ReaderKeys.DISCRIMINATOR_NAME, Constants.CUSTOM_NAME));
    final Iterator<View.Event> events = reader.readView().iterator();
    assertThat(events.next(),
        is(eventWith(View.Event.Type.BEGIN_OBJECT)));
    assertThat(events.next(),
        is(eventWith(View.Event.Type.DISCRIMINATOR, Discriminator.DEFAULT_NAME,
            Constants.DISCRIMINATOR_VALUE)));
  }

}
