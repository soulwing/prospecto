/*
 * File created on Mar 31, 2016
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

import org.soulwing.prospecto.api.association.ToOneAssociationManager;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * An updater for a to-one association between a target model object and
 * one of its properties.
 *
 * @author Carl Harris
 */
public class ConcreteToOneAssociationUpdater
    implements ToOneAssociationUpdater {

  private final AssociationManagerLocator managerLocator;

  public ConcreteToOneAssociationUpdater() {
    this(new ConcreteAssociationManagerLocator());
  }

  ConcreteToOneAssociationUpdater(AssociationManagerLocator managerLocator) {
    this.managerLocator = managerLocator;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void update(AbstractViewNode node, Object target,
      MutableViewEntity entity,
      ToOneAssociationManager defaultManager, ScopedViewContext context)
      throws Exception {

    final ToOneAssociationManager manager = managerLocator.findManager(
            ToOneAssociationManager.class, defaultManager, node, context);

    final Object currentAssociate = manager.get(target);

    if (manager.isSameAssociate(target, entity)) {
      if (currentAssociate != null) {
        entity.inject(currentAssociate, context);
      }
    }
    else {
      if (currentAssociate != null) {
        context.getListeners().entityDiscarded(
            new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, node,
                target, currentAssociate, context));
      }
      if (entity != null) {
        Object newAssociate = newAssociate(target, entity, manager, context);
        entity.inject(newAssociate, context);
        context.getListeners().entityCreated(
            new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, node,
                target, newAssociate, context));
        manager.set(target, newAssociate);
      }
      else {
        manager.set(target, null);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static Object newAssociate(Object owner,
      MutableViewEntity associateEntity, ToOneAssociationManager manager,
      ScopedViewContext context) throws Exception {

    Object newAssociate = manager.newAssociate(owner, associateEntity);

    if (newAssociate == null) {
      newAssociate = associateEntity.getType().newInstance();
      associateEntity.inject(newAssociate, context);
    }

    return newAssociate;
  }

}
