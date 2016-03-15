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

import org.soulwing.prospecto.api.handler.ViewNodeValueEvent;
import org.soulwing.prospecto.api.handler.ViewNodeValueHandler;

/**
 * A utility that provides handler notification support for a collection
 * of {@Link ViewNodeValueHandler} objects.
 *
 * @author Carl Harris
 */
public class ViewNodeValueHandlerSupport {

  /**
   * Notifies the {@link ViewNodeValueHandler} instances associated with the
   * view context of the given event that a model value has been extracted.
   * <p>
   * The first handler is allowed to replace the value in the subject event.
   * Successive handlers are allowed to replace the value produced by their
   * immediate predecessors.
   * @param event the subject event
   * @return value returned by the last handler in the context (or the
   *   value in the subject event if there are no handlers)
   */
  public static Object extractedValue(ViewNodeValueEvent event) {
    for (final ViewNodeValueHandler handler :
        event.getContext().getViewNodeValueHandlers()) {
      Object value = handler.onExtractValue(event);
      event = new ViewNodeValueEvent(event, value);
    }
    return event.getValue();
  }

  /**
   * Notifies the {@link ViewNodeValueHandler} instances associated with the
   * view context of the given event that a model value is to be injected.
   * <p>
   * The first handler is allowed to replace the value in the subject event.
   * Successive handlers are allowed to replace the value produced by their
   * immediate predecessors.
   * @param event the subject event
   * @return value returned by the last handler in the context (or the
   *   value in the subject event if there are no handlers)
   */
  public static Object injectedValue(ViewNodeValueEvent event) {
    for (final ViewNodeValueHandler handler :
        event.getContext().getViewNodeValueHandlers()) {
      Object value = handler.onInjectValue(event);
      event = new ViewNodeValueEvent(event, value);
    }
    return event.getValue();
  }

}
