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

import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.association.ToManyIndexedAssociationManager;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A strategy for updating an unordered collection.
 *
 * @author Carl Harris
 */
class OrderedToManyAssociationUpdateStrategy
    implements ToManyAssociationUpdateStrategy {

  public static final OrderedToManyAssociationUpdateStrategy INSTANCE =
      new OrderedToManyAssociationUpdateStrategy();

  private OrderedToManyAssociationUpdateStrategy() {}

  @Override
  public boolean supports(ToManyAssociationManager manager) {
    return ToManyIndexedAssociationManager.class.isInstance(manager);
  }

  @Override
  public void update(AbstractViewNode node, Object target,
      List<MutableViewEntity> entities, ToManyAssociationManager manager,
      ScopedViewContext context) throws Exception {

    assert manager instanceof ToManyIndexedAssociationManager;
    doUpdate(node, target, entities, (ToManyIndexedAssociationManager)
        manager, context);
  }

  @SuppressWarnings("unchecked")
  private void doUpdate(ViewNode node, Object target,
      List<MutableViewEntity> entities, ToManyIndexedAssociationManager manager,
      ScopedViewContext context) throws Exception {
    int viewIndex = 0;
    for (final MutableViewEntity entity : entities) {
      final int modelIndex = manager.indexOf(target, entity);
      if (modelIndex != -1) {
        final Object element = manager.get(target, modelIndex);
        if (modelIndex >= viewIndex) {
          entity.inject(element, context);
          if (modelIndex > viewIndex) {
            manager.remove(target, modelIndex);
            manager.add(target, viewIndex, element);
          }
        }
      }
      else {
        final Object newElement = manager.newAssociate(target, entity);

        context.getListeners().entityCreated(new ViewNodePropertyEvent(
            ViewNodeEvent.Mode.MODEL_UPDATE, node, target, newElement, context));

        manager.add(target, viewIndex, newElement);
      }
      viewIndex++;
    }

    final int count = manager.size(target) - viewIndex;
    for (int i = 0; i < count; i++) {
      final Object element = manager.get(target, viewIndex);
      context.getListeners().entityDiscarded(
          new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, node,
              target, element, context));
      manager.remove(target, viewIndex);
    }

  }

}
