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

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A view node that represents a value with a simple textual representation.
 *
 * @author Carl Harris
 */
public class ValueNode extends ValueViewNode {

  private ValueTypeConverter<?> converter;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   */
  public ValueNode(String name, String namespace) {
    super(name, namespace);
  }

  /**
   * Gets this node's value type converter.
   * @return value type converter or {@code null} if none is configured.
   */
  public ValueTypeConverter<?> getConverter() {
    return converter;
  }

  /**
   * Sets this node's value type converter.
   * @param converter the value type converter to set
   */
  public void setConverter(ValueTypeConverter<?> converter) {
    this.converter = converter;
  }

  @Override
  protected Object getModelValue(Object source, ScopedViewContext context)
      throws Exception {
    return getAccessor().get(source);
  }

  @Override
  protected Object toViewValue(Object model, ViewContext context)
      throws Exception {
    if (model == null) return null;
    if (converter != null) {
      return converter.toValue(model);
    }
    for (ValueTypeConverter<?> converter : context.getValueTypeConverters()) {
      if (converter.supports(model.getClass())) {
        return converter.toValue(model);
      }
    }
    return model;
  }

}
