/*
 * File created on Mar 29, 2016
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

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.association.AssociationManager;

/**
 * A {@link AssociationManagerService} backed by a {@link LinkedList}.
 *
 * @author Carl Harris
 */
public class LinkedListAssociationManagerService
    implements AssociationManagerService {

  private final List<AssociationManager> managers = new LinkedList<>();

  @Override
  @SuppressWarnings("unchecked")
  public <M extends AssociationManager> M findManager(
      Class<M> managerClass, Class<?> ownerClass, Class<?> elementClass) {
    for (final AssociationManager manager : managers) {
      if (managerClass.isInstance(manager)
          && manager.supports(ownerClass, elementClass)) {
        return (M) manager;
      }
    }
    return null;
  }

  @Override
  public void append(AssociationManager manager) {
    managers.add(manager);
  }

  @Override
  public void prepend(AssociationManager manager) {
    managers.add(0, manager);
  }

  @Override
  public boolean remove(AssociationManager manager) {
    return managers.remove(manager);
  }

  @Override
  public List<AssociationManager> toList() {
    return managers;
  }

}
