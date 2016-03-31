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

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A strategy for updating a to-many association (unordered)
 *
 * @author Carl Harris
 */
public class UnorderedToManyAssociationUpdateStrategy
    implements ToManyAssociationUpdateStrategy {

  public static final UnorderedToManyAssociationUpdateStrategy INSTANCE =
      new UnorderedToManyAssociationUpdateStrategy();

  private UnorderedToManyAssociationUpdateStrategy() {}

  @Override
  public boolean supports(ToManyAssociationManager manager) {
    return ToManyAssociationManager.class.isInstance(manager);
  }

  @Override
  public void update(ViewNode node, Object target,
      List<MutableViewEntity> entities, ToManyAssociationManager manager,
      ScopedViewContext context) throws Exception {
    assert manager instanceof ToManyAssociationManager;
    doUpdate(node, target, entities, (ToManyAssociationManager) manager,
        context);
  }

  @SuppressWarnings("unchecked")
  private void doUpdate(ViewNode node, Object target,
      List<MutableViewEntity> entities, ToManyAssociationManager manager,
      ScopedViewContext context) throws Exception {

    final Map<Object, Object> touched = new IdentityHashMap<>();
    for (final MutableViewEntity entity : entities)  {
      final Object element = manager.find(target, entity);
      if (element != null) {
        touched.put(element, element);
        entity.inject(element, context);
      }
      else {
        Object newElement = CollectionElementFactory.newElement(
            target, entity, manager);

        context.getListeners().entityCreated(new ViewNodePropertyEvent(
            ViewNodeEvent.Mode.MODEL_UPDATE, node, target, newElement, context));

        manager.add(target, newElement);
        touched.put(newElement, newElement);
      }
    }

    final List<Object> children = copyModelChildren(target, manager);
    for (final Object child : children) {
      if (!touched.containsKey(child)) {
        context.getListeners().entityDiscarded(
            new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, node,
                target, child, context));
        manager.remove(target, child);
      }
    }

  }

  @SuppressWarnings("unchecked")
  private List<Object> copyModelChildren(Object source,
      ToManyAssociationManager manager) throws Exception {
    final List<Object> children = new LinkedList<>();
    final Iterator<Object> i = manager.iterator(source);
    while (i.hasNext()) {
      children.add(i.next());
    }
    return children;
  }

}
