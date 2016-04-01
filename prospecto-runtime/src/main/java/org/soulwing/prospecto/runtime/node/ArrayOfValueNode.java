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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.ConcreteMultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.Convertible;

/**
 * A view node that represents an array of values.
 *
 * @author Carl Harris
 */
public class ArrayOfValueNode extends AbstractViewNode
    implements Convertible, ModelAccessingNode, UpdatableViewNode {

  private final String elementName;
  private final TransformationService transformationService;
  private final UpdatableViewNodeTemplate template;
  private final MultiValuedAccessorFactory accessorFactory;

  private Accessor accessor;
  private MultiValuedAccessor multiValuedAccessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   */
  public ArrayOfValueNode(String name, String elementName, String namespace) {
    this(name, elementName, namespace, ConcreteTransformationService.INSTANCE,
        ConcreteUpdatableViewNodeTemplate.INSTANCE,
        ConcreteMultiValuedAccessorFactory.INSTANCE);
  }

  ArrayOfValueNode(String name, String elementName, String namespace,
      TransformationService transformationService,
      UpdatableViewNodeTemplate template,
      MultiValuedAccessorFactory accessorFactory) {
    super(name, namespace, null);
    this.elementName = elementName;
    this.transformationService = transformationService;
    this.template = template;
    this.accessorFactory = accessorFactory;
  }

  /**
   * Gets the {@code elementName} property.
   * @return property value
   */
  public String getElementName() {
    return elementName;
  }

  @Override
  public Accessor getAccessor() {
    return accessor;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    this.accessor = accessor;
    this.multiValuedAccessor = accessor != null ?
        accessorFactory.newAccessor(accessor) : null;
  }

  @Override
  public List<View.Event> onEvaluate(Object model, ScopedViewContext context)
      throws Exception {
    final List<View.Event> events = new LinkedList<>();
    final Iterator<Object> i = getModelIterator(model);

    events.add(newEvent(View.Event.Type.BEGIN_ARRAY));
    while (i.hasNext()) {
      final Object value = i.next();
      final ViewNodePropertyEvent elementEvent = new ViewNodePropertyEvent(
          ViewNodeEvent.Mode.VIEW_GENERATION, this, model, value, context);
      if (context.getListeners().shouldVisitProperty(elementEvent)) {
        final Object transformedValue = transformationService.valueToExtract(
            model, value, this, context);
        if (transformedValue != UndefinedValue.INSTANCE) {
          events.add(
              newEvent(View.Event.Type.VALUE, elementName, transformedValue));
        }
      }
    }
    events.add(newEvent(View.Event.Type.END_ARRAY));

    return events;
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return multiValuedAccessor.iterator(source);
  }

  @Override
  public Object toModelValue(final ViewEntity parentEntity,
      final View.Event triggerEvent, final Deque<View.Event> events,
      final ScopedViewContext context) throws Exception {

    final Object value = template.toModelValue(this, parentEntity,
        context, new Method(parentEntity, triggerEvent, events, context));

    if (value == UndefinedValue.INSTANCE) {
      skipToEnd(triggerEvent, events);
    }

    return value;
  }

  private void skipToEnd(View.Event triggerEvent, Deque<View.Event> events) {
    while (!events.isEmpty()) {
      final View.Event event = events.removeFirst();
      if (event.getType() == triggerEvent.getType().complement()) break;
    }
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    multiValuedAccessor.clear(target);
    final List<?> array = (List<?>) value;
    for (final Object element : array) {
      multiValuedAccessor.add(target, element);
    }
  }

  class Method implements UpdatableViewNodeTemplate.Method {

    private final ViewEntity parentEntity;
    private final View.Event triggerEvent;
    private final Deque<View.Event> events;
    private final ScopedViewContext context;

    public Method(ViewEntity parentEntity, View.Event triggerEvent,
        Deque<View.Event> events, ScopedViewContext context) {
      this.parentEntity = parentEntity;
      this.triggerEvent = triggerEvent;
      this.events = events;
      this.context = context;
    }

    @Override
    public Object toModelValue() throws Exception {
      final List<Object> array = new ArrayList<>();
      while (!events.isEmpty()) {
        final View.Event event = events.removeFirst();
        if (event.getType() == triggerEvent.getType().complement()) break;
        if (event.getType() != View.Event.Type.VALUE) {
          throw new ModelEditorException(
              "unexpected non-value event in array-of-values");
        }

        final Object valueToInject = transformationService.valueToInject(
            parentEntity, multiValuedAccessor.getComponentType(),
            event.getValue(), ArrayOfValueNode.this, context);

        array.add(valueToInject);
      }
      return array;
    }

  }

}
