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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An abstract base for an indexed to-many association that is based on a
 * {@link List}.
 *
 * @author Carl Harris
 */
public abstract class AbstractListAssociationManager<T, E>
    extends AbstractToManyIndexedAssociationManager<T, E> {

  @Override
  public Iterator<E> iterator(T owner) throws Exception {
    final List<E> associates = getAssociates(owner);
    if (associates == null) return null;
    return associates.iterator();
  }

  @Override
  public int size(T owner) throws Exception {
    final List<E> associates = getAssociates(owner);
    if (associates == null) return 0;
    return associates.size();
  }

  @Override
  public E get(T owner, int index) throws Exception {
    return getOrInitAssociates(owner).get(index);
  }

  @Override
  public void set(T owner, int index, E associate) throws Exception {
    getOrInitAssociates(owner).set(index, associate);
  }

  @Override
  public void add(T owner, E associate) throws Exception {
    getOrInitAssociates(owner).add(associate);
  }

  @Override
  public void add(T owner, int index, E associate) throws Exception {
    getOrInitAssociates(owner).add(index, associate);
  }

  @Override
  public boolean remove(T owner, E associate) throws Exception {
    return getOrInitAssociates(owner).remove(associate);
  }

  @Override
  public void remove(T owner, int index) throws Exception {
    getOrInitAssociates(owner).remove(index);
  }

  @Override
  public void clear(T owner) throws Exception {
    getOrInitAssociates(owner).clear();
  }

  private List<E> getOrInitAssociates(T owner) throws Exception {
    List<E> associates = getAssociates(owner);
    if (associates == null) {
      associates = newAssociates();
      setAssociates(owner, associates);
    }
    return associates;
  }

  /**
   * Gets the list of associates.
   * @param owner association owner
   * @return list
   * @throws Exception
   */
  protected abstract List<E> getAssociates(T owner) throws Exception;

  /**
   * Sets the list of associates.
   * <p>
   * This method is called when a manager method is called to modify the
   * list and {@link #getAssociates(Object)} returns {@code null}. The
   * default implementation throws {@link UnsupportedOperationException}.
   * <p>
   * If your implementation of {@link #getAssociates(Object)} can return
   * {@code null}, you must override this method to allow the list to
   * be initialized.
   *
   * @param owner the association owner
   * @throws Exception
   */
  protected void setAssociates(T owner, List<E> associates)
      throws Exception {
    throw new UnsupportedOperationException(
        "manager does not implement setAssociates");
  }

  /**
   * Creates a new (empty) associates list.
   * <p>
   * The default implementation returns a {@link ArrayList}. Override if
   * you wish to use a different {@link List} subtype.
   *
   * @return list of associates (never {@code null})
   */
  protected List<E> newAssociates() {
    return new ArrayList<>();
  }

}
