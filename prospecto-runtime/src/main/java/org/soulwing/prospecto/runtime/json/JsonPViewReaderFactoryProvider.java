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

import java.io.InputStream;

import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.spi.ViewReaderFactoryProvider;

/**
 * A {@link ViewReaderFactoryProvider} for readers read a JSON-P structure.
 *
 * @author Carl Harris
 */
public class JsonPViewReaderFactoryProvider
    implements ViewReaderFactoryProvider {

  public static final String NAME = "JSON-P";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public ViewReaderFactory newFactory(Options options) {
    return new JsonPViewReaderFactory(options);
  }

  private static class JsonPViewReaderFactory implements ViewReaderFactory {

    private final Options options;

    JsonPViewReaderFactory(Options options) {
      this.options = options;
    }

    @Override
    public ViewReader newReader(InputStream inputStream) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ViewReader newReader(ViewReader.Source source) {
      return new JsonPViewReader(source, options);
    }

  }

}
