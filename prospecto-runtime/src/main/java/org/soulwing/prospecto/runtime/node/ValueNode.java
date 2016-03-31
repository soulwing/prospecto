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

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.ConverterSupport;
import org.soulwing.prospecto.runtime.converter.Convertible;

/**
 * A view node that represents a value with a simple textual representation.
 *
 * @author Carl Harris
 */
public class ValueNode extends ValueViewNode
    implements Convertible, UpdatableViewNode {

  private Accessor accessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   */
  public ValueNode(String name, String namespace) {
    super(name, namespace);
  }

  @Override
  public Accessor getAccessor() {
    return accessor;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    this.accessor = accessor;
  }

  @Override
  protected Object getModelValue(Object source, ScopedViewContext context)
      throws Exception {
    return getAccessor().get(source);
  }

  @Override
  protected Object toViewValue(Object model, ScopedViewContext context)
      throws Exception {
    return ConverterSupport.toViewValue(model, this, context);
  }

  @Override
  public Object toModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
    context.push(getName(), getModelType());
    try {
      final Object viewValue = triggerEvent.getValue();

      final ViewNodeEvent nodeEvent = new ViewNodeEvent(
          ViewNodeEvent.Mode.MODEL_UPDATE, this, viewValue, context);
      if (!context.getListeners().shouldVisitNode(nodeEvent)) {
        return UndefinedValue.INSTANCE;
      }

      final Object convertedValue = ConverterSupport.toModelValue(
          getAccessor().getDataType(), viewValue, this, context);

      final Object valueToInject = context.getListeners().willInjectValue(
          new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, this,
              parentEntity, convertedValue, context));

      context.getListeners().propertyVisited(
          new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, this,
              parentEntity, valueToInject, context));

      context.getListeners().nodeVisited(nodeEvent);

      return valueToInject;
    }
    catch (Exception ex) {
      throw new ModelEditorException("error at path "
          + context.currentViewPathAsString(), ex);
    }
    finally {
      context.pop();
    }

  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    getAccessor().forSubtype(target.getClass()).set(target, value);
  }

}
