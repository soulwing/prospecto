/*
 * File created on Apr 14, 2016
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
package org.soulwing.prospecto.demo.jaxrs.service.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyListener;

/**
 * A simple logging {@link ViewNodePropertyListener}.
 *
 * @author Carl Harris
 */
class LoggingViewNodePropertyListener
    implements ViewNodePropertyListener {

  private static final Logger logger =
      LoggerFactory.getLogger(LoggingViewNodePropertyListener.class);

  static final LoggingViewNodePropertyListener INSTANCE =
      new LoggingViewNodePropertyListener();

  private LoggingViewNodePropertyListener() {}

  @Override
  public void propertyVisited(ViewNodePropertyEvent event) {
    logger.debug("visited property at path {}: [{}]",
        event.getContext().currentViewPathAsString(), event.getValue());
  }

}
