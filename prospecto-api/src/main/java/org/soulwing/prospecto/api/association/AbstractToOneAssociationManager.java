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

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * An abstract base for {@link ToOneAssociationManager} implementations.
 * <p>
 * @author Carl Harris
 */
public abstract class AbstractToOneAssociationManager<T, E>
    extends AbstractAssociationManager<T, E>
    implements ToOneAssociationManager<T, E> {

  /**
   * Determines whether the given associate entity is logically equivalent to
   * the current associate of the given owner.
   * <p>
   * This implementation instantiates and populates an instance of type
   * {@code E} using the implementation of {@link AssociationManager#newAssociate(Object, ViewEntity, org.soulwing.prospecto.api.factory.ObjectFactory)}
   * on the supertype and compares it (in a null-safe fashion) to the current
   * associate of {@code owner} using {@link #equals(Object)}.
   *
   * @param owner the subject owner
   * @param associateEntity a view entity representing the state of the
   *    associate in the view
   * @param objectFactory
   * @return {@code true} if the given view entity is logically equivalent to
   *    the current associate of {@code entity}
   * @throws Exception
   */
  @Override
  public boolean isSameAssociate(T owner, ViewEntity associateEntity,
      ObjectFactory objectFactory)
      throws Exception {
    final Object currentAssociate = get(owner);
    if (currentAssociate == null && associateEntity == null) return true;
    if (currentAssociate == null || associateEntity == null) return false;
    final Object newAssociate = newAssociate(owner, associateEntity,
        objectFactory);
    return currentAssociate.equals(newAssociate);
  }

}
