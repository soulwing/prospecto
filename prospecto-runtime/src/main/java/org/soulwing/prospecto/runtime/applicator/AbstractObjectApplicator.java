/*
 * File created on Apr 7, 2016
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

import java.util.List;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.template.ObjectNode;
import org.soulwing.prospecto.runtime.association.ToOneAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.entity.ViewEntityFactory;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for {@link ObjectApplicator} implementations.
 *
 * @author Carl Harris
 */
abstract class AbstractObjectApplicator<N extends ObjectNode>
    extends AbstractContainerApplicator<N> {

  final ToOneAssociationUpdater associationUpdater;

  AbstractObjectApplicator(N node, List<ViewEventApplicator> children,
      ViewEntityFactory entityFactory,
      TransformationService transformationService,
      ToOneAssociationUpdater associationUpdater,
      ContainerApplicatorLocator applicatorLocator) {
    super(node, children, entityFactory, transformationService,
        applicatorLocator);
    this.associationUpdater = associationUpdater;
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    if (node.getAllowedModes().contains(AccessMode.WRITE)) {
      associationUpdater.update(node, target,
          (InjectableViewEntity) value, node.getDefaultManager(), context);
    }
  }

}
