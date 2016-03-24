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

import java.util.Collections;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.handler.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * An abstract base for nodes of value type.
 *
 * @author Carl Harris
 */
abstract class ValueViewNode extends AbstractViewNode {

  /**
   * Constructs a new instance
   * @param name node name
   * @param namespace namespace for {@code name}
   */
  protected ValueViewNode(String name, String namespace) {
    super(name, namespace, null);
  }

  @Override
  protected final List<View.Event> onEvaluate(Object source,
      ScopedViewContext context) throws Exception {

    final Object value = getModelValue(source, context);

    final Object extractedValue = context.getListeners().fireOnExtractValue(
        new ViewNodePropertyEvent(this, source, value, context));

    final Object viewValue = toViewValue(extractedValue, context);

    context.getListeners().firePropertyVisited(
        new ViewNodePropertyEvent(this, source, viewValue, context));

    return Collections.singletonList(newEvent(
        getEventType(), getEventName(value, context), viewValue));
  }

  protected String getEventName(Object model, ScopedViewContext context) {
    return getName();
  }

  protected View.Event.Type getEventType() {
    return View.Event.Type.VALUE;
  }

  protected abstract Object getModelValue(Object source,
      ScopedViewContext context) throws Exception;

  protected abstract Object toViewValue(Object model,
      ScopedViewContext context) throws Exception;

}
