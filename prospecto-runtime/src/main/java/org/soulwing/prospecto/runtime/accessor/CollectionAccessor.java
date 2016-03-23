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

import java.util.Collection;
import java.util.Iterator;

/**
 * An accessor for the elements of a collection.
 *
 * @author Carl Harris
 */
public class CollectionAccessor implements MultiValuedAccessor {

  protected Accessor delegate;

  public CollectionAccessor(Accessor delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean canRead() {
    return delegate.canRead();
  }

  @Override
  public boolean canWrite() {
    return delegate.canWrite();
  }

  @Override
  public Iterator<Object> iterator(Object source) throws Exception {
    final Collection<Object> collection = get(source);
    if (collection == null) return null;
    return collection.iterator();
  }

  @Override
  public void add(Object target, Object value) throws Exception {
    get(target).add(value);
  }

  @Override
  public void remove(Object target, Object value) throws Exception {
    get(target).remove(value);
  }

  @SuppressWarnings("unchecked")
  private Collection<Object> get(Object source) throws Exception {
    return (Collection<Object>) delegate.get(source);
  }


}
