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

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.SubtypeNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A generator for the events associated with a subtype node.
 * @author Carl Harris
 */
class SubtypeGenerator extends AbstractViewEventGenerator<SubtypeNode> {

  private final List<ViewEventGenerator> children;

  SubtypeGenerator(SubtypeNode node, List<ViewEventGenerator> children) {
    super(node);
    this.children = children;
  }

  @Override
  List<View.Event> onGenerate(Object model, ScopedViewContext context)
      throws Exception {
    final List<View.Event> viewEvents = new LinkedList<>();
    if (node.getModelType().isInstance(model)) {
      for (final ViewEventGenerator child : children) {
        viewEvents.addAll(child.generate(model, context));
      }
    }
    return viewEvents;
  }

  @Override
  void push(Object model, ScopedViewContext context) {
    context.push(null, null);
  }

}
