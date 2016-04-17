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

  @Override
  public Iterator<E> iterator(T owner) throws Exception {
    final Collection<E> associates = getAssociates(owner);
    if (associates == null) return null;
    return associates.iterator();
  }

  @Override
  public int size(T owner) throws Exception {
    final Collection<E> associates = getAssociates(owner);
    if (associates == null) return 0;
    return associates.size();
  }

  @Override
  public boolean add(T owner, E associate) throws Exception {
    return getOrInitAssociates(owner).add(associate);
  }

  @Override
  public boolean remove(T owner, E associate) throws Exception {
    return getOrInitAssociates(owner).remove(associate);
  }

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
