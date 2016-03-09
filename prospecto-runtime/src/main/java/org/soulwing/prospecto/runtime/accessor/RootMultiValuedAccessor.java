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

import java.util.Arrays;
import java.util.Iterator;

/**
 * A pass-thru accessor for the multi-valued object passed as the source to view
 * that represents a single object.
 *
 * @author Carl Harris
 */
public class RootMultiValuedAccessor implements MultiValuedAccessor {

  @Override
  public void setDelegate(Accessor accessor) {
  }

  @Override
  @SuppressWarnings("unchecked")
  public Iterator<Object> iterator(Object source) throws Exception {
    if (source instanceof Iterator) {
      return (Iterator<Object>) source;
    }
    if (source instanceof Iterable) {
      return ((Iterable<Object>) source).iterator();
    }
    if (source instanceof Object[]) {
      return Arrays.asList((Object[]) source).iterator();
    }
    throw new IllegalArgumentException(
        "source must be an iterator, iterable, or array");
  }

}
