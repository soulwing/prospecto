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
import org.soulwing.prospecto.api.handler.ViewNodeElementEvent;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.handler.ViewNodeElementHandlerSupport;

/**
 * A view node that represents an array of objects.
 *
 * @author Carl Harris
 */
public class ArrayOfObjectNode extends ContainerViewNode {

  private final String elementName;

  private MultiValuedAccessor accessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param modelType model type of the array elements
   * @return array-of-objects node
   */
  public ArrayOfObjectNode(String name, String elementName,
      String namespace, Class<?> modelType) {
    this(name, elementName, namespace, modelType,
        new ArrayList<AbstractViewNode>());
  }

  /**
   * Constructs a copy of a node, composing it with a new name.
   * @param source source node to be copied
   * @param name name to be composed in the new node
   */
  private ArrayOfObjectNode(ArrayOfObjectNode source, String name) {
    this(name, source.elementName, source.getNamespace(), source.getModelType(),
        source.getChildren());
  }

  private ArrayOfObjectNode(String name, String elementName,
      String namespace, Class<?> modelType, List<AbstractViewNode> children) {
    super(name, namespace, modelType, children);
    this.elementName = elementName;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.accessor = AccessorFactory.multiValue(accessor);
  }

  @Override
  protected List<View.Event> onEvaluate(Object model,
       ScopedViewContext context)
      throws Exception {
    final List<View.Event> events = new LinkedList<>();
    final Iterator<Object> i = getModelIterator(model);
    final ViewNodeElementHandlerSupport handlers =
        new ViewNodeElementHandlerSupport(context.getViewNodeElementHandlers());

    events.add(newEvent(View.Event.Type.BEGIN_ARRAY));
    while (i.hasNext()) {
      final Object elementModel = i.next();
      final ViewNodeElementEvent elementEvent = new ViewNodeElementEvent(this,
          model, elementModel, context);
      if (handlers.willVisitElement(elementEvent)) {
        events.add(newEvent(View.Event.Type.BEGIN_OBJECT, elementName));
        events.addAll(evaluateChildren(handlers.didVisitElement(elementEvent),
            context));
        events.add(newEvent(View.Event.Type.END_OBJECT, elementName));
        handlers.didVisitElement(elementEvent);
      }
    }
    events.add(newEvent(View.Event.Type.END_ARRAY));
    return events;
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return accessor.iterator(source);
  }

  @Override
  public ArrayOfObjectNode copy(String name) {
    return new ArrayOfObjectNode(this, name);
  }

}
