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

import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.listener.ViewMode;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;

/**
 * A strategy for updating a to-many association (unordered)
 *
 * @author Carl Harris
 */
class UnorderedToManyAssociationUpdateStrategy
    implements ToManyAssociationUpdateStrategy {

  public static final UnorderedToManyAssociationUpdateStrategy INSTANCE =
      new UnorderedToManyAssociationUpdateStrategy();

  private UnorderedToManyAssociationUpdateStrategy() {}

  @Override
  public boolean supports(ToManyAssociationManager manager) {
    return ToManyAssociationManager.class.isInstance(manager);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void update(UpdatableNode node, Object target,
      Iterable<?> values, ToManyAssociationManager manager,
      ScopedViewContext context)
      throws Exception {

    final Map<Object, Object> touched = new IdentityHashMap<>();
    for (final Object value : values)  {
      final InjectableViewEntity entity = (InjectableViewEntity) value;
      final Object element = manager.findAssociate(target, entity,
          context.getObjectFactories());
      if (element != null) {
        touched.put(element, element);
        entity.inject(element, context);
      }
      else {
        final Object newElement = manager.newAssociate(target, entity,
            context.getObjectFactories());
        entity.inject(newElement, context);

        context.getListeners().entityCreated(new ViewNodePropertyEvent(
            ViewMode.APPLY, node, target, newElement, context));

        manager.add(target, newElement);
        touched.put(newElement, newElement);
      }
    }

    final List<Object> children = copyModelChildren(target, manager);
    for (final Object child : children) {
      if (!touched.containsKey(child)) {
        context.getListeners().entityDiscarded(
            new ViewNodePropertyEvent(ViewMode.APPLY, node,
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
