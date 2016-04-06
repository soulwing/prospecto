/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.api.node;

import java.util.Iterator;

/**
 * A node that represents an array structure.
 *
 * @author Carl Harris
 */
public interface ArrayNode extends ViewNode {

  /**
   * Gets the name to use for elements of the array.
   * @return element name or {@code null} if none was specified in the source
   *    template
   */
  String getElementName();

  /**
   * Gets an iterator for the values associated with this node in the from
   * the given model.
   * @param model model containing the collection/array of values to iterate
   * @return iterator for the subject values
   * @throws Exception
   */
  Iterator<?> iterator(Object model) throws Exception;

}
