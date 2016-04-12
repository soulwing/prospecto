/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.runtime.accessor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.soulwing.prospecto.api.AccessMode;

/**
 * An accessor for the elements of an array.
 *
 * @author Carl Harris
 */
public class ArrayAccessor extends AbstractIndexedMultiValuedAccessor {

  private List<Object> buffer;

  public ArrayAccessor(Accessor delegate, Class<?> componentType) {
    super(delegate, componentType);
    if (!delegate.getDataType().getComponentType()
        .isAssignableFrom(componentType)) {
      throw new IllegalArgumentException("component type "
          + componentType.getSimpleName()
          + " is not compatible with array type "
          + delegate.getDataType().getComponentType());
    }
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return delegate.getSupportedModes();
  }

  @Override
  public Iterator<Object> iterator(Object source) throws Exception {
    final Object[] array = (Object[]) delegate.get(source);
    if (array == null) return null;
    return Arrays.asList(array).iterator();
  }

  @Override
  public int size(Object source) throws Exception {
    return getAsList(source, TransactionStatus.MANDATORY).size();
  }

  @Override
  public Object get(Object source, int index) throws Exception {
    return getAsList(source, TransactionStatus.OPTIONAL).get(index);
  }

  @Override
  public void set(Object target, int index, Object value) throws Exception {
    getAsList(target, TransactionStatus.OPTIONAL).set(index, value);
  }

  @Override
  public void add(Object target, Object value) throws Exception {
    assertHasTransaction();
    getAsList(target, TransactionStatus.MANDATORY).add(value);
  }

  @Override
  public boolean remove(Object target, Object value) throws Exception {
    assertHasTransaction();
    return getAsList(target, TransactionStatus.MANDATORY).remove(value);
  }

  @Override
  public void add(Object target, int index, Object value) throws Exception {
    assertHasTransaction();
    getAsList(target, TransactionStatus.MANDATORY).add(index, value);
  }

  @Override
  public void remove(Object target, int index) throws Exception {
    assertHasTransaction();
    getAsList(target, TransactionStatus.MANDATORY).remove(index);
  }

  @Override
  public void clear(Object target) throws Exception {
    assertHasTransaction();
    getAsList(target, TransactionStatus.MANDATORY).clear();
  }

  @Override
  public void begin(Object target) throws Exception {
    assertNoTransaction();
    buffer = new ArrayList<>(Arrays.asList(getAsArray(target)));
  }

  @Override
  public void end(Object target) throws Exception {
    assertHasTransaction();
    Object[] src = buffer.toArray();
    Object[] dest = getAsArray(target);
    if (dest.length != src.length) {
      dest = (Object[]) Array.newInstance(
          delegate.getDataType().getComponentType(), src.length);
      delegate.set(target, dest);
    }
    System.arraycopy(src, 0, dest, 0, src.length);
    buffer = null;
  }

  enum TransactionStatus {
    MANDATORY, OPTIONAL
  }

  private List<Object> getAsList(Object target,
      TransactionStatus transactionStatus) throws Exception {
    if (transactionStatus == TransactionStatus.MANDATORY) {
      assertHasTransaction();
    }
    if (buffer != null) {
      return buffer;
    }
    return Arrays.asList(getAsArray(target));
  }

  private Object[] getAsArray(Object target) throws Exception {
    Object[] array = (Object[]) delegate.get(target);
    if (array == null) {
      array = new Object[0];
    }
    return array;
  }

  private void assertNoTransaction() {
    if (buffer == null) return;
    throw new IllegalStateException("transaction already in progress");
  }

  private void assertHasTransaction() {
    if (buffer != null) return;
    throw new IllegalStateException("no transaction in progress");
  }

}
