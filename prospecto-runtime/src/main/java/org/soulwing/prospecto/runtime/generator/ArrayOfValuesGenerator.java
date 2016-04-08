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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.template.ArrayOfValuesNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * A generator for the events associated with an array-of-values node.
 *
 * @author Carl Harris
 */
class ArrayOfValuesGenerator
    extends AbstractViewEventGenerator<ArrayOfValuesNode> {

  private final TransformationService transformationService;

  ArrayOfValuesGenerator(ArrayOfValuesNode node) {
    this(node, ConcreteTransformationService.INSTANCE);
  }

  ArrayOfValuesGenerator(ArrayOfValuesNode node,
      TransformationService transformationService) {
    super(node);
    this.transformationService = transformationService;
  }

  @Override
  List<View.Event> onGenerate(Object owner, ScopedViewContext context)
      throws Exception {

    final Iterator<?> i = node.iterator(owner);
    final List<View.Event> events = new LinkedList<>();

    events.add(new ConcreteViewEvent(View.Event.Type.BEGIN_ARRAY,
        node.getName(), node.getNamespace()));

    int index = 0;
    while (i.hasNext()) {
      context.push(String.format("[%d]", index++), null);
      final Object value = i.next();
      final Object transformedValue = transformationService.valueToExtract(
          owner, value, node, context);
      context.pop();

      if (transformedValue != UndefinedValue.INSTANCE) {
        events.add(new ConcreteViewEvent(View.Event.Type.VALUE,
            node.getElementName(), node.getNamespace(), transformedValue));
      }

    }

    events.add(new ConcreteViewEvent(View.Event.Type.END_ARRAY,
        node.getName(), node.getNamespace()));

    return events;
  }
}
