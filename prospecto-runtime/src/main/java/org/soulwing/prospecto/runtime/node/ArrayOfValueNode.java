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
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
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
public class ArrayOfValueNode extends AbstractViewNode implements Convertable {

  private final String elementName;
  private final ConverterSupport converterSupport = new ConverterSupport();

  private MultiValuedAccessor accessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   */
  public ArrayOfValueNode(String name, String elementName, String namespace) {
    super(name, namespace, null);
    this.elementName = elementName;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.accessor = AccessorFactory.multiValue(accessor);
  }

  /**
   * Gets this node's value type converter.
   * @return value type converter or {@code null} if none is configured.
   */
  @Override
  public ValueTypeConverter<?> getConverter() {
    return converterSupport.getConverter();
  }

  /**
   * Sets this node's value type converter.
   * @param converter the value type converter to set
   */
  @Override
  public void setConverter(ValueTypeConverter<?> converter) {
    converterSupport.setConverter(converter);
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
            toViewValue(valueHandlers.valueToExtract(valueEvent), context)));
      }
    }
    events.add(newEvent(View.Event.Type.END_ARRAY));

    return events;
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return accessor.iterator(source);
  }

  private Object toViewValue(Object model, ViewContext context)
      throws Exception {
    return converterSupport.toViewValue(model, context);
  }

}
