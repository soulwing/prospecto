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
package org.soulwing.prospecto.runtime.association;

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A {@link ToOneAssociationUpdater} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteToManyAssociationUpdater implements ToManyAssociationUpdater {

  private static final ToManyAssociationUpdateStrategy[] DEFAULT_STRATEGIES = {
      OrderedToManyAssociationUpdateStrategy.INSTANCE,
      UnorderedToManyAssociationUpdateStrategy.INSTANCE
  };

  public static final ConcreteToManyAssociationUpdater INSTANCE =
      new ConcreteToManyAssociationUpdater();

  private final ToManyAssociationUpdateStrategy[] strategies;
  private final AssociationDescriptorFactory descriptorFactory;
  private final AssociationManagerLocator managerLocator;

  private ConcreteToManyAssociationUpdater() {
    this(DEFAULT_STRATEGIES,
        ConcreteAssociationDescriptorFactory.INSTANCE,
        ConcreteAssociationManagerLocator.INSTANCE);
  }

  ConcreteToManyAssociationUpdater(
      ToManyAssociationUpdateStrategy[] strategies,
      AssociationDescriptorFactory descriptorFactory,
      AssociationManagerLocator managerLocator) {
    this.strategies = strategies;
    this.descriptorFactory = descriptorFactory;
    this.managerLocator = managerLocator;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void update(UpdatableNode node, Object target,
      Iterable<?> values, ToManyAssociationManager defaultManager,
      ScopedViewContext context) throws Exception {

    final AssociationDescriptor descriptor =
        descriptorFactory.newDescriptor(node);

    final ToManyAssociationManager manager =
        managerLocator.findManager(ToManyAssociationManager.class,
            defaultManager, descriptor, node, context);

    manager.begin(target);
    findStrategy(manager).update(node, target, values, manager, context);
    manager.end(target);
  }

  private ToManyAssociationUpdateStrategy findStrategy(
      ToManyAssociationManager manager) {
    if (strategies != null) {
      for (final ToManyAssociationUpdateStrategy strategy : strategies) {
        if (strategy.supports(manager)) {
          return strategy;
        }
      }
    }
    throw new AssertionError("no strategy");
  }

}
