/*
 * File created on Apr 17, 2016
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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * An abstract base for a manager of a to-many association that is based on a
 * {@link Map}.
 *
 * @author Carl Harris
 */
public abstract class AbstractMapAssociationManager<T, K, E>
    extends AbstractAssociationManager<T, Map.Entry<K, E>>
    implements ToManyKeyedAssociationManager<T, K, E> {

  /**
   * Gets an iterator for the map of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * map and if the result is not null, returns its iterator.
   *
   * @param owner association owner
   * @return {@link #getAssociates(Object)}.{@link Map#entrySet()}.{@link Set#iterator()} or
   *     {@code null} if the map of associates is {@code null}
   * @throws Exception
   */
  @Override
  public Iterator<Map.Entry<K, E>> iterator(T owner) throws Exception {
    final Map<K, E> associates = getAssociates(owner);
    if (associates == null) return null;
    return associates.entrySet().iterator();
  }

  /**
   * Gets the size of the map of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * map and if the result is not null, returns its size.
   *
   * @param owner association owner
   * @return {@link #getAssociates(Object)}.{@link Map#size()} or
   *     {@code 0} if the map of associates is {@code null}
   * @throws Exception
   */
  @Override
  public int size(T owner) throws Exception {
    final Map<K, E> associates = getAssociates(owner);
    if (associates == null) return 0;
    return associates.size();
  }

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return false;
  }

  @Override
  public Map.Entry<K, E> findAssociate(T owner, ViewEntity associateEntity,
      ObjectFactory objectFactory) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public E get(T owner, K key) throws Exception {
    return getAssociates(owner).get(key);
  }

  /**
   * Adds an associate to the map of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * map of associates.  If {@code null} it creates a new map
   * and sets it on the owner (using {@link #newAssociates()}  and
   * {@link #setAssociates(Object, Map)}). The associate is added
   * using {@link Map#put(Object, Object)}.
   *
   * @param owner association owner
   * @param associate the associate to add to the collection
   * @return true if an associate was added (not replaced)
   * @throws Exception
   */
  @Override
  public boolean add(T owner, Map.Entry<K, E> associate) throws Exception {
    return getOrInitAssociates(owner).put(associate.getKey(), associate.getValue()) == null;
  }

  /**
   * Removes an associate from the map of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * map of associates.  If {@code null} it creates a new map
   * and sets it on the owner (using {@link #newAssociates()}  and
   * {@link #setAssociates(Object, Map)}). The associate is removed
   * using {@link Collection#remove(Object)}.
   *
   * @param owner association owner
   * @param associate the associate to add to the collection
   * @return same as {@link Collection#remove(Object)}
   * @throws Exception
   */
  @Override
  public boolean remove(T owner, Map.Entry<K, E> associate) throws Exception {
    return getOrInitAssociates(owner).remove(associate.getKey(), associate.getValue());
  }

  /**
   * Clears the map of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * map of associates.  If {@code null} it creates a new map
   * and sets it on the owner (using {@link #newAssociates()} and
   * {@link #setAssociates(Object, Map)}). The map is cleared
   * using {@link Map#clear()}.
   *
   * @param owner association owner
   * @throws Exception
   */
  @Override
  public void clear(T owner) throws Exception {
    getOrInitAssociates(owner).clear();
  }

  private Map<K, E> getOrInitAssociates(T owner) throws Exception {
    Map<K, E> associates = getAssociates(owner);
    if (associates == null) {
      associates = newAssociates();
      setAssociates(owner, associates);
    }
    return associates;
  }

  /**
   * Gets the map of associates.
   * @param owner association owner
   * @return map
   * @throws Exception
   */
  protected abstract Map<K, E> getAssociates(T owner) throws Exception;

  /**
   * Sets the map of associates.
   * <p>
   * This method is called when a manager method is called to modify the
   * collection and {@link #getAssociates(Object)} returns {@code null}. The
   * default implementation throws {@link UnsupportedOperationException}.
   * <p>
   * If your implementation of {@link #getAssociates(Object)} can return
   * {@code null}, you must override this method to allow the collection to
   * be initialized.
   *
   * @param owner the association owner
   * @throws Exception
   */
  protected void setAssociates(T owner, Map<K, E> associates)
      throws Exception {
    throw new UnsupportedOperationException(
        "manager does not implement setAssociates");
  }

  /**
   * Creates a new (empty) associates map.
   * <p>
   * The default implementation returns a {@link LinkedHashMap}. Override if
   * you wish to use a different {@link Collection} subtype.
   *
   * @return collection of associates (never {@code null})
   */
  protected Map<K, E> newAssociates() {
    return new LinkedHashMap<>();
  }

  /**
   * Begins a transaction for updating the associated collection.
   * <p>
   * This implementation does nothing. Subclasses should override as needed.
   * @param owner association owner
   * @throws Exception
   */
  @Override
  public void begin(T owner) throws Exception {}

  /**
   * Ends a transaction that has updated the associated collection.
   * <p>
   * This implementation does nothing. Subclasses should override as needed.
   * @param owner association owner
   * @throws Exception
   */
  @Override
  public void end(T owner) throws Exception {}

}
