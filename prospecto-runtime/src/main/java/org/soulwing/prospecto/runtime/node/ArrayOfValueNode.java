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
package org.soulwing.prospecto.runtime.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;

/**
 * A view node that represents an array of values.
 *
 * @author Carl Harris
 */
public class ArrayOfValueNode implements EventGeneratingViewNode {

  private final String name;
  private final String elementName;
  private final String namespace;

  private MultiValuedAccessor accessor;

  public ArrayOfValueNode(String name, String elementName, String namespace) {
    this(name, elementName, namespace, null);
  }

  public ArrayOfValueNode(String name, String elementName, String namespace,
      MultiValuedAccessor accessor) {
    this.name = name;
    this.elementName = elementName;
    this.namespace = namespace;
    this.accessor = accessor;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    this.accessor = AccessorFactory.multiValue(accessor);
  }

  @Override
  public List<View.Event> evaluate(Object source, ViewContext context)
      throws Exception {
    final List<View.Event> events = new LinkedList<>();
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, name, namespace));
    final Iterator<Object> i = accessor.iterator(source);
    while (i.hasNext()) {
      final Object value = i.next();
      events.add(new ConcreteViewEvent(View.Event.Type.VALUE,
          elementName, namespace, value));
    }
    events.add(new ConcreteViewEvent(View.Event.Type.END_ARRAY, name, namespace));
    return events;
  }

  @Override
  public EventGeneratingViewNode copy(String name) {
    return new ArrayOfValueNode(name, this.elementName, this.namespace, null);
  }

}
