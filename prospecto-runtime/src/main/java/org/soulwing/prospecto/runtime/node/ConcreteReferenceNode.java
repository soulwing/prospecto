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
package org.soulwing.prospecto.runtime.node;

import org.soulwing.prospecto.api.node.ReferenceNode;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A view node that represents a reference to an object.
 *
 * @author Carl Harris
 */
public class ConcreteReferenceNode extends ConcreteObjectNode
    implements ReferenceNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type associated with node
   */
  public ConcreteReferenceNode(String name, String namespace, Class<?> modelType) {
    super(name, namespace, modelType);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitReference(this, state);
  }

  @Override
  public void inject(Object target, Object value) {}

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    final MutableViewEntity entity = (MutableViewEntity) value;
    getAccessor().set(target, context.getReferenceResolvers()
        .resolve(entity.getType(), (MutableViewEntity) value));
  }

}
