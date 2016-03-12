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

  private final Iterable<ViewNodeElementHandler> handlers;

  public ViewNodeElementHandlerSupport(Iterable<ViewNodeElementHandler> handlers) {
    this.handlers = handlers;
  }

  public boolean willVisitElement(ViewNodeElementEvent event) {
    final Iterator<ViewNodeElementHandler> handlers = this.handlers.iterator();
    boolean visiting = true;
    while (visiting && handlers.hasNext()) {
      visiting = handlers.next().beforeVisitElement(event);
    }
    return visiting;
  }

  public Object didVisitElement(ViewNodeElementEvent event) {
    for (final ViewNodeElementHandler handler : handlers) {
      final Object elementModel = handler.onVisitElement(event);
      event = new ViewNodeElementEvent(event, elementModel);
    }
    return event.getElementModel();
  }

}
