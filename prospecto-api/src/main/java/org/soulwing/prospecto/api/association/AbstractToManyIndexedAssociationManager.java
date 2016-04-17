/*
 * File created on Mar 31, 2016
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
package org.soulwing.prospecto.api.association;

import java.util.Iterator;

import org.soulwing.prospecto.api.ViewEntity;

/**
 * An abstract base for {@link ToManyIndexedAssociationManager} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractToManyIndexedAssociationManager<T, E>
    extends AbstractToManyAssociationManager<T, E>
    implements ToManyIndexedAssociationManager<T, E> {

  /**
   * Finds the index of the associate of the given owner that is logically
   * equivalent to given view entity.
   * <p>
   * This implementation instantiates and populates an instance of type
   * {@code E} using the implementation of {@link #newAssociate(Object, ViewEntity)}
   * on the supertype and compares it to each associate of {@code owner}
   * using {@link #equals(Object)} until a match is found.
   *
   * @param owner the subject owner
   * @param associateEntity a view entity representing the state of the
   *    associate in the view
   * @return index of the associate of {@code code} that is logically equivalent
   *    to the given view entity or {@code -1} if no such associate exists
   * @throws Exception
   */
  @Override
  public int indexOf(T owner, ViewEntity associateEntity) throws Exception {
    E element = newAssociate(owner, associateEntity);
    final Iterator<E> i = iterator(owner);
    if (i != null) {
      int index = 0;
      while (i.hasNext()) {
        final E candidate = i.next();
        if (candidate.equals(element)) {
          return index;
        }
        index++;
      }
    }
    return -1;
  }

}
