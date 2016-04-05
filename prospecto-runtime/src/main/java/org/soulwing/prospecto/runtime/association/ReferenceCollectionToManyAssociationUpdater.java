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

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;
import org.soulwing.prospecto.runtime.reference.ReferenceResolverService;

/**
 * A {@link ToManyAssociationUpdater} for a collection of references.
 *
 * @author Carl Harris
 */
public class ReferenceCollectionToManyAssociationUpdater
    implements ToManyAssociationUpdater {

  public static final ReferenceCollectionToManyAssociationUpdater INSTANCE =
      new ReferenceCollectionToManyAssociationUpdater();

  private final AssociationDescriptorFactory descriptorFactory;
  private final AssociationManagerLocator managerLocator;

  private ReferenceCollectionToManyAssociationUpdater() {
    this(ConcreteAssociationDescriptorFactory.INSTANCE,
        ConcreteAssociationManagerLocator.INSTANCE);
  }

  ReferenceCollectionToManyAssociationUpdater(
      AssociationDescriptorFactory descriptorFactory,
      AssociationManagerLocator managerLocator) {
    this.descriptorFactory = descriptorFactory;
    this.managerLocator = managerLocator;
  }

  @Override
  public void update(ContainerViewNode node, Object target,
      Iterable<?> values,
      ToManyAssociationManager defaultManager,
      ScopedViewContext context) throws Exception {

    final AssociationDescriptor descriptor =
        descriptorFactory.newDescriptor(node);

    final ToManyAssociationManager manager =
        managerLocator.findManager(ToManyAssociationManager.class,
            defaultManager, descriptor, node, context);

    doUpdate(target, values, manager, context);
  }

  @SuppressWarnings("unchecked")
  private void doUpdate(Object target, Iterable<?> values,
      ToManyAssociationManager manager, ScopedViewContext context)
      throws Exception {
    final ReferenceResolverService resolvers = context.getReferenceResolvers();
    manager.begin(target);
    manager.clear(target);
    for (final Object value : values) {
      final MutableViewEntity entity = (MutableViewEntity) value;
      manager.add(target,
          resolvers.resolve(entity.getType(), entity));
    }
    manager.end(target);
  }

}
