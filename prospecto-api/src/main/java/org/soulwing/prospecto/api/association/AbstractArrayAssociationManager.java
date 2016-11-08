/*
 * File created on Apr 28, 2016
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.factory.ObjectFactory;

/**
 * An abstract base for a manager of an indexed to-many association that is
 * based on an array.
 *
 * @author Carl Harris
 */
public abstract class AbstractArrayAssociationManager<T, E>
    extends AbstractToManyIndexedAssociationManager<T, E>
    implements Stateful {

  private ToManyIndexedAssociationManager<T, E> delegate =
      new DefaultArrayAssociationManager();

  @Override
  public Iterator<E> iterator(T owner) throws Exception {
    return delegate.iterator(owner);
  }

  @Override
  public int size(T owner) throws Exception {
    return delegate.size(owner);
  }

  @Override
  public E get(T owner, int index) throws Exception {
    return delegate.get(owner, index);
  }

  @Override
  public void set(T owner, int index, E associate) throws Exception {
    delegate.set(owner, index, associate);
  }

  @Override
  public boolean add(T owner, E associate) throws Exception {
    return delegate.add(owner, associate);
  }

  @Override
  public boolean remove(T owner, E associate) throws Exception {
    return delegate.remove(owner, associate);
  }

  @Override
  public void add(T owner, int index, E associate) throws Exception {
    delegate.add(owner, index, associate);
  }

  @Override
  public void remove(T owner, int index) throws Exception {
    delegate.remove(owner, index);
  }

  @Override
  public void clear(T owner) throws Exception {
    delegate.clear(owner);
  }

  @Override
  public void begin(T owner) throws Exception {
    super.begin(owner);
    if (TransactionalArrayAssociationManager.class.isInstance(delegate)) {
      throw new IllegalStateException("transaction already in progress");
    }
    delegate = new TransactionalArrayAssociationManager(
        new ArrayList<>(Arrays.asList(getOrInitAssociates(owner))));
  }

  @Override
  public void end(T owner) throws Exception {
    if (!TransactionalArrayAssociationManager.class.isInstance(delegate)) {
      throw new IllegalStateException("no transaction in progress");
    }

    final Object[] src = ((TransactionalArrayAssociationManager) delegate)
        .buffer.toArray();

    E[] dest = getOrInitAssociates(owner);
    if (dest.length != src.length) {
      dest = newAssociates(src.length);
    }
    System.arraycopy(src, 0, dest, 0, src.length);
    setAssociates(owner, dest);
    delegate = new DefaultArrayAssociationManager();
    super.end(owner);
  }

  @Override
  public AbstractArrayAssociationManager clone() {
    try {
      return (AbstractArrayAssociationManager) super.clone();
    }
    catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }

  private E[] getOrInitAssociates(T owner) throws Exception {
    E[] associates = getAssociates(owner);
    if (associates == null) {
      associates = newAssociates(0);
      setAssociates(owner, associates);
    }
    return associates;
  }

  /**
   * Gets the array of associates.
   * @param owner association owner
   * @return array of associates (may be {@code null})
   * @throws Exception
   */
  protected abstract E[] getAssociates(T owner) throws Exception;

  /**
   * Sets the array of associates.
   * <p>
   * This method is called when a manager method is called to modify the
   * list and {@link #getAssociates(Object)} returns {@code null}, as well
   * as when it is necessary to resize the array.
   * <p>
   * @param owner the association owner
   * @param associates the array to set
   * @throws Exception
   */
  protected abstract void setAssociates(T owner, E[] associates)
      throws Exception;

  /**
   * Creates a new associates array of the specified size.
   * @param size size of the array to create
   * @return array of type {@code E} (never {@code null})
   */
  protected abstract E[] newAssociates(int size);


  private class DefaultArrayAssociationManager
      implements ToManyIndexedAssociationManager<T, E> {
    @Override
    public boolean supports(AssociationDescriptor descriptor) {
      return AbstractArrayAssociationManager.this.supports(descriptor);
    }

    @Override
    public E newAssociate(T owner, ViewEntity associateEntity, ObjectFactory objectFactory) throws Exception {
      return AbstractArrayAssociationManager.this.newAssociate(owner,
          associateEntity, objectFactory);
    }

    @Override
    public Iterator<E> iterator(T owner) throws Exception {
      final E[] array = getAssociates(owner);
      if (array == null) return null;
      return Arrays.asList(array).iterator();
    }

    @Override
    public int size(T owner) throws Exception {
      final E[] array = getAssociates(owner);
      if (array == null) return 0;
      return array.length;
    }

    @Override
    public E findAssociate(T owner, ViewEntity associateEntity,
        ObjectFactory objectFactory) throws Exception {
      return AbstractArrayAssociationManager.this.findAssociate(owner,
          associateEntity, objectFactory);
    }

    @Override
    public int indexOf(T owner, ViewEntity associateEntity,
        ObjectFactory objectFactory) throws Exception {
      return AbstractArrayAssociationManager.this.indexOf(owner,
          associateEntity, objectFactory);
    }

    @Override
    public E get(T owner, int index) throws Exception {
      return getOrInitAssociates(owner)[index];
    }

    @Override
    public void set(T owner, int index, E associate) throws Exception {
      getOrInitAssociates(owner)[index] = associate;
    }

    @Override
    public boolean add(T owner, E associate) throws Exception {
      throw new IllegalStateException("no transaction in progress");
    }

    @Override
    public boolean remove(T owner, E associate) throws Exception {
      throw new IllegalStateException("no transaction in progress");
    }

    @Override
    public void add(T owner, int index, E associate) throws Exception {
      throw new IllegalStateException("no transaction in progress");
    }

    @Override
    public void remove(T owner, int index) throws Exception {
      throw new IllegalStateException("no transaction in progress");
    }

    @Override
    public void clear(T owner) throws Exception {
      throw new IllegalStateException("no transaction in progress");
    }

    @Override
    public void begin(T owner) throws Exception {
      throw new UnsupportedOperationException();
    }

    @Override
    public void end(T owner) throws Exception {
      throw new UnsupportedOperationException();
    }

  }

  private class TransactionalArrayAssociationManager
      extends DefaultArrayAssociationManager {

    private final List<E> buffer;

    public TransactionalArrayAssociationManager(List<E> buffer) {
      this.buffer = buffer;
    }

    @Override
    public Iterator<E> iterator(T owner) throws Exception {
      return buffer.iterator();
    }

    @Override
    public int size(T owner) throws Exception {
      return buffer.size();
    }

    @Override
    public E get(T owner, int index) throws Exception {
      return buffer.get(index);
    }

    @Override
    public void set(T owner, int index, E associate) throws Exception {
      buffer.set(index, associate);
    }

    @Override
    public boolean add(T owner, E associate) throws Exception {
      return buffer.add(associate);
    }

    @Override
    public boolean remove(T owner, E associate) throws Exception {
      return buffer.remove(associate);
    }

    @Override
    public void add(T owner, int index, E associate) throws Exception {
      buffer.add(index, associate);
    }

    @Override
    public void remove(T owner, int index) throws Exception {
      buffer.remove(index);
    }

    @Override
    public void clear(T owner) throws Exception {
      buffer.clear();
    }

  }

}
