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
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A view node that represents an object.
 *
 * @author Carl Harris
 */
public class ObjectNode implements ContainerViewNode {

  private final String name;

  private final String namespace;

  private Accessor accessor;

  private Class<?> modelType;

  private final List<EventGeneratingViewNode> children;

  private ObjectNode(ObjectNode source, String name) {
    this(name, source.namespace, source.modelType, null,
        source.children);
  }

  public ObjectNode(String name, String namespace, Class<?> modelType) {
    this(name, namespace, modelType, null);
  }

  public ObjectNode(String name, String namespace, Class<?> modelType,
      Accessor accessor) {
    this(name, namespace, modelType, accessor, new ArrayList<EventGeneratingViewNode>());
  }

  private ObjectNode(String name, String namespace, Class<?> modelType, Accessor accessor,
      List<EventGeneratingViewNode> children) {
    this.name = name;
    this.namespace = namespace;
    this.accessor = accessor;
    this.modelType = modelType;
    this.children = children;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    this.accessor = accessor;
  }

  @Override
  public List<View.Event> evaluate(Object source, ViewContext context)
      throws Exception {
    final Object object = accessor != null ? accessor.get(source) : source;
    final List<View.Event> events = new LinkedList<>();
    context.push(name, modelType, object);
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, name, namespace));
    for (EventGeneratingViewNode child : children) {
      events.addAll(child.evaluate(object, context));
    }
    context.pop();
    events.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT, name, namespace));
    return events;
  }

  @Override
  public void addChild(EventGeneratingViewNode child) {
    children.add(child);
  }

  @Override
  public EventGeneratingViewNode copy(String name) {
    return new ObjectNode(this, name);
  }

}
