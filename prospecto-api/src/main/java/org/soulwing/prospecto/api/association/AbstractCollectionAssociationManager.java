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
import java.util.LinkedHashSet;

/**
 * An abstract base for a to-many association that is based on q
 * {@link Collection}.
 *
 * @author Carl Harris
 */
public abstract class AbstractCollectionAssociationManager<T, E>
    extends AbstractToManyAssociationManager<T, E> {

  /**
   * Gets an iterator for the collection of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * collection and if the result is not null, returns its iterator.
   *
   * @param owner association owner
   * @return {@link #getAssociates(Object)}.{@link Collection#iterator()} or
   *     {@code null} if the collection of associates is {@code null}
   * @throws Exception
   */
  @Override
  public Iterator<E> iterator(T owner) throws Exception {
    final Collection<E> associates = getAssociates(owner);
    if (associates == null) return null;
    return associates.iterator();
  }

  /**
   * Gets the size of the collection of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * collection and if the result is not null, returns its size.
   *
   * @param owner association owner
   * @return {@link #getAssociates(Object)}.{@link Collection#size()} or
   *     {@code 0} if the collection of associates is {@code null}
   * @throws Exception
   */
  @Override
  public int size(T owner) throws Exception {
    final Collection<E> associates = getAssociates(owner);
    if (associates == null) return 0;
    return associates.size();
  }

  /**
   * Adds an associate to the collection of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * collection of associates.  If {@code null} it creates a new collection
   * and sets it on the owner (using {@link #newAssociates()}  and
   * {@link #setAssociates(Object, Collection)}). The associate is added
   * using {@link Collection#add(Object)}.
   *
   * @param owner association owner
   * @param associate the associate to add to the collection
   * @return same as {@link Collection#add(Object)}
   * @throws Exception
   */
  @Override
  public boolean add(T owner, E associate) throws Exception {
    return getOrInitAssociates(owner).add(associate);
  }

  /**
   * Removes an associate to the collection of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * collection of associates.  If {@code null} it creates a new collection
   * and sets it on the owner (using {@link #newAssociates()}  and
   * {@link #setAssociates(Object, Collection)}). The associate is removed
   * using {@link Collection#remove(Object)}.
   *
   * @param owner association owner
   * @param associate the associate to add to the collection
   * @return same as {@link Collection#remove(Object)}
   * @throws Exception
   */
  @Override
  public boolean remove(T owner, E associate) throws Exception {
    return getOrInitAssociates(owner).remove(associate);
  }

  /**
   * Clears the collection of associates.
   * <p>
   * This implementation calls {@link #getAssociates(Object)} to get the
   * collection of associates.  If {@code null} it creates a new collection
   * and sets it on the owner (using {@link #newAssociates()}  and
   * {@link #setAssociates(Object, Collection)}). The collection is cleared
   * using {@link Collection#clear()}.
   *
   * @param owner association owner
   * @throws Exception
   */
  @Override
  public void clear(T owner) throws Exception {
    getOrInitAssociates(owner).clear();
  }

  private Collection<E> getOrInitAssociates(T owner) throws Exception {
    Collection<E> associates = getAssociates(owner);
    if (associates == null) {
      associates = newAssociates();
      setAssociates(owner, associates);
    }
    return associates;
  }

  /**
   * Gets the collection of associates.
   * @param owner association owner
   * @return collection
   * @throws Exception
   */
  protected abstract Collection<E> getAssociates(T owner) throws Exception;

  /**
   * Sets the collection of associates.
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
  protected void setAssociates(T owner, Collection<E> associates)
      throws Exception {
    throw new UnsupportedOperationException(
        "manager does not implement setAssociates");
  }

  /**
   * Creates a new (empty) associates collection.
   * <p>
   * The default implementation returns a {@link LinkedHashSet}. Override if
   * you wish to use a different {@link Collection} subtype.
   *
   * @return collection of associates (never {@code null})
   */
  protected Collection<E> newAssociates() {
    return new LinkedHashSet<>();
  }

}
