/*
 * File created on Mar 12, 2016
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
package org.soulwing.prospecto.jaxrs.runtime.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriterFactory;

/**
 * A message body writer for an XML representation of a {@link View}.
 *
 * @author Carl Harris
 */
@Provider
@Produces(MediaType.APPLICATION_XML)
public class XmlViewMessageBodyWriter implements MessageBodyWriter<View> {

  private final ViewWriterFactory writerFactory =
      ViewWriterFactoryProducer.getFactory("XML");

  @Override
  public boolean isWriteable(Class<?> aClass, Type type,
      Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  @Override
  public long getSize(View view, Class<?> aClass, Type type,
      Annotation[] annotations, MediaType mediaType) {
    return 0;
  }

  @Override
  public void writeTo(View view, Class<?> aClass, Type type,
      Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> multivaluedMap,
      OutputStream outputStream) throws IOException, WebApplicationException {
    writerFactory.newWriter(view, outputStream).writeView();
  }

}
