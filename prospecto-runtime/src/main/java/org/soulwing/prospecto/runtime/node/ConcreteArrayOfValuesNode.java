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
import org.soulwing.prospecto.api.node.ArrayOfValuesNode;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.ConcreteMultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ValueCollectionToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.Convertible;

/**
 * A view node that represents an array of values.
 *
 * @author Carl Harris
 */
public class ConcreteArrayOfValuesNode extends AbstractViewNode
    implements Convertible, ModelAccessingNode, UpdatableViewNode,
    ArrayOfValuesNode {

  private final String elementName;
  private final Class<?> componentType;
  private final TransformationService transformationService;
  private final UpdatableViewNodeTemplate template;
  private final MultiValuedAccessorFactory accessorFactory;
  private final ToManyAssociationUpdater associationUpdater;

  private MultiValuedAccessor multiValuedAccessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param componentType common type for elements
   */
  public ConcreteArrayOfValuesNode(String name, String elementName, String namespace,
      Class<?> componentType) {
    this(name, elementName, namespace, componentType,
        ConcreteTransformationService.INSTANCE,
        ConcreteUpdatableViewNodeTemplate.INSTANCE,
        ConcreteMultiValuedAccessorFactory.INSTANCE,
        ValueCollectionToManyAssociationUpdater.INSTANCE);
  }

  ConcreteArrayOfValuesNode(String name, String elementName, String namespace,
      Class<?> componentType, TransformationService transformationService,
      UpdatableViewNodeTemplate template,
      MultiValuedAccessorFactory accessorFactory,
      ToManyAssociationUpdater associationUpdater) {
    super(name, namespace, null);
    this.elementName = elementName;
    this.componentType = componentType;
    this.transformationService = transformationService;
    this.template = template;
    this.accessorFactory = accessorFactory;
    this.associationUpdater = associationUpdater;
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitArrayOfValues(this, state);
  }

  /**
   * Gets the {@code elementName} property.
   * @return property value
   */
  public String getElementName() {
    return elementName;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    super.setAccessor(accessor);
    this.multiValuedAccessor = accessor != null ?
        accessorFactory.newAccessor(accessor, componentType) : null;
  }

  @Override
  public List<View.Event> onEvaluate(Object model, ScopedViewContext context)
      throws Exception {

    final Iterator<Object> i = getModelIterator(model);
    final List<View.Event> events = new LinkedList<>();
    events.add(newEvent(View.Event.Type.BEGIN_ARRAY));

    while (i.hasNext()) {
      final Object value = i.next();
      final Object transformedValue = transformationService.valueToExtract(
          model, value, this, context);
      if (transformedValue != UndefinedValue.INSTANCE) {
        events.add(newEvent(View.Event.Type.VALUE, elementName,
            transformedValue));
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
  public void inject(Object target, Object value) throws Exception {}

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    associationUpdater.update(this, target, (Iterable<?>) value,
        multiValuedAccessor, context);
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
            event.getValue(), ConcreteArrayOfValuesNode.this, context);

        array.add(valueToInject);
      }
      return array;
    }

  }

}
