/*
 * File created on Mar 27, 2016
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
package org.soulwing.prospecto.api.scope;

import java.util.List;

/**
 * A mutable ordered collection of {@link Scope} instances.
 *
 * @author Carl Harris
 */
public interface Scopes {

  /**
   * Appends the given scope to the end of the collection.
   * @param scope the scope to append
   */
  void append(Scope scope);

  /**
   * Inserts the given scope such that it becomes the first scope in
   * the collection.
   * @param scope the scope to insert
   */
  void prepend(Scope scope);

  /**
   * Removes the given scope from the collection.
   * <p>
   * Any existing scope identical to {@code listener} is removed
   * @param scope the scope to remove
   * @return {@code true} if a scope was removed
   */
  boolean remove(Scope scope);

  /**
   * Coerces this collection into a list.
   * <p>
   * The returned list may be manipulated to update this collection of scopes.
   * @return list of scopes
   */
  List<Scope> toList();

}
