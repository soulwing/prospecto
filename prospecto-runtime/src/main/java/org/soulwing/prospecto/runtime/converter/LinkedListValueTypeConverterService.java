/*
 * File created on Apr 1, 2016
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

import java.util.LinkedList;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.converter.Coerce;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.util.SimpleLinkedList;

/**
 * A {@link ValueTypeConverterService} backed by a {@link LinkedList}.
 *
 * @author Carl Harris
 */
public class LinkedListValueTypeConverterService
    extends SimpleLinkedList<ValueTypeConverter>
    implements ValueTypeConverterService {

  @Override
  public Object toViewValue(Object model, ViewNode node, ViewContext context)
      throws Exception {
    if (model == null) return null;

    final ValueTypeConverter localConverter = node.get(ValueTypeConverter.class);
    if (localConverter != null) {
      return localConverter.toViewValue(model, context);
    }

    final ValueTypeConverter converter = findConverter(model.getClass());
    if (converter != null) {
      return converter.toViewValue(model, context);
    }

    return model;
  }

  @Override
  public Object toModelValue(Class<?> type, Object value, ViewNode node,
      ViewContext context) throws Exception {
    if (value == null) return null;

    final ValueTypeConverter localConverter = node.get(ValueTypeConverter.class);
    if (localConverter != null) {
      return localConverter.toModelValue(
          Coerce.toValueOfType(localConverter.getType(), value), context);
    }

    final ValueTypeConverter converter = findConverter(type);
    if (converter != null) {
      return converter.toModelValue(
          Coerce.toValueOfType(converter.getType(), value), context);
    }

    return Coerce.toValueOfType(type, value);
  }

  private ValueTypeConverter findConverter(Class<?> type) {
    for (final ValueTypeConverter converter : toList()) {
      if (converter.supports(type)) {
        return converter;
      }
    }
    return null;
  }

}
