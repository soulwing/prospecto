/*
 * File created on Mar 26, 2016
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
package org.soulwing.prospecto.jaxrs.runtime.reader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewReaderFactory;

/**
 * A {@link MessageBodyReader} for JSON.
 *
 * @author Carl Harris
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class JsonViewMessageBodyReader implements MessageBodyReader<View> {

  private static final ViewReaderFactory readerFactory =
      ViewReaderFactoryProducer.getFactory("JSON",
          Collections.<String, Object>emptyMap());

  @Override
  public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations,
      MediaType mediaType) {
    return true;
  }

  @Override
  public View readFrom(Class<View> aClass, Type type, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, String> multivaluedMap,
      InputStream inputStream) throws IOException, WebApplicationException {
    return readerFactory.newReader(inputStream).readView();
  }

}
