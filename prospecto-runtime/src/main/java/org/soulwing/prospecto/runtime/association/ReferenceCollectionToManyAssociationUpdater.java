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

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;

/**
 * A {@link ToManyAssociationUpdater} for a collection of references.

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
  public void findManagerAndUpdate(UpdatableNode node, Object target,
      Iterable<?> values,
      ToManyAssociationManager defaultManager,
      ScopedViewContext context) throws Exception {

    final AssociationDescriptor descriptor =
        descriptorFactory.newDescriptor(node);

    final ToManyAssociationManager manager =
        managerLocator.findManager(ToManyAssociationManager.class,
            defaultManager, descriptor, node, context);

    updateUsingManager(node, target, values, manager, context);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void updateUsingManager(UpdatableNode node, Object target,
      Iterable<?> values, ToManyAssociationManager manager,
      ScopedViewContext context) throws Exception {

    manager.begin(target);
    final Map<Object, Object> touched = new IdentityHashMap<>();
    for (final Object value : values)  {
      final InjectableViewEntity entity = (InjectableViewEntity) value;
      final Object element = manager.findAssociate(target, entity,
          context.getObjectFactories());
      if (element != null) {
        touched.put(element, element);
      }
      else {
        final Object newElement = context.getReferenceResolvers()
            .resolve(entity.getType(), entity);

        manager.add(target, newElement);
        touched.put(newElement, newElement);
      }
    }

    for (final Object child : copyModelChildren(target, manager)) {
      if (!touched.containsKey(child)) {
        manager.remove(target, child);
      }
    }

    manager.end(target);
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
