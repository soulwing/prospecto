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

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.association.ToManyKeyedAssociationManager;
import org.soulwing.prospecto.api.listener.ViewMode;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;

/**
 * A strategy for updating an unordered collection.
 *
 * @author Carl Harris
 */
class KeyedToManyAssociationUpdateStrategy
    implements ToManyAssociationUpdateStrategy {

  public static final KeyedToManyAssociationUpdateStrategy INSTANCE =
      new KeyedToManyAssociationUpdateStrategy();

  private KeyedToManyAssociationUpdateStrategy() {}

  @Override
  public boolean supports(ToManyAssociationManager manager) {
    return ToManyKeyedAssociationManager.class.isInstance(manager);
  }

  @Override
  public void update(UpdatableNode node, Object target,
      Iterable<?> values, ToManyAssociationManager manager,
      ScopedViewContext context) throws Exception {

    assert manager instanceof ToManyKeyedAssociationManager;
    doUpdate(node, target, values, (ToManyKeyedAssociationManager)
        manager, context);
  }

  @SuppressWarnings("unchecked")
  private void doUpdate(UpdatableNode node, Object target,
      Iterable<?> values, ToManyKeyedAssociationManager manager,
      ScopedViewContext context) throws Exception {

    final Set<Object> entityKeys = new LinkedHashSet<>();
    for (final Object value : values) {
      final Map.Entry<Object, InjectableViewEntity> entry = (Map.Entry<Object, InjectableViewEntity>) value;
      entityKeys.add(entry.getKey());
      final Object newElement = manager.newAssociate(target, entry.getValue(),
          context.getObjectFactories());
      entry.getValue().inject(newElement, context);
      final Map.Entry<Object, Object> newEntry =
          new AbstractMap.SimpleEntry<>(entry.getKey(), newElement);
      context.getListeners().entityCreated(new ViewNodePropertyEvent(
          ViewMode.APPLY, node, target, newEntry, context));
      manager.add(target, newEntry);
    }

    final Iterator<Map.Entry<?, ?>> it = manager.iterator(target);
    while (it.hasNext()) {
      final Map.Entry<?, ?> entry = it.next();
      if (!entityKeys.contains(entry.getKey())) {
        context.getListeners().entityDiscarded(
            new ViewNodePropertyEvent(ViewMode.APPLY, node,
                target, entry.getValue(), context));
        manager.remove(target, entry);
      }
    }

  }

}
