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

import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * An updater for a to-many association between a target model object and
 * one of its properties.
 *
 * @author Carl Harris
 */
public interface ToManyAssociationUpdater {

  void update(UpdatableNode node, Object target,
      Iterable<?> values, ToManyAssociationManager defaultManager,
      ScopedViewContext context) throws Exception;

}
