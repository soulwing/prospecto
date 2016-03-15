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
package org.soulwing.prospecto.runtime.handler;

import java.util.Iterator;

import org.soulwing.prospecto.api.handler.ViewNodeElementEvent;
import org.soulwing.prospecto.api.handler.ViewNodeElementHandler;

/**
 * A utility that provides handler notification support for a collection
 * of {@Link ViewNodeElementHandler} objects.
 *
 * @author Carl Harris
 */
public class ViewNodeElementHandlerSupport {

  /**
   * Notifies {@link ViewNodeElementHandler} instances in the given event's view
   * context that an element associated with node is to be visited.
   * <p>
   * Handlers are notified in order until a handler vetoes visitation of the
   * element in the subject event or until all handlers have been visited.
   * @param event the subject event
   * @return {@code false} if any handler vetoed visitation of the element
   */
  public static boolean willVisitElement(ViewNodeElementEvent event) {
    final Iterator<ViewNodeElementHandler> handlers =
        event.getContext().getViewNodeElementHandlers().iterator();
    boolean visiting = true;
    while (visiting && handlers.hasNext()) {
      visiting = handlers.next().beforeVisitElement(event);
    }
    return visiting;
  }

  /**
   * Notifies {@link ViewNodeElementHandler} instances in the given event's view
   * context that an element associated with the a node has been extracted.
   * <p>
   * The first handler is allowed to replace the element in the subject event.
   * Successive handlers are allowed to replace the element produced by their
   * immediate predecessors.
   * @param event the subject event
   * @return element returned by the last handler in the context (or the
   *   element in the subject event if there are no handlers)
   */
  public static Object extractedElement(ViewNodeElementEvent event) {
    for (final ViewNodeElementHandler handler :
        event.getContext().getViewNodeElementHandlers()) {
      final Object elementModel = handler.onExtractElement(event);
      event = new ViewNodeElementEvent(event, elementModel);
    }
    return event.getElement();
  }

}
