/*
 * File created on Apr 1, 2016
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
package org.soulwing.prospecto.tests.editor;

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.view.ConcreteView;

/**
 * A builder for a view.
 *
 * @author Carl Harris
 */
public class ViewBuilder {

  static class EventInfo {
    View.Event.Type type;
    String name;
    String namespace;
    Object value;

    View.Event generate() {
      return new ConcreteViewEvent(type, name, namespace, value);
    }
  }

  private final List<View.Event> events = new LinkedList<>();

  private EventInfo event = new EventInfo();

  public static ViewBuilder begin() {
    return new ViewBuilder();
  }

  public ViewBuilder type(View.Event.Type type) {
    next();
    event.type = type;
    return this;
  }

  public ViewBuilder name(String name) {
    event.name = name;
    return this;
  }

  public ViewBuilder namespace(String namespace) {
    event.namespace = namespace;
    return this;
  }

  public ViewBuilder value(Object value) {
    event.value = value;
    return this;
  }

  public View end() {
    next();
    return new ConcreteView(events);
  }

  private void next() {
    if (event.type != null) {
      events.add(event.generate());
      event = new EventInfo();
    }
  }

}
