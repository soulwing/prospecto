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

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.AssociationManager;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A service that locates an appropriate {@link AssociationManager} for
 * an update operation.
 *
 * @author Carl Harris
 */
interface AssociationManagerLocator {

  /**
   * Finds an association manager.
   * @param <M> manager type
   * @param managerClass manager class
   * @param defaultManager default manager if none found
   * @param descriptor association descriptor
   * @param node subject view node
   * @param context view context
   * @return manager (never {@code null})
   */
  <M extends AssociationManager> M findManager(
      Class<M> managerClass, M defaultManager,
      AssociationDescriptor descriptor, AbstractViewNode node,
      ScopedViewContext context);
}
