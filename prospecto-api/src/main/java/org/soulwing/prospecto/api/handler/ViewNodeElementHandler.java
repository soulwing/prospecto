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
package org.soulwing.prospecto.api.handler;

/**
 * A handler that is invoked as the elements of an array view node are visited
 * to create the subview for each element.
 * <p>
 * A handler can be used to implement a variety of filtering and transformation
 * tasks.
 * @author Carl Harris
 */
public interface ViewNodeElementHandler {

  /**
   * Notifies the recipient that an element associated with an array node is
   * about to be visited.
   * @param event the subject event
   * @return {@code true} to allow visitation of the element; the handler can
   *   return {@code false} to prevent visitation of the element, effectively
   *   removing it from the view
   */
  boolean beforeVisitElement(ViewNodeElementEvent event);

  /**
   * Notifies the recipient that an element associated with an array-of-values
   * node is being visited.
   * @param event the subject event
   * @return a <em>value</em> to use as a substitute for the element view model
   *    contained in the event; if the handler does not wish to substitute the
   *    view model it <em>must</em> return the
   *    {@linkplain ViewNodeElementEvent#getElement() subject element model}.
   */
  Object onExtractElement(ViewNodeElementEvent event);

}
