/*
 * File created on Mar 16, 2016
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
import java.util.List;

/**
 * A static utility for producing a {@link MultiValuedAccessor}.
 *
 * @author Carl Harris
 */
public class MultiValuedAccessorFactory {

  public static MultiValuedAccessor newAccessor(Accessor accessor) {
    if (List.class.isAssignableFrom(accessor.getDataType())) {
      return new ListAccessor(accessor);
    }
    if (Collection.class.isAssignableFrom(accessor.getDataType())) {
      return new CollectionAccessor(accessor);
    }
    if (Object[].class.isAssignableFrom(accessor.getDataType())) {
      return new ArrayAccessor(accessor);
    }
    throw new IllegalArgumentException("expected an array or a collection");
  }

}
