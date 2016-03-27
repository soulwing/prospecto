/*
 * File created on Mar 23, 2016
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
package org.soulwing.prospecto.api.listener;

/**
 * A listener that is notified before property values are visited during view
 * generation or model update.
 *
 * @author Carl Harris
 */
public interface ViewNodePropertyAcceptor extends ViewListener {

  /**
   * Notifies the recipient that a model property value associated with a view
   * node will be visited.
   * <p>
   * This method is invoked as each property of an object node is visited, as
   * well for each element of an array-of-objects or array-of-values node.
   *
   * @param event event describing the property value
   * @return {@code true} if this filter wishes to allow the property
   *    to be visited
   */
  boolean shouldVisitProperty(ViewNodePropertyEvent event);

}
