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

import org.soulwing.prospecto.api.template.MapOfReferencesNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;

/**
 * A view node that represents an array of references.
 *
 * @author Carl Harris
 */
public class ConcreteMapOfReferencesNode extends ConcreteMapOfObjectsNode
    implements MapOfReferencesNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param keyType type of the map keys
   * @param modelType model type of the map elements
   */
  public ConcreteMapOfReferencesNode(String name,
      String namespace, Class<?> keyType, Class<?> modelType) {
    super(name, namespace, keyType, modelType);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitMapOfReferences(this, state);
  }

}
