/*
 * File created on Apr 5, 2016
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
package org.soulwing.prospecto.api.url;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.template.ViewNode;

/**
 * A service that resolvers the actual URL string for a value node of type
 * <em>url</em>.
 * <p>
 * An implementation <em>must</em> be thread safe as it may be used to resolve
 * URLs for multiple concurrent view generation requests.
 *
 * @author Carl Harris
 */
public interface UrlResolver {

  /**
   * Resolves a URL string for a view node of type <em>url</em>.
   * @param node the subject URL view node
   * @param context view context
   * @return URL string
   * @throws UnresolvedUrlException if the resolver cannot resolve the URL
   *    string for the subject view node
   */
  String resolve(ViewNode node, ViewContext context);

}
