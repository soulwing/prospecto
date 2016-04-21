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
 * An object that during view application manages the association between an
 * object and an associated object.
 *
 * @author Carl Harris
 */
public interface ToOneAssociationManager<T, E>
    extends AssociationManager<T, E> {

  /**
   * Tests whether the given view entity representing an associate is logically
   * the same object as the owner's current associate.
   * @param owner the subject owner
   * @param associateEntity view entity representing the associate in the view
   * @return {@code true} if {@code associateEntity} is logically the same
   *   associate as the current associate of {@code owner}
   * @throws Exception
   */
  boolean isSameAssociate(T owner, ViewEntity associateEntity) throws Exception;

  /**
   * Gets the current associate.
   * @param owner the subject owner
   * @return associate or {@code null} if there is no associate
   * @throws Exception
   */
  E get(T owner) throws Exception;

  /**
   * Sets the current associate.
   * @param owner the subject owner
   * @param associate the associate to set
   * @throws Exception
   */
  void set(T owner, E associate) throws Exception;

}
