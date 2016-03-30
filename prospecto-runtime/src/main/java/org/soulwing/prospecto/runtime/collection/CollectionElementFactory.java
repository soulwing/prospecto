/*
 * File created on Mar 30, 2016
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
package org.soulwing.prospecto.runtime.collection;

import org.soulwing.prospecto.api.collection.CollectionManager;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A static factory method for an element in a collection.
 *
 * @author Carl Harris
 */
class CollectionElementFactory {

  @SuppressWarnings("unchecked")
  public static Object newElement(Object target, MutableViewEntity entity,
      CollectionManager manager) throws Exception {
    Object newElement = manager.newElement(target, entity);
    if (newElement == null) {
      newElement = manager.newElement(target, entity);
    }
    return newElement;
  }

}
