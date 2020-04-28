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
package org.soulwing.prospecto.runtime.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A {@link View} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteView implements View {

  private final Envelope envelope = new ConcreteEnvelope();

  private final List<Event> events;

  public ConcreteView(List<Event> events) {
    this.events = events;
  }

  @Override
  public Envelope getEnvelope() {
    return envelope();
  }

  @Override
  public Envelope envelope() {
    return envelope;
  }

  @Override
  public Iterator<Event> iterator() {
    return events.iterator();
  }

  class ConcreteEnvelope implements Envelope {
    private final Map<String, Object> properties = new LinkedHashMap<>();

    @Override
    public Envelope putProperty(String name, Object value) {
      properties.put(name, value);
      return this;
    }

    @Override
    public View seal(String name) {
      return seal(name, null);
    }

    @Override
    public View seal(String name, String namespace) {
      final List<Event> wrapper = new ArrayList<>();
      wrapper.add(new ConcreteViewEvent(Event.Type.BEGIN_OBJECT, null, null));
      for (final String key : properties.keySet()) {
        wrapper.add(new ConcreteViewEvent(Event.Type.VALUE, key, null,
            properties.get(key)));
      }
      wrapper.add(new ConcreteViewEvent(events.get(0).getType(),
          name, namespace, null));
      wrapper.addAll(events.subList(1, events.size() - 1));
      wrapper.add(new ConcreteViewEvent(events.get(events.size() - 1).getType(),
          name, namespace, null));
      wrapper.add(new ConcreteViewEvent(Event.Type.END_OBJECT, null, null));
      return new ConcreteView(wrapper);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
      return properties.entrySet().iterator();
    }

  }

}
