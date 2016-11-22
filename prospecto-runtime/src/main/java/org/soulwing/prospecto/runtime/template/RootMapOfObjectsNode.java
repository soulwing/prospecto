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

import java.util.Iterator;
import java.util.Map;

/**
 * A root view node that represents an map of objects.
 *
 * @author Carl Harris
 */
public class RootMapOfObjectsNode extends ConcreteMapOfObjectsNode {

  /**
   * Constructs a new instance.
   * @param name name of the array node
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   */
  public RootMapOfObjectsNode(String name,
      String namespace, Class<?> modelType) {
    super(name, namespace, modelType);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Iterator<Map.Entry> getModelIterator(Object source)
      throws Exception {
    return ((Map) source).entrySet().iterator();
  }

}
