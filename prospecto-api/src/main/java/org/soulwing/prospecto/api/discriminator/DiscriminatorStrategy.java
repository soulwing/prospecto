/*
 * File created on Mar 16, 2016
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
package org.soulwing.prospecto.api.discriminator;

/**
 * A strategy that produces a {@link Discriminator} for a given model type.
 * <p>
 * The strategy consists of a function which is used to transform a type's
 * class into a value that can be composed in a view and the function's inverse,
 * which utilizes a type's discriminator to determine the corresponding class.
 *
 * @author Carl Harris
 */
public interface DiscriminatorStrategy {

  /**
   * Produces a discriminator for the given subtype of a base type.
   * @param base the base type
   * @param subtype a subtype of base
   * @return discriminator
   */
  Discriminator toDiscriminator(Class<?> base, Class<?> subtype);

  /**
   * Determines the subtype that corresponds to a given discriminator.
   * @param base base type
   * @param discriminator discriminator
   * @param <T> base type
   * @return type that corresponds to {@code discriminator}
   * @throws ClassNotFoundException
   */
  <T> Class<T> toSubtype(Class<T> base, Discriminator discriminator)
      throws ClassNotFoundException;

}
