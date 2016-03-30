/*
 * File created on Mar 30, 2016
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

/**
 * An abstract base for {@link IndexedMultiValuedAccessor} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractIndexedMultiValuedAccessor
    extends AbstractMultiValuedAccessor
    implements IndexedMultiValuedAccessor {

  public AbstractIndexedMultiValuedAccessor(Accessor delegate) {
    super(delegate);
  }

  @Override
  public int indexOf(Object owner, ViewEntity entity) throws Exception {
    Object element = newElement(owner, entity);
    final Iterator<Object> i = iterator(owner);
    int index = 0;
    while (i.hasNext()) {
      Object candidate = i.next();
      if (candidate.equals(element)) {
        return index;
      }
      index++;
    }
    return -1;
  }

}
