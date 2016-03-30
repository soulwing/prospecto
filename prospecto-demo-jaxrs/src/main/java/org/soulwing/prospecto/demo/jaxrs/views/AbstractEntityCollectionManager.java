/*
 * File created on Mar 29, 2016
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
package org.soulwing.prospecto.demo.jaxrs.views;

import java.util.Iterator;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.collection.CollectionManager;
import org.soulwing.prospecto.demo.jaxrs.domain.AbstractEntity;

/**
 * A
 * @author Carl Harris
 */
abstract class AbstractEntityCollectionManager
    <T extends AbstractEntity, E extends AbstractEntity>
        implements CollectionManager<T, E> {

  @Override
  public E find(T owner, ViewEntity elementEntity) throws Exception {
    if (elementEntity.get("id") == null) return null;
    final Iterator<E> i = iterator(owner);
    while (i.hasNext()) {
      final E element = i.next();
      if (element.getId().equals(elementEntity.get("id"))) {
        return element;
      }
    }
    return null;
  }

}
