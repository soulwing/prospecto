/*
 * File created on Mar 11, 2016
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

import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.RootAccessor;

/**
 * A root view node that represents an object.
 *
 * @author Carl Harris
 */
public class RootObjectNode extends ConcreteObjectNode {

  /**
   * Constructs a new instance
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type associated with the node
   */
  public RootObjectNode(String name, String namespace, Class<?> modelType) {
    super(name, namespace, modelType);
  }

  @Override
  public Accessor getAccessor() {
    return RootAccessor.INSTANCE;
  }

}
