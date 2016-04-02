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

import org.soulwing.prospecto.api.ViewTemplateException;

/**
 * A {@link MultiValuedAccessorFactory} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteMultiValuedAccessorFactory
    implements MultiValuedAccessorFactory {

  public static final ConcreteMultiValuedAccessorFactory INSTANCE =
      new ConcreteMultiValuedAccessorFactory();

  private ConcreteMultiValuedAccessorFactory() {}

  @Override
  public MultiValuedAccessor newAccessor(Accessor accessor,
      Class<?> componentType) {
    if (List.class.isAssignableFrom(accessor.getDataType())) {
      return new ListAccessor(accessor, componentType);
    }
    if (Collection.class.isAssignableFrom(accessor.getDataType())) {
      return new CollectionAccessor(accessor, componentType);
    }
    if (accessor.getDataType().isArray()) {
      return new ArrayAccessor(accessor, componentType);
    }
    throw new ViewTemplateException("expected an array or a collection");
  }

}
