/*
 * File created on Apr 6, 2016
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
package org.soulwing.prospecto.runtime.listener;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A service that supports transformation of model values during view
 * generation and transformation of view values during view application.
 *
 * @author Carl Harris
 */
public interface TransformationService {

  /**
   * Derives the value to extract for a model property.
   * <p>
   * Property listeners on the view context are first allowed to transform the
   * value as desired. Then a value type converter (if available) is used to
   * converter the transformed model value to its view representation.
   *
   * @param owner owner of the property in the model
   * @param modelValue value of the property in model representation
   * @param node associated view node
   * @param context view context
   * @return derived view property value
   * @throws Exception
   */
  Object valueToExtract(Object owner, Object modelValue, ViewNode node,
      ScopedViewContext context) throws Exception;

  /**
   * Derives the value to inject for a model property.
   * <p>
   * The view value is first converted to an instance of the model data type.
   * Then property listeners on the view context are allowed to transform the
   * value as desired to arrive at the resulting derived value.
   *
   * @param ownerEntity view entity representing the owner of the property
   * @param type property type in the model
   * @param viewValue value of the property in view representation
   * @param node associated view node
   * @param context view context
   * @return derived model property value
   * @throws Exception
   */
  Object valueToInject(ViewEntity ownerEntity, Class<?> type, Object viewValue,
      ViewNode node, ScopedViewContext context) throws Exception;

  /**
   * Derives the key to extract for an entry in a map associated with a
   * <em>map-of</em> node.
   * <p>
   * A value type converter (if available) is used to convert the transformed
   * model key to its view representation.
   *
   * @param owner owner of the property in the model
   * @param modelKey value of the key in model representation
   * @param node associated view node
   * @param context view context
   * @return derived view key value
   * @throws Exception
   */
  String keyToExtract(Object owner, Object modelKey, ViewNode node,
      ScopedViewContext context) throws Exception;

  /**
   * Derives the value to inject for a model property.
   * <p>
   * The view key is converted to an instance of the model key type using
   * a value type converter.
   *
   * @param ownerEntity view entity representing the owner of the map
   * @param type key type in the model
   * @param viewKey value of the key in view representation
   * @param node associated view node
   * @param context view context
   * @return derived model key value
   * @throws Exception
   */
  Object keyToInject(ViewEntity ownerEntity, Class<?> type, String viewKey,
      ViewNode node, ScopedViewContext context) throws Exception;

}
