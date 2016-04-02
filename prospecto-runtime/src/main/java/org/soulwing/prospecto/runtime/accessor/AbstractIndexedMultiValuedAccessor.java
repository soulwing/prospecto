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

import org.soulwing.prospecto.api.association.AbstractToManyIndexedAssociationManager;
import org.soulwing.prospecto.api.association.AssociationDescriptor;

/**
 * An abstract base for {@link IndexedMultiValuedAccessor} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractIndexedMultiValuedAccessor
    extends AbstractToManyIndexedAssociationManager<Object, Object>
    implements IndexedMultiValuedAccessor {

  protected final Accessor delegate;
  protected final Class<?> componentType;

  public AbstractIndexedMultiValuedAccessor(Accessor delegate,
      Class<?> componentType) {
    this.delegate = delegate;
    this.componentType = componentType;
  }

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return true;
  }

  @Override
  public Class<?> getComponentType() {
    return componentType;
  }

}
