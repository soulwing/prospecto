/*
 * File created on Aug 26, 2022
 *
 * Copyright (c) 2022 Carl Harris, Jr
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
package org.soulwing.prospecto.api.url;

/**
 * A decorator for a URL template string.
 * <p>
 * An instance of this class gets a URL template string during the URL
 * resolution process, and is allowed to manipulate the URL string by adding
 * path, query parameters, etc.
 *
 * @author Carl Harris
 */
public interface UrlDecorator {

  /**
   * Decorates a URL string.
   * <p>
   * An implementation may modify the input URL or completely replace it
   * as desired.
   * @param url source URL
   * @return resulting URL (must not be {@code null})
   */
  String decorate(String url);

}
