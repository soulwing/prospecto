/*
 * File created on Apr 28, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import org.soulwing.prospecto.api.association.ToManyAssociationManager;

/**
 * A object that holds a collection/array to be updated and the manager that
 * will be used to perform the update.
 *
 * @author Carl Harris
 */
class TargetAndManager {

  private final Object target;
  private final ToManyAssociationManager<?, ?> manager;

  public TargetAndManager(Object target,
      ToManyAssociationManager<?, ?> manager) {
    this.target = target;
    this.manager = manager;
  }

  public Object getTarget() {
    return target;
  }

  public ToManyAssociationManager<?, ?> getManager() {
    return manager;
  }

}
