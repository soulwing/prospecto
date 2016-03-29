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
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.collection.CollectionManager;
import org.soulwing.prospecto.api.collection.ListManager;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessorFactory;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

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
    super(name, namespace, modelType,
        new ArrayList<AbstractViewNode>());
    this.elementName = elementName;
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
    this.accessor = MultiValuedAccessorFactory.newAccessor(accessor);
  }

  @Override
  protected List<View.Event> onEvaluate(Object model,
       ScopedViewContext context)
      throws Exception {
    final Iterator<Object> i = getModelIterator(model);
    if (i == null) {
      return Collections.singletonList(newEvent(View.Event.Type.VALUE));
    }

    final List<View.Event> viewEvents = new LinkedList<>();
    viewEvents.add(newEvent(View.Event.Type.BEGIN_ARRAY));
    while (i.hasNext()) {
      Object elementModel = i.next();
      final ViewNodePropertyEvent elementEvent = new ViewNodePropertyEvent(this,
          model, elementModel, context);
      if (context.getListeners().shouldVisitProperty(elementEvent)) {
        viewEvents.add(newEvent(View.Event.Type.BEGIN_OBJECT, elementName));
        viewEvents.addAll(evaluateChildren(
            context.getListeners().didExtractValue(elementEvent),
            context));
        viewEvents.add(newEvent(View.Event.Type.END_OBJECT, elementName));
      }
    }
    viewEvents.add(newEvent(View.Event.Type.END_ARRAY));
    return viewEvents;
  }

  @Override
  public List<MutableViewEntity> toModelValue(ViewEntity parentEntity,
      View.Event triggerEvent, Deque<View.Event> events,
      ScopedViewContext context) throws Exception {
    final List<MutableViewEntity> entities = new ArrayList<>();
    View.Event event = events.removeFirst();
    while (event != null
        && View.Event.Type.BEGIN_OBJECT.equals(event.getType())) {
      entities.add((MutableViewEntity)
          super.toModelValue(parentEntity, event, events, context));
      event = events.removeFirst();
    }
    if (event == null
        || event.getType() != triggerEvent.getType().complement()) {
      throw new ModelEditorException("expected END_ARRAY");
    }
    return entities;
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    final CollectionManager manager = getCollectionManager(context);
    final Map<Object, Object> touched = createOrUpdateChildren(target,
        (List<?>) value, manager, context);
    removeChildren(target, touched, manager, context);
  }

  @SuppressWarnings("unchecked")
  private CollectionManager getCollectionManager(ScopedViewContext context) {
    assert getParent() != null;
    CollectionManager manager = get(CollectionManager.class);
    if (manager != null) {
      if (!manager.supports(getParent().getModelType(), getModelType())) {
        throw new ModelEditorException(
            "collection manager does not support expected types");
      }
      return manager;
    }

    manager = context.getCollectionManagers().findManager(
        getParent().getModelType(), getModelType());
    if (manager != null) return manager;

    return accessor;
  }

  @SuppressWarnings("unchecked")
  protected Map<Object, Object> createOrUpdateChildren(Object target,
      List<?> entities, CollectionManager manager, ScopedViewContext context)
      throws Exception {
    final Map<Object, Object> touched = new IdentityHashMap<>();
    final Iterator<?> i = ((List) entities).iterator();
    int index = 0;
    while (i.hasNext()) {
      final MutableViewEntity entity = (MutableViewEntity) i.next();
      final Object element = manager.find(target, entity);
      if (element != null) {
        touched.put(element, element);
        entity.inject(element, context);
      }
      else {
        Object newElement = manager.newElement(target, entity);
        if (newElement == null) {
          newElement = accessor.newElement(target, entity);
        }
        context.getListeners().entityCreated(new ViewNodePropertyEvent(
            this, target, newElement, context));
        if (manager instanceof ListManager) {
          ((ListManager) manager).add(target, index, newElement);
        }
        else {
          manager.add(target, newElement);
        }
        touched.put(newElement, newElement);
      }
      index++;
    }
    return touched;
  }

  @SuppressWarnings("unchecked")
  protected void removeChildren(Object target, Map<Object, Object> touched,
      CollectionManager manager, ScopedViewContext context) throws Exception {
    final List<Object> children = copyModelChildren(target);
    int index = 0;
    for (final Object child : children) {
      if (!touched.containsKey(child)) {
        context.getListeners().entityDiscarded(
            new ViewNodePropertyEvent(this, target, child, context));
        if (manager instanceof ListManager) {
          ((ListManager) manager).remove(target, index);
        }
        else {
          manager.remove(target, child);
        }
      }
      index++;
    }
  }

  private List<Object> copyModelChildren(Object source) throws Exception {
    final List<Object> children = new LinkedList<>();
    final Iterator<Object> i = getModelIterator(source);
    while (i.hasNext()) {
      children.add(i.next());
    }
    return children;
  }

  protected Iterator<Object> getModelIterator(Object source) throws Exception {
    return accessor.iterator(source);
  }

}
