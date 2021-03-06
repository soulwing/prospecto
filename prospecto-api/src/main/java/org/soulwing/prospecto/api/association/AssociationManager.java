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
 * A marker interface for association managers.
 *
 * @author Carl Harris
 */
public interface AssociationManager<T, E> {

  /**
   * Tests whether this manager supports a given association.
   * @param descriptor association descriptor
   * @return {@code true} if
   */
  boolean supports(AssociationDescriptor descriptor);

  /**
   * Creates an instance of type {@code E} and populates it using the state
   * of the given view entity.
   *
   * @param owner owner object
   * @param associateEntity a view entity representing the state of the
   *    associate in the view
   * @param objectFactory object factory that may be used to create the
   *    new associate object
   * @return instance of type {@code E} containing state of
   *    {@code associateEntity}
   * @throws Exception
   */
  E newAssociate(T owner, ViewEntity associateEntity,
      ObjectFactory objectFactory) throws Exception;

}
