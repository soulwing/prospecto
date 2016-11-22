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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyMappedAssociationManager;
import org.soulwing.prospecto.api.listener.ViewMode;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;

/**
 * A {@link ToManyMappedAssociationUpdater} implementation that supports
 * the map-of-objects structure.
 *
 * @author Carl Harris
 */
public class ObjectMapManyMappedAssociationUpdater
    implements ToManyMappedAssociationUpdater {

  public static final ObjectMapManyMappedAssociationUpdater INSTANCE =
      new ObjectMapManyMappedAssociationUpdater();

  private final AssociationDescriptorFactory descriptorFactory;
  private final AssociationManagerLocator managerLocator;

  private ObjectMapManyMappedAssociationUpdater() {
    this(ConcreteAssociationDescriptorFactory.INSTANCE,
        ConcreteAssociationManagerLocator.INSTANCE);
  }

  ObjectMapManyMappedAssociationUpdater(
      AssociationDescriptorFactory descriptorFactory,
      AssociationManagerLocator managerLocator) {
    this.descriptorFactory = descriptorFactory;
    this.managerLocator = managerLocator;
  }

  @Override
  public void findManagerAndUpdate(UpdatableNode node,
      Object target, Map<?, ?> map,
      ToManyMappedAssociationManager defaultManager,
      ScopedViewContext context) throws Exception {

    final AssociationDescriptor descriptor =
        descriptorFactory.newDescriptor(node);

    final ToManyMappedAssociationManager manager =
        managerLocator.findManager(ToManyMappedAssociationManager.class,
            defaultManager, descriptor, node, context);

    updateUsingManager(node, target, map, manager, context);

  }

  @Override
  @SuppressWarnings("unchecked")
  public void updateUsingManager(UpdatableNode node, Object target,
      Map<?, ?> map, ToManyMappedAssociationManager manager,
      ScopedViewContext context) throws Exception {

    manager.begin(target);

    final Map<Object, Object> touched = new IdentityHashMap<>();
    for (final Map.Entry<?, ?> entry : map.entrySet()) {
      final InjectableViewEntity entity = (InjectableViewEntity) entry.getValue();
      final Object element = manager.get(target, entry.getKey());
      if (element != null) {
        touched.put(entry.getKey(), entry.getKey());
        entity.inject(element, context);
      }
      else {
        final Object newElement = manager.newAssociate(target, entity,
            context.getObjectFactories());
        entity.inject(newElement, context);

        context.getListeners().entityCreated(new ViewNodePropertyEvent(
            ViewMode.APPLY, node, target, newElement, context));

        manager.put(target, entry.getKey(), newElement);
        touched.put(entry.getKey(), entry.getKey());
      }
    }

    final Map<?, ?> children = copyModelChildren(target, manager);
    for (final Map.Entry<?, ?> child : children.entrySet()) {
      if (!touched.containsKey(child.getKey())) {
        context.getListeners().entityDiscarded(
            new ViewNodePropertyEvent(ViewMode.APPLY, node,
                target, child, context));
        manager.remove(target, child);
      }
    }

    manager.end(target);
  }

  @SuppressWarnings("unchecked")
  private Map<?, ?> copyModelChildren(Object source,
      ToManyMappedAssociationManager manager) throws Exception {
    final Map<Object, Object> children = new HashMap<>();
    final Iterator<Map.Entry> i = manager.iterator(source);
    while (i.hasNext()) {
      final Map.Entry<?, ?> entry = i.next();
      children.put(entry.getKey(), entry.getValue());
    }
    return children;
  }

}
