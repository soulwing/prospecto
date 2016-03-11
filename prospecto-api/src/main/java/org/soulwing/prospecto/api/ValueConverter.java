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

/**
 * A converter that transforms model values of some type for representation as
 * values in a view.
 *
 * @param <M> model type
 * @param <V> view type
 * @author Carl Harris
 */
public interface ValueConverter<M, V> {

  /**
   * Converts a model value (generally of a simple type) to a view
   * representation.
   * @param value the subject model value
   * @return view representation of {@code value}
   * @throws ViewException if conversion fails
   */
  V modelToView(M value) throws ViewException;

  /**
   * Converts a view representation of a value to a model value.
   * @param value the subject view representation of a value
   * @return model value
   * @throws ViewException if conversion fails
   */
  M viewToModel(V value) throws ViewException;

}

