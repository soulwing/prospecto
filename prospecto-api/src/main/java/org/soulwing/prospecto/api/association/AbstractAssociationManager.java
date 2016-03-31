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

/**
 * An abstract base for {@link AssociationManager} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractAssociationManager<T, E>
    implements AssociationManager<T, E> {

  /**
   * Creates an instance of type {@code E} and populates it using the state
   * of the given view entity.
   * <p>
   * This implementation constructs an instance of type {@code E} using the
   * no-arg constructor and populates its properties by injecting them
   * using {@link ViewEntity#inject(Object)}.
   *
   * @param owner owner object
   * @param associateEntity a view entity representing the state of the
   *    associate in the view
   * @return instance of type {@code E} containing state of
   *    {@code associateEntity}
   * @throws Exception
   */
  @Override
  @SuppressWarnings("unchecked")
  public E newAssociate(T owner, ViewEntity associateEntity) throws Exception {
    final E associate = (E) associateEntity.getType().newInstance();
    associateEntity.inject(associate);
    return associate;
  }

}
