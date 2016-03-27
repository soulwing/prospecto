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
package org.soulwing.prospecto.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.handler.ViewListeners;
import org.soulwing.prospecto.api.reference.ReferenceResolver;
import org.soulwing.prospecto.api.reference.ReferenceResolvers;
import org.soulwing.prospecto.api.scope.MutableScope;
import org.soulwing.prospecto.api.scope.Scope;

/**
 * A context used when evaluating view template with a model to produce a view.
 *
 * @author Carl Harris
 */
public interface ViewContext {

  /**
   * Delimiter used in the string representation of a view path
   */
  char PATH_DELIMITER = '/';

  /**
   * Creates a new mutable scope.
   * <p>
   * After creation, the caller must add the scope to the context's
   * {@linkplain #getScopes() configured scopes} in order for it to be
   * consulted when requesting an object from the context.
   *
   * @return new mutable scope instance
   */
  MutableScope newScope();

  /**
   * Creates a new mutable scope appending it to the list of scopes on this
   * context.
   * @return new mutable scope instance
   */
  MutableScope addScope();

  /**
   * Creates a new mutable scope inserting in the list of scopes on this
   * context at the specified index.
   * *
   * @return new mutable scope instance
   */
  MutableScope addScope(int index);

  /**
   * Gets the sequence of scopes that will be consulted when a requested
   * context object is not found in any of the internal scopes managed by the
   * context itself. The scopes are consulted in order until a requested
   * object is found or until all available scopes have been exhausted.
   * <p>
   * The returned list can be manipulated directly to add or remove scopes
   * as needed. Manipulating the list while a view is being generated using
   * this context has no effect.
   *
   * @return scopes
   */
  List<Scope> getScopes();

  /**
   * Gets the collection of listeners associated with this context.
   * <p>
   * The returned collection can be manipulated directly to add or remove
   * listeners as needed. Manipulating the collection during view generation
   * or model update has no effect.
   * @return listener collection
   */
  ViewListeners getListeners();

  /**
   * Gets the set of {@link ValueTypeConverter} instances that will be consulted
   * to convert model value types to view value types and vice-versa.
   * <p>
   * The list of converters can be manipulated directly to add
   * (or remove, or reorder) converters as needed. The order of the converters
   * in this list has an effect on converter selection; the first converter
   * in the list that claims support for a given type {via its
   * {@link ValueTypeConverter#supports(Class)} method}, will be used to
   * convert <em>all</em> model instances of that type.
   * <p>
   * Manipulating the list of converters while a view is being generated using
   * this context has no effect.
   * @return value type converters
   */
  List<ValueTypeConverter<?>> getValueTypeConverters();

  /**
   * Gets the collection of {@link ReferenceResolver} instances that will be
   * consulted to resolve reference objects in a view.
   * <p>
   * The returned collection can be manipulated directly to add or remove
   * resolvers as needed. Manipulating the collection during view generation
   * or model update has no effect.
   * @return resolver collection
   */
  ReferenceResolvers getReferenceResolvers();

  /**
   * Gets the sequence of view node names that form the path to the current
   * view node as a template is being evaluated to produce a view.
   * @return sequence of view node names which is empty before visiting the
   *   root view node
   */
  List<String> currentViewPath();

  /**
   * Gets the sequence of view node names that form the path to the current
   * view node as a template is being evaluated to produce a view.
   * @return sequence of view node names joined using the slash ('/') character;
   *   the string always begins with a leading slash, and contains no additional
   *   name segments before visiting the root view node
   */
  String currentViewPathAsString();

  /**
   * Gets the sequence of model types encountered on view nodes of type
   * object and array-of-object that form the model path to the current view
   * node as a template is being evaluated to produce a view.
   * @return sequence of model types which is empty before visiting the root
   *   view node
   */
  List<Class<?>> currentModelPath();

  /**
   * Gets a singleton object of the given type.
   * @param type the type of object to retrieve
   * @return singleton instance of the given type
   * @throws NullPointerException if there is no object of the given type
   * @throws IllegalStateException if there is more than one object of the
   *    given type
   */
  <T> T get(Class<T> type);

  /**
   * Gets a singleton object of the given type.
   * @param type the type of object to retrieve
   * @return singleton instance of the given type or {@code null} if there
   *    is no object of the given type
   * @throws IllegalStateException if there is more than one object of the
   *    given type
   */
  <T> T getOptional(Class<T> type);

  /**
   * Gets a named object of the given type.
   * @param name name of the object to retrieve
   * @param type expected type of the named object
   * @return instance of the given type that corresponds to the given name
   * @throws NullPointerException exception if there is no object with the
   *    given name
   * @throws ClassCastException if the object with the given name does not
   *    have the expected type
   */
  <T> T get(String name, Class<T> type);

  /**
   * Gets a named object of the given type.
   * @param name name of the object to retrieve
   * @param type expected type of the named object
   * @return instance of the given type that corresponds to the given name
   *    or {@code null} if no such instance exists
   * @throws ClassCastException if the object with the given name does not
   *    have the expected type
   */
  <T> T getOptional(String name, Class<T> type);

  /**
   * Puts an object into the current frame of the context stack.
   * <p>
   * The given value replaces any existing object that is logically equivalent
   * (according to {@link Object#equals(Object)}) to {@code obj}, including
   * any named object
   * @param obj the object to put
   */
  void put(Object obj);

  /**
   * Puts a named object into the current frame of the context stack.
   * <p>
   * The given value replaces any existing object that is logically equivalent
   * (according to {@link Object#equals(Object)}) to {@code obj}, including
   * any unnamed object
   * @param name name of the object
   * @param obj the object to associate with {@code name}
   */
  void put(String name, Object obj);

  /**
   * Puts all of the objects from the given collection into the current frame
   * of the context stack.
   * @param objs the collection of values to put
   */
  void putAll(Collection<?> objs);

  /**
   * Puts all of the named objects from the given map into the current frame
   * of the context stack.
   * @param objs the map of objects to put
   */
  void putAll(Map<String, ?> objs);

  /**
   * Removes an object from the current frame of the context stack.
   * @param value the value to remove
   * @return {@code true} if an object matching the identity of {@code value}
   *   was removed
   */
  boolean remove(Object value);

}
