/*
 * File created on Apr 2, 2016
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
package org.soulwing.prospecto.runtime.node;

import java.util.List;

import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.reference.ReferenceResolverService;

/**
 * A view node that represents an array of references.
 *
 * @author Carl Harris
 */
public class ArrayOfReferenceNode extends ArrayOfObjectNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param elementName name for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName}
   * @param modelType model type of the array elements
   */
  public ArrayOfReferenceNode(String name, String elementName,
      String namespace, Class<?> modelType) {
    super(name, elementName, namespace, modelType);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    final List<MutableViewEntity> entities = (List<MutableViewEntity>) value;
    final ReferenceResolverService resolvers = context.getReferenceResolvers();
    getMultiValuedAccessor().clear(target);
    for (final MutableViewEntity entity : entities) {
      getMultiValuedAccessor().add(target,
          resolvers.resolve(entity.getType(), entity));
    }
  }

}
