/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.runtime.generator;

import java.util.Collections;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * An abstract base for {@link ViewEventGenerator} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractViewEventGenerator<N extends ViewNode>
    implements ViewEventGenerator {

  protected final N node;

  AbstractViewEventGenerator(N node) {
    this.node = node;
  }

  @Override
  public final List<View.Event> generate(Object model, ScopedViewContext context)
      throws Exception {
    final ViewNodeEvent nodeEvent = new ViewNodeEvent(
        ViewNodeEvent.Mode.VIEW_GENERATION, node, model, context);
    if (context.getListeners().shouldVisitNode(nodeEvent)) {
      push(model, context);
      final List<View.Event> viewEvents = onGenerate(model, context);
      context.getListeners().nodeVisited(nodeEvent);
      pop(context);
      return viewEvents;
    }
    else {
      return Collections.emptyList();
    }
  }

  void push(Object model, ScopedViewContext context) {
    context.push(node.getName(), node.getModelType());
    context.put(model);
  }

  void pop(ScopedViewContext context) {
    context.pop();
  }

  abstract List<View.Event> onGenerate(Object model, ScopedViewContext context)
      throws Exception;

}
