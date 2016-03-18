/*
 * File created on Mar 18, 2016
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
package org.soulwing.prospecto.runtime.event;

import org.soulwing.prospecto.api.View;

/**
 * A factory that produces {@link View.Event} objects.
 *
 * @author Carl Harris
 */
public interface ViewEventFactory {

  /**
   * Creates a new event object.
   * @param type event type
   * @param name name; may be {@code null}
   * @param namespace; may be {@code null}
   * @param value; may be {@code null}
   * @return view event
   */
  View.Event newEvent(View.Event.Type type, String name, String namespace,
      Object value);

}
