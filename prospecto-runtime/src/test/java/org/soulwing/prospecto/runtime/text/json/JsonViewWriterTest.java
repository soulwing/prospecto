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
package org.soulwing.prospecto.runtime.text.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.InputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.stream.JsonParser;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.runtime.text.ViewWriterTestBase;

/**
 * Tests for {@link JsonViewWriter}.
 *
 * @author Carl Harris
 */
public class JsonViewWriterTest extends ViewWriterTestBase {

  public JsonViewWriterTest() {
    super(".json");
  }

  @Override
  protected ViewWriter newViewWriter(View view, OutputStream outputStream) {
    return new JsonViewWriter(view, outputStream);
  }

  @Override
  protected void validateView(InputStream actual,
      InputStream expected) throws Exception {
    JsonParser testParser = Json.createParser(expected);
    JsonParser viewParser = Json.createParser(actual);
    while (testParser.hasNext()) {
      assertThat(viewParser.hasNext(), is(true));
      JsonParser.Event testEvent = testParser.next();
      JsonParser.Event viewEvent = viewParser.next();
      assertThat(viewEvent, is(equalTo(testEvent)));
    }
  }

}
