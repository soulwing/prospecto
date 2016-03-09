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
 * A view node that represents an envelope.
 * <p>
 * An envelope inserts another object node into the textual representation of
 * a view without a corresponding change in model context.
 *
 * @author Carl Harris
 */
public class EnvelopeNode implements ContainerViewNode {

  private final String name;

  private final String namespace;

  private Accessor accessor;

  private final List<EventGeneratingViewNode> children;

  public EnvelopeNode(String name, String namespace) {
    this(name, namespace, null);
  }

  public EnvelopeNode(String name, String namespace, Accessor accessor) {
    this(name, namespace, accessor, new ArrayList<EventGeneratingViewNode>());
  }

  private EnvelopeNode(String name, String namespace, Accessor accessor,
      List<EventGeneratingViewNode> children) {
    this.name = name;
    this.namespace = namespace;
    this.accessor = accessor;
    this.children = children;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    this.accessor = accessor;
  }

  @Override
  public List<View.Event> evaluate(Object source, ViewContext context)
      throws Exception {
    final List<View.Event> events = new LinkedList<>();
    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, name, namespace));
    for (EventGeneratingViewNode child : children) {
      events.addAll(child.evaluate(source, context));
    }
    events.add(new ConcreteViewEvent(View.Event.Type.END_OBJECT, name, namespace));
    return events;
  }

  @Override
  public void addChild(EventGeneratingViewNode child) {
    children.add(child);
  }

  @Override
  public EventGeneratingViewNode copy(String name) {
    return new EnvelopeNode(name, this.namespace, null, this.children);
  }

}
