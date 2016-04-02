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
import java.util.Arrays;
import java.util.Iterator;

/**
 * An accessor for the elements of an array.
 *
 * @author Carl Harris
 */
public class ArrayAccessor extends AbstractIndexedMultiValuedAccessor {

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
  public Iterator<Object> iterator(Object source) throws Exception {
    final Object[] array = (Object[]) delegate.get(source);
    if (array == null) return null;
    return Arrays.asList(array).iterator();
  }

  @Override
  public int size(Object source) throws Exception {
    return ((Object[]) delegate.get(source)).length;
  }

  @Override
  public Object get(Object source, int index) throws Exception {
    return ((Object[]) delegate.get(source))[index];
  }

  @Override
  public void set(Object target, int index, Object value) throws Exception {
    ((Object[]) delegate.get(target))[index] = value;
  }

  @Override
  public void add(Object target, Object value) throws Exception {
    add(target, ((Object[]) delegate.get(target)).length, value);
  }

  @Override
  public void remove(Object target, Object value) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(Object target, int index, Object value) throws Exception {
    Object[] array = (Object[]) delegate.get(target);
    assertIndexIsInRange(array, index, array.length);
    Object[] arrayCopy = Arrays.copyOf(array, array.length + 1);
    if (index < array.length) {
      System.arraycopy(arrayCopy, index, arrayCopy, index + 1,
        array.length - index);
    }
    arrayCopy[index] = value;
    delegate.set(target, arrayCopy);
  }

  @Override
  public void remove(Object target, int index) throws Exception {
    Object[] array = (Object[]) delegate.get(target);
    assertIndexIsInRange(array, index, array.length - 1);
    Object[] arrayCopy = array.length - 1 > 0 ?
        Arrays.copyOf(array, array.length - 1) : new Object[0];
    if (array.length - 1 > 0) {
      if (index - 1 >= 0) {
        System.arraycopy(array, 0, arrayCopy, 0, index - 1);
      }
      System.arraycopy(array, index + 1, arrayCopy, index,
          arrayCopy.length - index);
    }
    delegate.set(target, arrayCopy);
  }

  @Override
  public void clear(Object target) throws Exception {
    delegate.set(target, Array.newInstance(
        delegate.getDataType().getComponentType(), 0));
  }

  private static void assertIndexIsInRange(Object[] array, int index,
      int extent) {
    if (index < 0 || index > extent) {
      throw new ArrayIndexOutOfBoundsException("index " + index + " is not in"
          + " range [0, " + extent + "]");
    }

  }

}
