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

/**
 * A pass-thru accessor for the object passed as the source to view that
 * represents a single object.
 *
 * @author Carl Harris
 */
public class RootAccessor implements Accessor {

  @Override
  public Class<?> getDataType() {
    throw new UnsupportedOperationException("cannot get data type of root value");
  }

  @Override
  public Object get(Object source) throws Exception {
    if (source instanceof Object[] || source instanceof Collection) {
      throw new IllegalArgumentException("source must be a single object");
    }

    return source;
  }

  @Override
  public void set(Object source, Object value) throws Exception {
    throw new UnsupportedOperationException("cannot set root value");
  }

}
