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

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A view node that represents an object.
 *
 * @author Carl Harris
 */
public class ObjectNode extends ContainerViewNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type associated with node
   */
  public ObjectNode(String name, String namespace, Class<?> modelType) {
    super(name, namespace, modelType);
  }

  @Override
  protected List<View.Event> onEvaluate(Object source,
      ScopedViewContext context) throws Exception {

    final List<View.Event> events = new LinkedList<>();

    final Object model = getModelObject(source);
    if (model != null) {
      events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
      events.addAll(evaluateChildren(model, context));
      events.add(newEvent(View.Event.Type.END_OBJECT));
    }
    else {
      events.add(newEvent(View.Event.Type.VALUE));
    }
    return events;
  }

  @Override
  public void onUpdate(Object target, View.Event event,
      Deque<View.Event> events,
      ScopedViewContext context) throws Exception {
    Object model = getModelObject(target);
    final Object child = createChild(getModelType(), events, context);
    if (model == null || !model.equals(child)) {
      // FIXME -- need to tell some handler that we created or replaced a child
      model = child;
      setModelObject(target, model);
    }

    updateChildren(model, event, events, context);
  }

  @Override
  public boolean supportsUpdateEvent(View.Event event) {
    return event.getType() == View.Event.Type.BEGIN_OBJECT
        || event.getType() == View.Event.Type.VALUE;
  }

  protected Object getModelObject(Object source) throws Exception {
    return getAccessor().get(source);
  }

  protected void setModelObject(Object target, Object value) throws Exception {
    getAccessor().set(target, value);
  }

}
