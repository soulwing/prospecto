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

import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriterFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * A Jackson serializer for a Prospecto {@link View}.
 *
 * @author Carl Harris
 */
public class ViewSerializer extends StdSerializer<View> {

  private final ViewWriterFactory writerFactory =
      ViewWriterFactoryProducer.getFactory("JSON");

  public ViewSerializer() {
    super(View.class);
  }

  @Override
  public void serialize(View view, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) {

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    writerFactory.newWriter(view, outputStream).writeView();

    final ObjectMapper mapper = new ObjectMapper();
    final ByteArrayInputStream inputStream =
        new ByteArrayInputStream(outputStream.toByteArray());

    jsonGenerator.writeTree(mapper.readTree(inputStream));
  }

}
