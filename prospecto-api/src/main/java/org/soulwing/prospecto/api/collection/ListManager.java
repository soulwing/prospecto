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
package org.soulwing.prospecto.api.collection;

/**
 * An object that during model update manages the relationship between an object
 * and an array/collection composed in the object.
 *
 * @author Carl Harris
 */
public interface ListManager<T, E> extends CollectionManager<T, E> {

  void add(T owner, int index, E element) throws Exception;

  void remove(T owner, int index) throws Exception;

}
