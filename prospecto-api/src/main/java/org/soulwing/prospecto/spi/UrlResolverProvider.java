/*
 * File created on Mar 11, 2016
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
package org.soulwing.prospecto.spi;

import java.util.Map;

import org.soulwing.prospecto.api.url.UrlResolver;

/**
 * A provider for a {@link UrlResolver}.
 * @author Carl Harris
 */
public interface UrlResolverProvider {

  /**
   * Initializes this provider.
   * @param properties configuration properties
   */
  void init(Map<String, Object> properties);

  /**
   * Prepares this provider to be discarded.
   */
  void destroy();

  /**
   * Retrieves the resolver instance.
   * <p>
   * As specified in the documentation for {@link UrlResolver}, the provided
   * implementation <em>must</em> be thread safe.
   * @return URL resolver
   */
  UrlResolver getResolver();

}
