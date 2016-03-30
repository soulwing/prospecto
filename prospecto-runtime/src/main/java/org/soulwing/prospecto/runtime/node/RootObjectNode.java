/*
 * File created on Mar 11, 2016
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

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A root view node that represents an object.
 *
 * @author Carl Harris
 */
public class RootObjectNode extends ObjectNode
    implements UpdatableRootNode {

  /**
   * Constructs a new instance
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type associated with the node
   * @return root object node
   */
  public RootObjectNode(String name, String namespace, Class<?> modelType) {
    super(name, namespace, modelType);
  }

  @Override
  public Object create(Deque<View.Event> events, ScopedViewContext context)
      throws ModelEditorException {
    final View.Event triggerEvent = events.removeFirst();
    if (triggerEvent.getType() != View.Event.Type.BEGIN_OBJECT) {
      throw new ModelEditorException("view must start with an object");
    }
    try {
      Object target = null;
      Object value = toModelValue(null, triggerEvent, events, context);
      if (value != UndefinedValue.INSTANCE) {
        target = ((MutableViewEntity) value).getType().newInstance();
        ((MutableViewEntity) value).inject(target, context);
      }
      if (target == null) {
        throw new ModelEditorException("view produced no object");
      }
      return target;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ModelEditorException(ex);
    }
  }

  @Override
  public void update(Object target, Deque<View.Event> events,
      ScopedViewContext context) throws ModelEditorException {
    final View.Event triggerEvent = events.removeFirst();
    if (triggerEvent.getType() != View.Event.Type.BEGIN_OBJECT) {
      throw new ModelEditorException("view must start with an object");
    }
    try {
      Object value = toModelValue(null, triggerEvent, events, context);
      if (value != UndefinedValue.INSTANCE) {
        ((MutableViewEntity) value).inject(target, context);
      }
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ModelEditorException(ex);
    }
  }

  @Override
  protected Object getModelObject(Object source) throws Exception {
    return source;
  }

}
