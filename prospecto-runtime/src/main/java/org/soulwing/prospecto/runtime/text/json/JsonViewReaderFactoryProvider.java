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

import java.io.InputStream;
import java.util.Map;

import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.spi.ViewReaderFactoryProvider;

/**
 * A {@link ViewReaderFactoryProvider} for readers that parse JSON.
 *
 * @author Carl Harris
 */
public class JsonViewReaderFactoryProvider
    implements ViewReaderFactoryProvider {

  public static final String NAME = "JSON";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public ViewReaderFactory newFactory(Map<String, Object> properties) {
    return new JsonViewReaderFactory(properties);
  }

  static class JsonViewReaderFactory implements ViewReaderFactory {

    private final Map<String, Object> properties;

    public JsonViewReaderFactory(Map<String, Object> properties) {
      this.properties = properties;
    }

    @Override
    public ViewReader newReader(InputStream inputStream) {
      return new JsonViewReader(inputStream, properties);
    }

  }

}
