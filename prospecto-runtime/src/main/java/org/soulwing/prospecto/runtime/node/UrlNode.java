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

import java.util.Collections;
import java.util.List;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A view node that represents a value that is resolved as a URL.
 *
 * @author Carl Harris
 */
public class UrlNode extends AbstractViewNode {

  public static final String DEFAULT_NAME = "href";

  private final TransformationService transformationService;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   */
  public UrlNode(String name, String namespace) {
    this(name, namespace, ConcreteTransformationService.INSTANCE);
  }

  UrlNode(String name, String namespace,
      TransformationService transformationService) {
    super(name, namespace, null);
    this.transformationService = transformationService;
  }

  @Override
  protected List<View.Event> onEvaluate(Object source,
      ScopedViewContext context) throws Exception {

    final Object modelValue =
        context.get(UrlResolver.class).resolve(this, context);

    final Object transformedValue =
        transformationService.valueToExtract(source, modelValue, this, context);

    if (transformedValue == UndefinedValue.INSTANCE) {
      return Collections.emptyList();
    }

    return Collections.singletonList(
        newEvent(View.Event.Type.URL, getName(), transformedValue));
  }

}
