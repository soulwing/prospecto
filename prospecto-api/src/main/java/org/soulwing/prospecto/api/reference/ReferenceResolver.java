/*
 * File created on Mar 26, 2016
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
package org.soulwing.prospecto.api.reference;

import org.soulwing.prospecto.api.ViewEntity;

/**
 * A strategy for resolving a reference entity in a view to an actual object
 * during a model update.
 * <p>
 * An implementation of this interface could, for example, use the properties
 * in the given {@link ViewEntity} to perform a database lookup.
 *
 * @author Carl Harris
 */
public interface ReferenceResolver {

  /**
   * Determines whether this resolver supports the given model type.
   * @param type the subject model type
   * @return {@code true} if this resolver supports the given type
   */
  boolean supports(Class<?> type);

  /**
   * Resolves the given reference entity to an actual object instance of the
   * type represented by the reference.
   * @param reference the subject entity reference to resolve
   * @return reference object or {@code null} if the reference cannot be
   *    resolved (e.g. it does not exist)
   */
  Object resolve(Class<?> type, ViewEntity reference);

}
