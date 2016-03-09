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

import org.soulwing.prospecto.api.ValueConverter;

/**
 * An accessor that applies a converter.
 *
 * @author Carl Harris
 */
public class ValueConvertingAccessor implements Accessor {

  private final Accessor delegate;
  private final ValueConverter converter;

  public ValueConvertingAccessor(Accessor delegate,
      ValueConverter converter) {
    this.delegate = delegate;
    this.converter = converter;
  }

  @Override
  public Class<?> getDataType() {
    return delegate.getDataType();
  }

  @Override
  public Object get(Object source) throws Exception {
    return converter.modelToView(delegate.get(source));
  }

  @Override
  public void set(Object source, Object value) throws Exception {
    delegate.set(source, converter.viewToModel(value));
  }

}
