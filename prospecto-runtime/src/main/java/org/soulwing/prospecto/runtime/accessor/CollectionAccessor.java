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
import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.association.AbstractCollectionAssociationManager;
import org.soulwing.prospecto.api.association.AssociationDescriptor;

/**
 * An accessor for the elements of a collection.
 *
 * @author Carl Harris
 */
class CollectionAccessor
    extends AbstractCollectionAssociationManager<Object, Object>
    implements MultiValuedAccessor {

  private final Accessor delegate;
  private final Class<?> componentType;

  CollectionAccessor(Accessor delegate, Class<?> componentType) {
    this.delegate = delegate;
    this.componentType = componentType;
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return EnumSet.allOf(AccessMode.class);
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
  @SuppressWarnings("unchecked")
  protected Collection<Object> getAssociates(Object owner) throws Exception {
    return (Collection<Object>) delegate.get(owner);
  }

  @Override
  protected void setAssociates(Object owner, Collection<Object> associates)
      throws Exception {
    delegate.set(owner, associates);
  }

}
