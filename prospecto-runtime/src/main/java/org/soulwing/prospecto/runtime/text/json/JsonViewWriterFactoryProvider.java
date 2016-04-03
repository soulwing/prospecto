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

import java.io.OutputStream;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.spi.ViewWriterFactoryProvider;

/**
 * A {@link ViewWriterFactoryProvider} for writers that produce JSON.
 *
 * @author Carl Harris
 */
public class JsonViewWriterFactoryProvider
    implements ViewWriterFactoryProvider {

  public static final String NAME = "JSON";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public ViewWriterFactory newFactory(Options options) {
    return new JsonViewWriterFactory(options);
  }

  private static class JsonViewWriterFactory implements ViewWriterFactory {

    private final Options options;

    JsonViewWriterFactory(Options options) {
      this.options = options;
    }

    @Override
    public ViewWriter newWriter(View view, OutputStream outputStream) {
      return new JsonViewWriter(view, outputStream, options);
    }

  }

}
