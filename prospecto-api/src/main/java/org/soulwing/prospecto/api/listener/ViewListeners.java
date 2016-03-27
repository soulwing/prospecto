/*
 * File created on Mar 23, 2016
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
package org.soulwing.prospecto.api.listener;

import java.util.List;

/**
 * A mutable ordered collection of {@link ViewListener} instances.
 *
 * @author Carl Harris
 */
public interface ViewListeners {

  /**
   * Appends the given listener to the end of the collection.
   * @param listener the listener to append
   */
  void append(ViewListener listener);

  /**
   * Inserts the given listener such that it becomes the first listener in
   * the collection.
   * @param listener the listener to add
   */
  void prepend(ViewListener listener);

  /**
   * Removes the given listener from the collection.
   * <p>
   * Any existing listener identical to {@code listener} is removed
   * @param listener the listener to remove
   * @return {@code true} if a listener was removed
   */
  boolean remove(ViewListener listener);

  /**
   * Coerces this collection into a list.
   * <p>
   * The returned list may be manipulated to update this collection of listeners.
   * @return list of listeners
   */
  List<ViewListener> toList();

}
