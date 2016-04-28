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

import java.lang.reflect.Array;
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.association.AbstractArrayAssociationManager;
import org.soulwing.prospecto.api.association.AssociationDescriptor;

/**
 * An accessor for the elements of an array.
 *
 * @author Carl Harris
 */
class ArrayAccessor
    extends AbstractArrayAssociationManager<Object, Object>
    implements IndexedMultiValuedAccessor {

  private final Accessor delegate;
  private final Class<?> componentType;

  ArrayAccessor(Accessor delegate, Class<?> componentType) {
    if (!delegate.getDataType().getComponentType()
        .isAssignableFrom(componentType)) {
      throw new IllegalArgumentException("component type "
          + componentType.getSimpleName()
          + " is not compatible with array type "
          + delegate.getDataType().getComponentType());
    }

    this.delegate = delegate;
    this.componentType = componentType;
  }

  @Override
  public Class<?> getComponentType() {
    return componentType;
  }

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return true;
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return delegate.getSupportedModes();
  }

  @Override
  protected Object[] getAssociates(Object owner) throws Exception {
    return (Object[]) delegate.get(owner);
  }

  @Override
  protected void setAssociates(Object owner, Object[] associates)
      throws Exception {
    delegate.set(owner, associates);
  }

  @Override
  protected Object[] newAssociates(int size) {
    return (Object[]) Array.newInstance(
        delegate.getDataType().getComponentType(), size);
  }

}
