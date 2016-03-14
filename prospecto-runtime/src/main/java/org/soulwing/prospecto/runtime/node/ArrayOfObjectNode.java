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
   */
  public ArrayOfObjectNode(String name, String elementName,
      String namespace, Class<?> modelType) {
    super(name, namespace, modelType, new ArrayList<AbstractViewNode>());
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
      Object elementModel = i.next();
      final ViewNodeElementEvent elementEvent = new ViewNodeElementEvent(this,
          model, elementModel, context);
      if (handlers.willVisitElement(elementEvent)) {
        handlers.didVisitElement(elementEvent);
        events.add(newEvent(View.Event.Type.BEGIN_OBJECT, elementName));
        events.addAll(evaluateChildren(handlers.didVisitElement(elementEvent),
            context));
        events.add(newEvent(View.Event.Type.END_OBJECT, elementName));
      }
    }
    events.add(newEvent(View.Event.Type.END_ARRAY));
    return events;
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return accessor.iterator(source);
  }

}
