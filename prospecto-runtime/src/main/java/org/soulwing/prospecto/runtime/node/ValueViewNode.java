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
import org.soulwing.prospecto.api.handler.ViewNodeValueEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.handler.ViewNodeValueHandlerSupport;

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

    final ViewNodeValueEvent valueEvent = new ViewNodeValueEvent(this,
        getModelValue(source, context), context);

    return Collections.singletonList(newEvent(getEventType(), getName(),
        toViewValue(ViewNodeValueHandlerSupport.extractedValue(valueEvent),
        context)));
  }

  protected View.Event.Type getEventType() {
    return View.Event.Type.VALUE;
  }

  protected abstract Object getModelValue(Object source,
      ScopedViewContext context) throws Exception;

  protected abstract Object toViewValue(Object model,
      ScopedViewContext context) throws Exception;

}
