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
 * A handler that is invoked as view nodes are visited to create the event
 * stream for a view.
 * <p>
 * A handler can be used to implement a variety of filtering and transformation
 * tasks.
 *
 * @author Carl Harris
 */
public interface ViewNodeHandler {

  /**
   * Notifies the recipient that a view node is about to be visited.
   * @param event the subject event
   * @return {@code true} if the handler wishes to allow visitation of the
   *   view node; the handler can return {@code false} to prevent visitation
   *   of the view node, effectively removing it from the view
   */
  boolean beforeVisit(ViewNodeEvent event);

  /**
   * Notifies the recipient that a view node has been visited
   * @param event the subject event
   */
  void afterVisit(ViewNodeEvent event);
}
