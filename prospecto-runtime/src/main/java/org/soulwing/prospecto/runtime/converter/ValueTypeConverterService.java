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

import org.soulwing.prospecto.api.converter.ValueTypeConverters;
import org.soulwing.prospecto.api.template.ViewNode;

/**
 * A value type converter service.
 *
 * @author Carl Harris
 */
public interface ValueTypeConverterService extends ValueTypeConverters {

  /**
   * Converts a model value to a view value for a node.
   * @param model model value
   * @param node the subject node
   * @return view value
   * @throws Exception
   */
  Object toViewValue(Object model, ViewNode node) throws Exception;

  /**
   * Converts a view value to a model value for a node.
   * @param type target model type
   * @param value view value
   * @param node the subject node
   * @return model value
   * @throws Exception
   */
  Object toModelValue(Class<?> type, Object value, ViewNode node)
      throws Exception;

}
