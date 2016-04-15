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
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodeListener;

/**
 * A simple logging {@link ViewNodeListener}.
 *
 * @author Carl Harris
 */
class LoggingViewNodeListener implements ViewNodeListener {

  private static final Logger logger =
      LoggerFactory.getLogger(LoggingViewNodeListener.class);

  static final LoggingViewNodeListener INSTANCE = new LoggingViewNodeListener();

  private LoggingViewNodeListener() {}

  @Override
  public void nodeVisited(ViewNodeEvent event) {
    logger.debug("visited node {}",
        event.getContext().currentViewPathAsString());
  }

}
