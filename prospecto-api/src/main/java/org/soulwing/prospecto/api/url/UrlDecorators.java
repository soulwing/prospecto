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

import java.util.List;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.template.ViewNode;

/**
 * An ordered set of {@link UrlDecorator} instances.
 * <p>
 * During URL resolution, the {@link #decorate(String)} method of this
 * interface is called to allow the registered decorators to augment
 * or otherwise manipulate the URL template associated with a view node.
 * Each decorator is visited in order. The resulting URL template is then
 * resolved replacing any placeholders with model values as appropriate.
 *
 * @author Carl Harris
 */
public interface UrlDecorators {

  /**
   * Appends the given decorator to the end of the collection.
   * @param decorator the decorator to append
   */
  void append(UrlDecorator decorator);

  /**
   * Prepends the given decorator to the beginning of the collection.
   * @param decorator the decorator to prepend
   */
  void prepend(UrlDecorator decorator);

  /**
   * Removes a decorator from the collection.
   * @param decorator the decorator to remove
   * @return {@code true} if decorator was present (and removed) from the
   *    collection
   */
  boolean remove(UrlDecorator decorator);

  /**
   * Gets the ordered sequence of decorators as a list.
   * @return decorator sequence
   */
  List<UrlDecorator> toList();

  /**
   * Decorates the given URL template string by invoking each of the registered
   * decorators in order.
   * @param url source URL
   * @param node the URL view node that is being resolved
   * @param context view context
   * @return the result of composing the decorators in order to produce
   *    a final result
   */
  String decorate(String url, ViewNode node, ViewContext context);

}
