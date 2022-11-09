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
package org.soulwing.prospecto.api.converter;

import java.util.List;

/**
 * A mutable ordered collection of {@link KeyTypeConverters}.
 * 
 * @author Carl Harris
 */
public interface KeyTypeConverters {

  /**
   * Appends the given converter to the end of the collection.
   * @param converter the converter to append
   */
  void append(KeyTypeConverter converter);

  /**
   * Inserts the given converter such that it becomes the first converter in
   * the collection.
   * @param converter the converter to insert
   */
  void prepend(KeyTypeConverter converter);

  /**
   * Removes the given converter from the collection.
   * <p>
   * Any existing converter identical to {@code converter} is removed
   * @param converter the converter to remove
   * @return {@code true} if a converter was removed
   */
  boolean remove(KeyTypeConverter converter);

  /**
   * Coerces this collection into a list.
   * <p>
   * The returned list may be manipulated to update this collection of converters.
   * @return list of converters
   */
  List<KeyTypeConverter> toList();

}
