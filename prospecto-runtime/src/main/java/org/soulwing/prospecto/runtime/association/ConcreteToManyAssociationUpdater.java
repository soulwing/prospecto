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

import java.util.List;

import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A {@link ToOneAssociationUpdater} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteToManyAssociationUpdater implements ToManyAssociationUpdater {

  private static final ToManyAssociationUpdateStrategy[] strategies = {
      OrderedToManyAssociationUpdateStrategy.INSTANCE,
      UnorderedToManyAssociationUpdateStrategy.INSTANCE
  };

  private final AssociationManagerLocator managerLocator;

  public ConcreteToManyAssociationUpdater() {
    this(new ConcreteAssociationManagerLocator());
  }

  ConcreteToManyAssociationUpdater(AssociationManagerLocator managerLocator) {
    this.managerLocator = managerLocator;
  }

  @Override
  public void update(AbstractViewNode node, Object target,
      List<MutableViewEntity> entities, ToManyAssociationManager defaultManager,
      ScopedViewContext context) throws Exception {
    final ToManyAssociationManager manager =
        managerLocator.findManager(ToManyAssociationManager.class,
            defaultManager, node, context);
    findStrategy(manager).update(node, target, entities, defaultManager, context);
  }

  private static ToManyAssociationUpdateStrategy findStrategy(
      ToManyAssociationManager manager) {
    for (final ToManyAssociationUpdateStrategy strategy : strategies) {
      if (strategy.supports(manager)) {
        return strategy;
      }
    }
    throw new AssertionError("no strategy");
  }

}
