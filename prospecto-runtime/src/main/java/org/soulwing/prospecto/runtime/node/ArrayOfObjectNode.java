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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A view node that represents an array of objects.
 *
 * @author Carl Harris
 */
public class ArrayOfObjectNode implements ContainerViewNode {

  private final String name;
  private final String elementName;
  private final String namespace;
  private final Class<?> modelType;
  private final List<EventGeneratingViewNode> children;

  private MultiValuedAccessor accessor;

  private ArrayOfObjectNode(ArrayOfObjectNode source, String name) {
    this(name, source.elementName, source.namespace, source.modelType, null,
        source.children);
  }

  public ArrayOfObjectNode(String name, String elementName,
      String namespace, Class<?> modelType) {
    this(name, elementName, namespace, modelType, null);
  }

  public ArrayOfObjectNode(String name, String elementName,
      String namespace, Class<?> modelType, MultiValuedAccessor accessor) {
    this(name, elementName, namespace, modelType, accessor,
        new ArrayList<EventGeneratingViewNode>());
  }

  private ArrayOfObjectNode(String name, String elementName,
      String namespace, Class<?> modelType, MultiValuedAccessor accessor,
      List<EventGeneratingViewNode> children) {
    this.name = name;
    this.elementName = elementName;
    this.namespace = namespace;
    this.modelType = modelType;
    this.accessor = accessor;
    this.children = children;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    this.accessor = AccessorFactory.multiValue(accessor);
  }

  @Override
  public List<View.Event> evaluate(Object source, ViewContext context)
      throws Exception {
    final List<View.Event> events = new LinkedList<>();
    context.push(name);
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY, name,
        namespace));
    final Iterator<Object> i = accessor.iterator(source);

    while (i.hasNext()) {
      final Object elementSource = i.next();
      context.push(elementName, modelType, elementSource);
      events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, elementName,
          namespace));
      for (EventGeneratingViewNode child : children) {
        events.addAll(child.evaluate(elementSource, context));
      }
      events.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT, elementName,
          namespace));
      context.pop();
    }
    events.add(new ConcreteViewEvent(View.Event.Type.END_ARRAY, name,
        namespace));
    context.pop();
    return events;
  }

  @Override
  public void addChild(EventGeneratingViewNode child) {
    children.add(child);
  }

  @Override
  public EventGeneratingViewNode copy(String name) {
    return new ArrayOfObjectNode(this, name);
  }

}
