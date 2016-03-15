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

import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.converter.ConverterSupport;
import org.soulwing.prospecto.runtime.converter.Convertible;

/**
 * A view node that represents a value with a simple textual representation.
 *
 * @author Carl Harris
 */
public class ValueNode extends ValueViewNode implements Convertible {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   */
  public ValueNode(String name, String namespace) {
    super(name, namespace);
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

}
