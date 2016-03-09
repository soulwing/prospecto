/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.api;

import java.io.OutputStream;

/**
 * A factory that produces {@link ViewWriter} instances.
 *
 * @author Carl Harris
 */
public interface ViewWriterFactory {

  /**
   * Creates a writer that will produce a textual representation of a view
   * on the given output stream.
   * @param view the source view
   * @param outputStream target output stream
   * @return view writer
   */
  ViewWriter newWriter(View view, OutputStream outputStream);

}
