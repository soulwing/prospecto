/*
 * File created on Apr 5, 2016
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
package org.soulwing.prospecto.runtime.association;

import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;

/**
 * A {@link ToManyAssociationUpdater} for a collection of references.

 * @author Carl Harris
 */
public class ReferenceCollectionToManyAssociationUpdater
    extends ValueCollectionToManyAssociationUpdater {

  public static final ReferenceCollectionToManyAssociationUpdater INSTANCE =
      new ReferenceCollectionToManyAssociationUpdater();

  private ReferenceCollectionToManyAssociationUpdater() {
    super();
  }

  ReferenceCollectionToManyAssociationUpdater(
      AssociationDescriptorFactory descriptorFactory,
      AssociationManagerLocator managerLocator) {
    super(descriptorFactory, managerLocator);
  }

  @Override
  protected Object resolve(Object value,
      ScopedViewContext context) {
    final InjectableViewEntity entity = (InjectableViewEntity) value;
    return context.getReferenceResolvers().resolve(entity.getType(), entity);
  }

}
