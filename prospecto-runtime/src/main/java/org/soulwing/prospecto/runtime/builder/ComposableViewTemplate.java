/*
 * File created on Mar 14, 2016
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
package org.soulwing.prospecto.runtime.builder;

import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A view template that can be composed in another view template.
 *
 * @author Carl Harris
 */
public interface ComposableViewTemplate extends ViewTemplate {

  /**
   * Extracts a view node of object type from the root of this template for
   * use as a subview in another template.
   * @param name name for the node (as it will appear in the calling template)
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @return new view node containing the children of the root node of this
   *    template
   * @see ViewTemplateBuilder#object(String, String, ViewTemplate)
   */
  AbstractViewNode object(String name, String namespace);

  /**
   * Extracts a view node of array-of-objects type from the root of this
   * template for use as a subview in another template.
   * @param name name for the node (as it will appear in the calling template)
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   *    in only some view types (e.g. XML)
   * @return new view node containing the children of the root node of this
   *    template
   * @see ViewTemplateBuilder#arrayOfObjects(String, String, String, ViewTemplate)
   */
  AbstractViewNode arrayOfObjects(String name, String elementName,
      String namespace);

}
