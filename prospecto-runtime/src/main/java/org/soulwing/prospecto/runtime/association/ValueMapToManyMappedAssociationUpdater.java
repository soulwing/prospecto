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

import java.util.Map;

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyMappedAssociationManager;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A {@link ToManyMappedAssociationUpdater} for a map of values.
 *
 * @author Carl Harris
 */
public class ValueMapToManyMappedAssociationUpdater
    implements ToManyMappedAssociationUpdater {

  public static final ValueMapToManyMappedAssociationUpdater INSTANCE =
      new ValueMapToManyMappedAssociationUpdater();

  private final AssociationDescriptorFactory descriptorFactory;
  private final AssociationManagerLocator managerLocator;

  ValueMapToManyMappedAssociationUpdater() {
    this(ConcreteAssociationDescriptorFactory.INSTANCE,
        ConcreteAssociationManagerLocator.INSTANCE);
  }

  ValueMapToManyMappedAssociationUpdater(
      AssociationDescriptorFactory descriptorFactory,
      AssociationManagerLocator managerLocator) {
    this.descriptorFactory = descriptorFactory;
    this.managerLocator = managerLocator;
  }

  @Override
  public void findManagerAndUpdate(UpdatableNode node, Object target,
      Map<?, ?> values,
      ToManyMappedAssociationManager defaultManager,
      ScopedViewContext context) throws Exception {

    final AssociationDescriptor descriptor =
        descriptorFactory.newDescriptor(node);

    final ToManyMappedAssociationManager manager =
        managerLocator.findManager(ToManyMappedAssociationManager.class,
            defaultManager, descriptor, node, context);

    updateUsingManager(node, target, values, manager, context);
  }

  @Override
  public void updateUsingManager(UpdatableNode node, Object target,
      Map<?, ?> values, ToManyMappedAssociationManager manager,
      ScopedViewContext context) throws Exception {
    doUpdate(target, values, manager, context);
  }

  @SuppressWarnings("unchecked")
  private void doUpdate(Object target, Map<?, ?> values,
      ToManyMappedAssociationManager manager, ScopedViewContext context)
      throws Exception {
    manager.begin(target);
    manager.clear(target);
    for (final Map.Entry entry : values.entrySet()) {
      manager.put(target, entry.getKey(), resolve(entry.getValue(), context));
    }
    manager.end(target);
  }

  @SuppressWarnings("unused")
  protected Object resolve(Object value, ScopedViewContext context) {
    return value;
  }

}
