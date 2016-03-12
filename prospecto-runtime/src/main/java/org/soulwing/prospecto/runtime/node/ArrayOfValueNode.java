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
import org.soulwing.prospecto.api.handler.ViewNodeElementEvent;
import org.soulwing.prospecto.api.handler.ViewNodeValueEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.handler.ViewNodeElementHandlerSupport;
import org.soulwing.prospecto.runtime.handler.ViewNodeValueHandlerSupport;

/**
 * A view node that represents an array of values.
 *
 * @author Carl Harris
 */
public class ArrayOfValueNode extends AbstractViewNode {

  private final String elementName;

  private MultiValuedAccessor accessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   * @return array-of-values node
   */
  public ArrayOfValueNode(String name, String elementName, String namespace) {
    super(name, namespace, null);
    this.elementName = elementName;
  }

  /**
   * Constructs a copy of a node, composing it with a new name.
   * @param source source node to be copied
   * @param name name to be composed in the new node
   */
  private ArrayOfValueNode(ArrayOfValueNode source, String name) {
    this(name, source.elementName, source.getNamespace());
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.accessor = AccessorFactory.multiValue(accessor);
  }

  @Override
  public List<View.Event> onEvaluate(Object model, ScopedViewContext context)
      throws Exception {
    final List<View.Event> events = new LinkedList<>();
    final Iterator<Object> i = getModelIterator(model);
    final ViewNodeElementHandlerSupport elementHandlers =
        new ViewNodeElementHandlerSupport(context.getViewNodeElementHandlers());
    final ViewNodeValueHandlerSupport valueHandlers =
        new ViewNodeValueHandlerSupport(context.getViewNodeValueHandlers());

    events.add(newEvent(View.Event.Type.BEGIN_ARRAY));
    while (i.hasNext()) {
      final Object elementModel = i.next();
      final ViewNodeElementEvent elementEvent = new ViewNodeElementEvent(this,
          model, elementModel, context);
      if (elementHandlers.willVisitElement(elementEvent)) {
        final ViewNodeValueEvent valueEvent = new ViewNodeValueEvent(this,
            elementHandlers.didVisitElement(elementEvent), context);
        events.add(newEvent(View.Event.Type.VALUE, elementName,
            valueHandlers.valueToExtract(valueEvent)));
      }
    }
    events.add(newEvent(View.Event.Type.END_ARRAY));

    return events;
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return accessor.iterator(source);
  }

  @Override
  public ArrayOfValueNode copy(String name) {
    return new ArrayOfValueNode(this, name);
  }

}
