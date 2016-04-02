/*
 * File created on Mar 26, 2016
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

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A view node that represents a reference to an object.
 *
 * @author Carl Harris
 */
public class ReferenceNode extends ObjectNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type associated with node
   */
  public ReferenceNode(String name, String namespace, Class<?> modelType) {
    super(name, namespace, modelType);
  }

  @Override
  public Object toModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
    MutableViewEntity entity = (MutableViewEntity) super.toModelValue(
        parentEntity, triggerEvent, events, context);
    return context.getReferenceResolvers().resolve(getModelType(), entity);
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    getAccessor().set(target, value);
  }

}
