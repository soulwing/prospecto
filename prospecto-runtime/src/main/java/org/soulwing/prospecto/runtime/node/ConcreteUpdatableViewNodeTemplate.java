/*
 * File created on Apr 1, 2016
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

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * An {@link UpdatableViewNodeTemplate} implementation.
 *
 * @author Carl Harris
 */
class ConcreteUpdatableViewNodeTemplate implements UpdatableViewNodeTemplate {

  public static final ConcreteUpdatableViewNodeTemplate INSTANCE =
      new ConcreteUpdatableViewNodeTemplate();

  private ConcreteUpdatableViewNodeTemplate() {}

  @Override
  public Object toModelValue(ViewNode node, ViewEntity parentEntity,
      ScopedViewContext context, Method method) throws Exception {

    context.push(node.getName(), node.getModelType());
    try {
      final ViewNodeEvent nodeEvent = new ViewNodeEvent(
          ViewNodeEvent.Mode.MODEL_UPDATE, node, parentEntity, context);

      if (!context.getListeners().shouldVisitNode(nodeEvent)) {
        return UndefinedValue.INSTANCE;
      }

      final Object transformedValue = method.toModelValue();
      context.getListeners().nodeVisited(nodeEvent);
      return transformedValue;

    }
    catch (Exception ex) {
      throw new ModelEditorException("error at path "
          + context.currentViewPathAsString(), ex);
    }
    finally {
      context.pop();
    }
  }

}
