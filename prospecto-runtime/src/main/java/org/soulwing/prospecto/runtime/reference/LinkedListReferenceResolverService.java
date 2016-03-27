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
package org.soulwing.prospecto.runtime.reference;

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.reference.ReferenceResolver;

/**
 * A {@link ReferenceResolverService} collection implemented using a
 * {@link LinkedList}.
 *
 * @author Carl Harris
 */
public class LinkedListReferenceResolverService
    implements ReferenceResolverService {

  private final List<ReferenceResolver> resolvers = new LinkedList<>();

  @Override
  public Object resolve(Class<?> type, ViewEntity reference) {
    for (final ReferenceResolver resolver : resolvers) {
      if (resolver.supports(type)) return resolver.resolve(type, reference);
    }
    throw new ReferenceResolverNotFoundException(type);
  }

  @Override
  public void prepend(ReferenceResolver resolver) {
    resolvers.add(0, resolver);
  }

  @Override
  public void append(ReferenceResolver resolver) {
    resolvers.add(resolver);
  }

  @Override
  public List<ReferenceResolver> toList() {
    return resolvers;
  }

}
