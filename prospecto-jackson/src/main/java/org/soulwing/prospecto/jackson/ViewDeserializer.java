/*
 * File created on Apr 27, 2016
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
package org.soulwing.prospecto.jackson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReaderFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * A Jackson {@link com.fasterxml.jackson.databind.JsonDeserializer} for a
 * Prospecto {@link View}.
 *
 * @author Carl Harris
 */
public class ViewDeserializer extends StdDeserializer<View> {

  private final ViewReaderFactory readerFactory =
      ViewReaderFactoryProducer.getFactory("JSON");

  public ViewDeserializer() {
    super(View.class);
  }

  @Override
  public View deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {

    final JsonNode tree = jsonParser.readValueAsTree();
    final ObjectMapper mapper = new ObjectMapper();
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    mapper.writeValue(outputStream, tree);

    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());
    return readerFactory.newReader(inputStream).readView();
  }

}
