/*
 * File created on Apr 8, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.ViewApplicatorException;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A {@link ViewApplicator} implementation.
 *
 * @author Carl Harris
 */
@SuppressWarnings("ThrowableInstanceNeverThrown")
class ConcreteViewApplicator implements ViewApplicator {

  private static final ViewApplicatorException NOT_WELL_FORMED_EXCEPTION =
      new ViewApplicatorException("view is not well-formed");

  private final Class<?> modelType;
  private final RootViewEventApplicator root;
  private final View source;
  private final ScopedViewContext context;
  private final String dataKey;

  /**
   * Constructs a new instance.
   * @param modelType expected root model type
   * @param root root root applicator
   * @param source source view
   * @param context view context
   * @param dataKey envelope key that contains the editable view data
*    or {@code null} if the view is not enveloped
   */
  ConcreteViewApplicator(Class<?> modelType, RootViewEventApplicator root, View source,
      ScopedViewContext context, String dataKey) {
    this.modelType = modelType;
    this.root = root;
    this.source = source;
    this.context = context;
    this.dataKey = dataKey;
  }

  @Override
  public Object create() throws ViewApplicatorException {
    try {
      final MutableViewEntity entity = deriveInjector();
      if (entity == UndefinedValue.INSTANCE) return null;

      final Object model = ((MutableViewEntity) entity).getType().newInstance();
      root.apply(entity, model, context);
      return model;
    }
    catch (ViewApplicatorException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ViewApplicatorException(ex);
    }
  }

  @Override
  public void update(Object model) throws ViewApplicatorException {
    try {
      assertHasRootModelType(model);
      final MutableViewEntity entity = deriveInjector();
      if (entity != UndefinedValue.INSTANCE) {
        root.apply(entity, model, context);
      }
    }
    catch (ViewApplicatorException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ViewApplicatorException(ex);
    }
  }

  private MutableViewEntity deriveInjector() throws Exception {
    final Deque<View.Event> events = eventDeque(source);
    final View.Event triggerEvent = events.removeFirst();
    if (triggerEvent.getType() != View.Event.Type.BEGIN_OBJECT) {
      throw new ViewApplicatorException("view must start with an object");
    }
    return (MutableViewEntity) root.toModelValue(null, triggerEvent, events,
        context);
  }

  private void assertHasRootModelType(Object model) {
    if (!modelType.isInstance(model)) {
      throw new ViewApplicatorException("model is not an instance of "
          + modelType.getName());
    }
  }

  private Deque<View.Event> eventDeque(View view) throws ViewApplicatorException {
    final Deque<View.Event> deque = new LinkedList<>();
    final Iterator<View.Event> events = view.iterator();
    if (!events.hasNext()) {
      throw NOT_WELL_FORMED_EXCEPTION;
    }

    final View.Event firstEvent = events.next();
    if (firstEvent.getType() != View.Event.Type.BEGIN_OBJECT) {
      throw new ViewApplicatorException("root view type must be an object");
    }

    View.Event triggerEvent = firstEvent;
    if (dataKey != null) {
      triggerEvent = dataEvent(events);
    }

    updateDeque(triggerEvent, events, deque);

    if (dataKey != null) {
      skipToEnd(firstEvent, events);
    }
    if (events.hasNext()) {
      throw NOT_WELL_FORMED_EXCEPTION;
    }
    return deque;
  }

  private void updateDeque(View.Event triggerEvent,
      Iterator<View.Event> events, Deque<View.Event> deque)
      throws ViewApplicatorException {
    deque.addLast(triggerEvent);
    while (events.hasNext()) {
      final View.Event event = events.next();
      if (event.getType() == triggerEvent.getType().complement()) {
        if (!Objects.equals(triggerEvent.getNamespace(), event.getNamespace())
            || !Objects.equals(triggerEvent.getName(), event.getName())) {
          throw NOT_WELL_FORMED_EXCEPTION;
        }
        deque.addLast(event);
        return;
      }
      if (event.getType() != event.getType().complement()) {
        if (!event.getType().isBegin()) {
          throw NOT_WELL_FORMED_EXCEPTION;
        }
        updateDeque(event, events, deque);
      }
      else {
        deque.addLast(event);
      }
    }
    throw NOT_WELL_FORMED_EXCEPTION;
  }

  private View.Event dataEvent(Iterator<View.Event> events)
      throws ViewApplicatorException {
    while (events.hasNext()) {
      final View.Event event = events.next();
      if (event.getType() == View.Event.Type.BEGIN_OBJECT
          && dataKey.equals(event.getName())) {
        return event;
      }
      if (event.getType() == View.Event.Type.END_OBJECT) {
        throw new ViewApplicatorException("object '" + dataKey
            + "' not found in view envelope");
      }
      if (event.getType() != event.getType().complement()) {
        throw new ViewApplicatorException("unexpected structure in view envelope");
      }
    }
    throw NOT_WELL_FORMED_EXCEPTION;
  }


  private void skipToEnd(View.Event triggerEvent, Iterator<View.Event> events)
      throws ViewApplicatorException {
    while (events.hasNext()) {
      final View.Event event = events.next();
      if (event.getType() == triggerEvent.getType().complement()) break;
      if (event.getType() != event.getType().complement()) {
        if (!event.getType().isBegin()) {
          throw NOT_WELL_FORMED_EXCEPTION;
        }
        skipToEnd(triggerEvent, events);
      }
    }
  }

}