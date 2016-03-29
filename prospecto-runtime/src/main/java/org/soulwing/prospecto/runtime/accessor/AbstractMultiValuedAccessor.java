/*
 * File created on Mar 29, 2016
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

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.runtime.context.ConcreteViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * An abstract base for {@link MultiValuedAccessor} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractMultiValuedAccessor
    implements MultiValuedAccessor {

  protected Accessor delegate;

  protected AbstractMultiValuedAccessor(Accessor delegate) {
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
  public boolean supports(Class ownerClass, Class elementClass) {
    return true;
  }

  @Override
  public Object newElement(Object owner, ViewEntity elementEntity)
      throws Exception {
    final Object element = elementEntity.getType().newInstance();
    ((MutableViewEntity) elementEntity).inject(
        element, new ConcreteViewContext());
    return element;
  }

  @Override
  public Object find(Object owner, ViewEntity entity) throws Exception {
    Object element = newElement(owner, entity);
    final Iterator<Object> i = iterator(owner);
    while (i.hasNext()) {
      Object candidate = i.next();
      if (candidate.equals(element)) {
        return candidate;
      }
    }
    return null;
  }

}
