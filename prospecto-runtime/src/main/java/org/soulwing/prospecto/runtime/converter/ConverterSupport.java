/*
 * File created on Mar 15, 2016
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
package org.soulwing.prospecto.runtime.converter;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;

/**
 * A utility that provides support for model value to view value conversion.
 *
 * @author Carl Harris
 */
public class ConverterSupport {

  /**
   * Converts a model value to a view value for a node.
   * @param model model value
   * @param node the subject node
   * @param context view context
   * @return view value
   * @throws Exception
   */
  public static Object toViewValue(Object model, ViewNode node,
      ViewContext context) throws Exception {
    if (model == null) return null;

    final ValueTypeConverter<?> localConverter = node.get(ValueTypeConverter.class);
    if (localConverter != null) {
      return localConverter.toValue(model);
    }

    for (ValueTypeConverter<?> converter : context.getValueTypeConverters()) {
      if (converter.supports(model.getClass())) {
        return converter.toValue(model);
      }
    }

    return model;
  }

}
