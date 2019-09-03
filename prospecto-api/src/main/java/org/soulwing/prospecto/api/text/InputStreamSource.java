/*
 * File created on Aug 29, 2019
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
package org.soulwing.prospecto.api.text;

import java.io.InputStream;

import org.soulwing.prospecto.api.ViewReader;

/**
 * A source for a {@link ViewReader} that reads to an input stream.
 *
 * @author Carl Harris
 */
public class InputStreamSource implements ViewReader.Source {

  private final InputStream inputStream;
  private final String encoding;

  public InputStreamSource(InputStream inputStream) {
    this(inputStream, null);
  }

  public InputStreamSource(InputStream inputStream, String encoding) {
    this.inputStream = inputStream;
    this.encoding = encoding;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public String getEncoding() {
    return encoding;
  }

}
