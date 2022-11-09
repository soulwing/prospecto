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
import org.soulwing.prospecto.api.converter.KeyTypeConverter;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.util.SimpleLinkedList;

/**
 * A {@link KeyTypeConverterService} backed by a {@link LinkedList}.
 *
 * @author Carl Harris
 */
public class LinkedListKeyTypeConverterService
    extends SimpleLinkedList<KeyTypeConverter>
    implements KeyTypeConverterService {

  @Override
  public String toViewKey(Object model, ViewNode node, ViewContext context)
      throws Exception {
    if (model == null) {
      throw new NullPointerException("null not allowed as a map key in a view");
    }

    final KeyTypeConverter localConverter = node.get(KeyTypeConverter.class);
    if (localConverter != null) {
      return localConverter.toViewKey(model, context);
    }

    final KeyTypeConverter converter = findConverter(model.getClass());
    if (converter != null) {
      return converter.toViewKey(model, context);
    }

    return Coerce.toString(model);
  }

  @Override
  public Object toModelKey(Class<?> type, String value, ViewNode node,
      ViewContext context) throws Exception {
    if (value == null) {
      throw new NullPointerException("null not allowed as a map key in a view");
    }

    final KeyTypeConverter localConverter = node.get(KeyTypeConverter.class);
    if (localConverter != null) {
      return localConverter.toModelKey(value, context);
    }

    final KeyTypeConverter converter = findConverter(type);
    if (converter != null) {
      return converter.toModelKey(value, context);
    }

    return Coerce.toValueOfType(type, value);
  }

  private KeyTypeConverter findConverter(Class<?> type) {
    for (final KeyTypeConverter converter : toList()) {
      if (converter.supports(type)) {
        return converter;
      }
    }
    return null;
  }

}
