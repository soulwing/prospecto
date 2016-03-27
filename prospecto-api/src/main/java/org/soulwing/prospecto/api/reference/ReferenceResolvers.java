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

import java.util.List;

/**
 * A mutable ordered collection of {@link ReferenceResolver} instances.
 *
 * @author Carl Harris
 */
public interface ReferenceResolvers {

  /**
   * Prepends a resolver to the collection.
   * @param resolvers the resolver to add
   */
  void prepend(ReferenceResolver resolvers);

  /**
   * Appends a resolver to the collection.
   * @param resolvers the resolver to add
   */
  void append(ReferenceResolver resolvers);

  /**
   * Coerces this collection to a {@link List}.
   * <p>
   * The returned list can be manipulated to update this collection of
   * resolvers.
   *
   * @return resolver list
   */
  List<ReferenceResolver> toList();

}
