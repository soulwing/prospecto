/*
 * File created on Mar 22, 2016
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

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.association.AbstractListAssociationManager;
import org.soulwing.prospecto.api.association.AbstractMapAssociationManager;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyMappedAssociationManager;

/**
 * A map accessor.
 *
 * @author Carl Harris
 */
class ConcreteMapAccessor
    extends AbstractMapAssociationManager<Object, Object, Object>
    implements MapAccessor {

  private final Accessor delegate;

  ConcreteMapAccessor(Accessor delegate) {
    this.delegate = delegate;
  }

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return true;
  }

  @Override
  public EnumSet<AccessMode> getSupportedModes() {
    return EnumSet.allOf(AccessMode.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Map<Object, Object> getAssociates(Object owner) throws Exception {
    return (Map<Object, Object>) delegate.get(owner);
  }

  @Override
  protected void setAssociates(Object owner, Map<Object, Object> associates)
      throws Exception {
    delegate.set(owner, associates);
  }

}
