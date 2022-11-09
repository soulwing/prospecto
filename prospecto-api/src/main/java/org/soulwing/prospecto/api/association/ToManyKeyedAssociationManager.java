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

import java.util.Map;

/**
 * An object that during view application manages the association between an
 * object and a map composed in the object.
 * <p>
 * A manager is used, by the underlying framework, in a transactional fashion.
 * Before invoking methods to update the associated map, the framework
 * will first invoke the {@link #begin(Object)} method to inform the manager
 * that a sequence of map operations will be performed. When all
 * update operations have been performed, the framework will notify the manager
 * that the update is complete by invoking the {@link #end(Object)} method.
 *
 * @author Carl Harris
 */
public interface ToManyKeyedAssociationManager<T, K, E>
    extends ToManyAssociationManager<T, Map.Entry<K, E>> {

  /**
   * Gets the associate of the given owner having the specified key.
   * @param owner the subject owner
   * @param key key of the desired associate
   * @return associate or {@code null} if key does not exist in the map
   * @throws Exception
   */
  E get(T owner, K key) throws Exception;

}
