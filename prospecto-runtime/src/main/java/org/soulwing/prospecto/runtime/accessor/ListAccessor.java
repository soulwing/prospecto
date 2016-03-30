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

import java.util.Iterator;
import java.util.List;

/**
 * A {@link MultiValuedAccessor} for a {@link List}.
 *
 * @author Carl Harris
 */
public class ListAccessor extends AbstractIndexedMultiValuedAccessor {

  public ListAccessor(Accessor delegate) {
    super(delegate);
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
  public void set(Object target, int index, Object value) throws Exception {
    get(target).set(index, value);
  }

  @Override
  public void add(Object target, Object value) throws Exception {
    get(target).add(value);
  }

  @Override
  public void remove(Object target, Object value) throws Exception {
    get(target).remove(value);
  }

  @Override
  public void add(Object target, int index, Object value) throws Exception {
    get(target).add(index, value);
  }

  @Override
  public void remove(Object target, int index) throws Exception {
    get(target).remove(index);
  }

  @SuppressWarnings("unchecked")
  private List<Object> get(Object source) throws Exception {
    return (List<Object>) delegate.get(source);
  }

}
