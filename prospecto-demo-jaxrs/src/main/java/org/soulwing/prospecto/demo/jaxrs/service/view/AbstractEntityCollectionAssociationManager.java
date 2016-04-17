/*
 * File created on Apr 14, 2016
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
package org.soulwing.prospecto.demo.jaxrs.service.view;

import java.util.Iterator;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.association.AbstractAssociationManager;
import org.soulwing.prospecto.api.association.AbstractCollectionAssociationManager;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.demo.jaxrs.domain.AbstractEntity;

/**
 * An abstract base for association managers that manage a to-many association
 * between an object and an entity type.
 *
 * @param <T> type of the owning side of the association
 * @param <E> type of the owned side of the association
 *
 * @author Carl Harris
 */
abstract class AbstractEntityCollectionAssociationManager
    <T extends Object, E extends AbstractEntity>
        extends AbstractCollectionAssociationManager<T, E> {

  @Override
  public E findAssociate(T owner, ViewEntity associateEntity) throws Exception {
    if (associateEntity.get("id") == null) return null;
    final Iterator<E> i = iterator(owner);
    while (i.hasNext()) {
      final E element = i.next();
      if (element.getId().equals(associateEntity.get("id"))) {
        return element;
      }
    }
    return null;
  }

}
