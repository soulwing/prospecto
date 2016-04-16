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
package org.soulwing.prospecto.api.association;

import org.soulwing.prospecto.api.ViewEntity;

/**
 * An object that during view application manages the association between an
 * object and a indexed collection or array composed in the object.
 *
 * @author Carl Harris
 */
public interface ToManyIndexedAssociationManager<T, E>
    extends ToManyAssociationManager<T, E> {

  /**
   * Finds the index of the associate of the given owner that is logically
   * equivalent to given view entity.
   * @param owner the subject owner
   * @param associateEntity a view entity representing the state of the
   *    associate in the view
   * @return index of the associate of {@code code} that is logically equivalent
   *    to the given view entity or {@code -1} if no such associate exists
   * @throws Exception
   */
  int indexOf(T owner, ViewEntity associateEntity) throws Exception;


  /**
   * Gets the associate of the given owner at a specified index.
   * @param owner the subject owner
   * @param index index of the desired associate
   * @return associate
   * @throws IndexOutOfBoundsException if not
   *    {@code 0 <= index < number of associates}
   * @throws Exception
   */
  E get(T owner, int index) throws Exception;

  /**
   * Sets (replaces) the associate of the given owner at a specified index.
   * @param owner the subject owner
   * @param index index of the associate to set
   * @param associate replacement associate
   * @throws IndexOutOfBoundsException if not
   *    {@code 0 <= index < number of associates}
   * @throws Exception
   */
  void set(T owner, int index, E associate) throws Exception;

  /**
   * Adds (inserts) an associate of the given owner at a specified index.
   * @param owner the subject owner
   * @param index index at which the new associate will be inserted
   * @param associate replacement associate
   * @throws IndexOutOfBoundsException if not
   *    {@code 0 <= index <= number of associates}
   * @throws Exception
   */
  void add(T owner, int index, E associate) throws Exception;

  /**
   * Removes an associate of the given owner at a specified index.
   * @param owner the subject owner
   * @param index index of the associate to remove
   * @throws IndexOutOfBoundsException if not
   *    {@code 0 <= index < number of associates}
   * @throws Exception
   */
  void remove(T owner, int index) throws Exception;

}
