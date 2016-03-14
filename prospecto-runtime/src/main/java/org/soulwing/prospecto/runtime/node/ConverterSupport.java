/*
 * File created on Mar 14, 2016
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

/**
 * DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class ConverterSupport {

  private ValueTypeConverter<?> converter;

  /**
   * Gets the {@code converter} property.
   * @return property value
   */
  public ValueTypeConverter<?> getConverter() {
    return converter;
  }

  /**
   * Sets the {@code converter} property.
   * @param converter the property value to set
   */
  public void setConverter(ValueTypeConverter<?> converter) {
    this.converter = converter;
  }

  /**
   * Converts a model value to a view value.
   * @param model model value
   * @param context view context
   * @return view value
   * @throws Exception
   */
  public Object toViewValue(Object model, ViewContext context)
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
