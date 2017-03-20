/*
 * File created on Mar 24, 2016
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
package org.soulwing.prospecto.api;

import java.util.Set;

/**
 * An abstract entity in a view.
 * <p>
 * An instance of this type is used collect the properties of an entity
 * associated with a node of type object or array-of-objects until an
 * instance of the model type associated with the node can be instantiated.
 *
 * @author Carl Harris
 */
public interface ViewEntity {

  /**
   * Gets the data type represented by this entity.
   * @return data type (or {@code null} if the type is not yet known)
   */
  Class<?> getType();

  /**
   * Gets the set of property names that are present in this entity.
   * @return set of names
   */
  Set<String> nameSet();

  /**
   * Navigates this view entity, following the given path, to obtain
   * a reference to a nested view entity.
   * @param path the navigation path, specified as a dot-delimited sequence
   *    of property names
   * @return view entity or {@code null} if not found
   */
  ViewEntity navigateTo(String path);

  /**
   * Gets a property of the entity.
   * @param name name of the property
   * @return property value or {@code null} if the property has not been set
   */
  Object get(String name);

  /**
   * Gets a property of the entity.
   * @param name name fo the property.
   * @param type expected property type
   * @param <T> property type
   * @return property value or {@code null} if the property has not been set
   * @throws ClassCastException if the property has a type that is not
   *    assignment compatible with {@code type}
   */
  <T> T get(String name, Class<T> type);

  /**
   * Replaces a property value.
   * <p>
   * Any existing value associated with {@code name} is replaced by {@code value}.
   * <p>
   * This method <em>cannot</em> be used to add new property to the entity.
   *
   * @param name name of the property
   * @param value the value to set
   * @throws IllegalArgumentException if name does not have a defined value
   */
  void put(String name, Object value);

  /**
   * Removes a property of the entity.
   * @param name name of the property to remove
   */
  void remove(String name);

  /**
   * Injects the properties of this entity onto the given target model object.
   * @param target the injection target
   */
  void inject(Object target) throws ViewApplicatorException;

}
