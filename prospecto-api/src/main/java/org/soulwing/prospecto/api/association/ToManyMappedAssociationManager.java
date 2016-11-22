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

import java.util.Iterator;
import java.util.Map;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * An object that during view application manages the association between an
 * object and a mapped collection.
 *
 * @author Carl Harris
 */
public interface ToManyMappedAssociationManager<T, K, E>
    extends AssociationManager<T, E> {

  /**
   * Gets an iterator for the associated collection.
   * @param owner association owner
   * @return map entry iterator
   * @throws Exception
   */
  Iterator<Map.Entry> iterator(T owner) throws Exception;

  /**
   * Gets the size of the associated collection.
   * @param owner association owner
   * @return collection size
   * @throws Exception
   */
  int size(T owner) throws Exception;

  /**
   * Gets an associate of the given owner with a given key
   * @param owner the subject owner
   * @param key index of the desired associate
   * @return associate or {@code null} if there exists no associate with the
   *    given key
   * @throws Exception
   */
  E get(T owner, K key) throws Exception;

  /**
   * Puts (replaces or adds) the associate of the given owner at a specified
   * index.
   * @param owner the subject owner
   * @param key of the associate to put
   * @param associate replacement associate
   * @throws Exception
   */
  void put(T owner, K key, E associate) throws Exception;

  /**
   * Removes the associate of the given owner with the given key
   * @param owner the subject owner
   * @param key key of the associate to remove
   * @throws Exception
   */
  void remove(T owner, K key) throws Exception;

  /**
   * Removes all associates from the collection.
   * @param owner association owner
   * @throws Exception
   */
  void clear(T owner) throws Exception;

  /**
   * Begins a transaction for updating the associated collection.
   * @param owner association owner
   * @throws Exception
   */
  void begin(T owner) throws Exception;

  /**
   * Ends a transaction that has updated the associated collection.
   * @param owner association owner
   * @throws Exception
   */
  void end(T owner) throws Exception;

}
