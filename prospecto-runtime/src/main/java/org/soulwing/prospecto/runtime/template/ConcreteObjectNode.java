/*
 * File created on Apr 8, 2016
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
package org.soulwing.prospecto.runtime.template;

import java.beans.Introspector;

import org.soulwing.prospecto.api.association.ToOneAssociationManager;
import org.soulwing.prospecto.api.template.ObjectNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;

/**
 * A view node that represents an object.
 *
 * @author Carl Harris
 */
public class ConcreteObjectNode extends AbstractContainerNode
    implements ObjectNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type associated with node
   */
  public ConcreteObjectNode(String name, String namespace, Class<?> modelType) {
    super(name, namespace, null, modelType);
  }

  @Override
  public String getName() {
    String name = super.getName();
    if (name == null) {
      name = Introspector.decapitalize(getModelType().getSimpleName());
    }
    return name;
  }

  @Override
  public Object getObject(Object model) throws Exception {
    return getAccessor().get(model);
  }

  @Override
  public ToOneAssociationManager<?, ?> getDefaultManager() {
    return getAccessor();
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitObject(this, state);
  }

}
