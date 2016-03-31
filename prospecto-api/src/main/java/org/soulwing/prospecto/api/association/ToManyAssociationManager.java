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

import org.soulwing.prospecto.api.ViewEntity;

/**
 * An object that during model update manages the association between an object
 * and a collection composed in the object.
 *
 * @author Carl Harris
 */
public interface ToManyAssociationManager<T, E> extends AssociationManager {

  boolean supports(Class<?> ownerClass, Class<?> elementClass);

  Iterator<E> iterator(T owner) throws Exception;

  int size(T owner) throws Exception;

  E find(T owner, ViewEntity elementEntity) throws Exception;

  E newElement(T owner, ViewEntity elementEntity) throws Exception;

  void add(T owner, E element) throws Exception;

  void remove(T owner, E element) throws Exception;

}
