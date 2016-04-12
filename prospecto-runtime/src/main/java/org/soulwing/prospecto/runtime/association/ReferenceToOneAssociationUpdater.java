/*
 * File created on Apr 7, 2016
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

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToOneAssociationManager;
import org.soulwing.prospecto.api.template.ContainerNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;

/**
 * A {@link ToManyAssociationUpdater} for a collection of values.

 * @author Carl Harris
 */
public class ReferenceToOneAssociationUpdater implements ToOneAssociationUpdater {

  public static final ReferenceToOneAssociationUpdater INSTANCE =
      new ReferenceToOneAssociationUpdater();

  private final AssociationDescriptorFactory descriptorFactory;
  private final AssociationManagerLocator managerLocator;

  private ReferenceToOneAssociationUpdater() {
    this(ConcreteAssociationDescriptorFactory.INSTANCE,
        ConcreteAssociationManagerLocator.INSTANCE);
  }

  ReferenceToOneAssociationUpdater(
      AssociationDescriptorFactory descriptorFactory,
      AssociationManagerLocator managerLocator) {
    this.descriptorFactory = descriptorFactory;
    this.managerLocator = managerLocator;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void update(ContainerNode node, Object target,
      InjectableViewEntity entity,
      ToOneAssociationManager defaultManager, ScopedViewContext context)
      throws Exception {

    final AssociationDescriptor descriptor =
        descriptorFactory.newDescriptor(node);

    final ToOneAssociationManager manager =
        managerLocator.findManager(ToOneAssociationManager.class,
            defaultManager, descriptor, node, context);

    final Object associate = entity != null ?
        context.getReferenceResolvers().resolve(entity.getType(), entity) : null;

    if (associate != UndefinedValue.INSTANCE) {
      manager.set(target, associate);
    }
  }

}
