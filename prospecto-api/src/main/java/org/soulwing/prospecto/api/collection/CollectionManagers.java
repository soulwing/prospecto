/*
 * File created on Mar 29, 2016
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
package org.soulwing.prospecto.api.collection;

import java.util.List;

/**
 * A mutable ordered collection of {@link CollectionManager} instances.
 * <p>
 *
 * @author Carl Harris
 */
public interface CollectionManagers {

  /**
   * Appends the given manager to the end of the collection.
   * @param manager the manager to append
   */
  void append(CollectionManager manager);

  /**
   * Inserts the given manager such that it becomes the first manager in the
   * collection.
   * @param manager the collection to insert
   */
  void prepend(CollectionManager manager);

  /**
   * Removes the given manager from the collection
   * <p>
   * Any existing scope identical to {@code manager} is removed
   * @param manager the manager to remove
   * @return {@code true} true if a manager was removed
   */
  boolean remove(CollectionManager manager);

  /**
   * Coerces this collection into a list.
   * <p>
   * The returned list may be manipulated to update this collection of managers.
   * @return list of managers
   */
  List<CollectionManager<?, ?>> toList();

}
