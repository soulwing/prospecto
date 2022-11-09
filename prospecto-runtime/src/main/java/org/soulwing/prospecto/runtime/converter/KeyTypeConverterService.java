/*
 * File created on Nov 6, 2022
 *
 * Copyright (c) 2022 Carl Harris, Jr
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
import org.soulwing.prospecto.api.converter.KeyTypeConverters;
import org.soulwing.prospecto.api.template.ViewNode;

/**
 * A key type converter service.
 *
 * @author Carl Harris
 */
public interface KeyTypeConverterService extends KeyTypeConverters {

  /**
   * Converts a map key from the model to a string value.
   * @param modelKey model key
   * @param node the subject node
   * @param context view context
   * @return key value
   * @throws Exception
   */
  String toViewKey(Object modelKey, ViewNode node, ViewContext context)
      throws Exception;

  /**
   * Converts a map key from a view to the corresponding model key value.
   * @param keyType model key type
   * @param viewKey string representation of the key from a view
   * @param node the subject node
   * @param context view context
   * @return key value
   * @throws Exception
   */
  Object toModelKey(Class<?> keyType, String viewKey, ViewNode node,
      ViewContext context) throws Exception;

}
