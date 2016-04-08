/*
 * File created on Apr 8, 2016
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

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.node.MetaNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for meta node generators.
 *
 * @author Carl Harris
 */
abstract class AbstractMetaGenerator<N extends MetaNode>
    extends AbstractViewEventGenerator<N> {

  private final TransformationService transformationService;

  AbstractMetaGenerator(N node, TransformationService transformationService) {
    super(node);
    this.transformationService = transformationService;
  }

  @Override
  final List<View.Event> onGenerate(Object source, ScopedViewContext context)
      throws Exception {

    final Object modelValue = node.getHandler().produceValue(node, context);

    final Object transformedValue = transformationService.valueToExtract(
        source, modelValue, node, context);

    if (transformedValue == UndefinedValue.INSTANCE) {
      return Collections.emptyList();
    }

    return Collections.singletonList(newEvent(transformedValue));
  }

  abstract View.Event newEvent(Object value);

}

