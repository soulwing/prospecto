/*
 * File created on Aug 30, 2018
 *
 * Copyright (c) 2018 Carl Harris, Jr
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
package org.soulwing.prospecto.runtime.applicator;

import java.util.Deque;
import java.util.Iterator;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.api.ViewInputException;

/**
 * Static utility methods for working with view events.
 *
 * @author Carl Harris
 */
class EventUtil {


  static final ViewInputException NOT_WELL_FORMED_EXCEPTION =
      new ViewInputException("view is not well-formed");

  /**
   * Skips an entire subtree in an event stream.
   * @param triggerEvent event that marks start of the subtree
   * @param events subsequent events
   * @throws ViewApplicatorException if the stream is not well formed
   */
  static void skipSubtree(View.Event triggerEvent, Iterator<View.Event> events)
      throws ViewApplicatorException {
    while (events.hasNext()) {
      final View.Event event = events.next();
      if (event.getType() == triggerEvent.getType().complement()) break;
      if (event.getType() != event.getType().complement()) {
        if (!event.getType().isBegin()) {
          throw NOT_WELL_FORMED_EXCEPTION;
        }
        skipSubtree(triggerEvent, events);
      }
    }
  }

  /**
   * Skips an entire subtree in an event stream.
   * @param triggerEvent event that marks start of the subtree
   * @param events subsequent events
   * @throws ViewApplicatorException if the stream is not well formed
   */
  static void consumeSubtree(View.Event triggerEvent, Deque<View.Event> events)
      throws ViewApplicatorException {
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType() == triggerEvent.getType().complement()) break;
      if (event.getType() != event.getType().complement()) {
        if (!event.getType().isBegin()) {
          throw NOT_WELL_FORMED_EXCEPTION;
        }
        consumeSubtree(triggerEvent, events);
      }
    }
  }

}
