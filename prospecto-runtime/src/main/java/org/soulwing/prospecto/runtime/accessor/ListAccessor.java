/*
 * File created on Mar 22, 2016
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

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.soulwing.prospecto.api.AccessMode;

/**
 * A {@link MultiValuedAccessor} for a {@link List}.
 *
 * @author Carl Harris
 */
public class ListAccessor extends AbstractIndexedMultiValuedAccessor {

  public ListAccessor(Accessor delegate, Class<?> componentType) {
    super(delegate, componentType);
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return EnumSet.allOf(AccessMode.class);
  }

  @Override
  public Iterator<Object> iterator(Object source) throws Exception {
    return get(source).iterator();
  }

  @Override
  public int size(Object source) throws Exception {
    return get(source).size();
  }

  @Override
  public Object get(Object source, int index) throws Exception {
    return get(source).get(index);
  }

  @Override
  public void set(Object target, int index, Object associate) throws Exception {
    get(target).set(index, associate);
  }

  @Override
  public void add(Object target, Object associate) throws Exception {
    get(target).add(associate);
  }

  @Override
  public boolean remove(Object target, Object associate) throws Exception {
    return get(target).remove(associate);
  }

  @Override
  public void add(Object target, int index, Object associate) throws Exception {
    get(target).add(index, associate);
  }

  @Override
  public void remove(Object target, int index) throws Exception {
    get(target).remove(index);
  }

  @Override
  public void clear(Object target) throws Exception {
    get(target).clear();
  }

  @SuppressWarnings("unchecked")
  private List<Object> get(Object source) throws Exception {
    return (List<Object>) delegate.get(source);
  }

}
