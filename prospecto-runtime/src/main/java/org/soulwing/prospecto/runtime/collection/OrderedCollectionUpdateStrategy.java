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
package org.soulwing.prospecto.runtime.collection;

import java.util.List;

import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.collection.CollectionManager;
import org.soulwing.prospecto.api.collection.ListManager;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A strategy for updating an unordered collection.
 *
 * @author Carl Harris
 */
public class OrderedCollectionUpdateStrategy
    implements CollectionUpdateStrategy {

  public static final OrderedCollectionUpdateStrategy INSTANCE =
      new OrderedCollectionUpdateStrategy();

  private OrderedCollectionUpdateStrategy() {}

  @Override
  public boolean supports(CollectionManager manager) {
    return ListManager.class.isInstance(manager);
  }

  @Override
  public void update(ViewNode node, Object target,
      List<MutableViewEntity> entities, CollectionManager manager,
          ScopedViewContext context) throws Exception {
    assert manager instanceof ListManager;
    doUpdate(node, target, entities, (ListManager) manager, context);
  }

  @SuppressWarnings("unchecked")
  private void doUpdate(ViewNode node, Object target,
      List<MutableViewEntity> entities, ListManager manager,
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
        final Object newElement = CollectionElementFactory.newElement(
            target, entity, manager);

        context.getListeners().entityCreated(new ViewNodePropertyEvent(
            MODE, node, target, newElement, context));

        manager.add(target, viewIndex, newElement);
      }
      viewIndex++;
    }

    final int count = manager.size(target) - viewIndex;
    for (int i = 0; i < count; i++) {
      final Object element = manager.get(target, viewIndex);
      context.getListeners().entityDiscarded(
          new ViewNodePropertyEvent(MODE, node, target, element, context));
      manager.remove(target, viewIndex);
    }

  }

}
