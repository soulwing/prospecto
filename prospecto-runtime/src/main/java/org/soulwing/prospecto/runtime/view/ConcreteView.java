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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.View;

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
    return envelope;
  }

  @Override
  public Iterator<Event> iterator() {
    return events.iterator();
  }

  static class ConcreteEnvelope implements Envelope {
    private final Map<String, Object> properties = new LinkedHashMap<>();

    @Override
    public void putProperty(String name, Object value) {
      properties.put(name, value);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
      return properties.entrySet().iterator();
    }

  }

}
