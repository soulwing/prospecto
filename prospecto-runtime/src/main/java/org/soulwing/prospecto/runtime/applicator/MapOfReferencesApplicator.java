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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.template.MapOfReferencesNode;
import org.soulwing.prospecto.runtime.association.ReferenceMapToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.ConcreteViewEntityFactory;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.entity.ViewEntityFactory;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;
import org.soulwing.prospecto.runtime.reference.ReferenceResolverService;

/**
 * An applicator for an {@link MapOfReferencesNode}.
 *
 * @author Carl Harris
 */
class MapOfReferencesApplicator
    extends AbstractMapOfObjectsApplicator<MapOfReferencesNode>
    implements RootViewEventApplicator {

  MapOfReferencesApplicator(MapOfReferencesNode node,
      List<ViewEventApplicator> children) {
    this(node, children,
        ConcreteViewEntityFactory.INSTANCE,
        ConcreteTransformationService.INSTANCE,
        ReferenceMapToManyAssociationUpdater.INSTANCE,
        HierarchicalContainerApplicatorLocator.INSTANCE);
  }

  MapOfReferencesApplicator(MapOfReferencesNode node,
      List<ViewEventApplicator> children, ViewEntityFactory entityFactory,
      TransformationService transformationService,
      ToManyAssociationUpdater associationUpdater,
      ContainerApplicatorLocator applicatorLocator) {
    super(node, children, entityFactory, transformationService,
        associationUpdater, applicatorLocator);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object apply(Object injector, Object target, ScopedViewContext context)
      throws Exception {
    final Map<Object, InjectableViewEntity> entities =
        (Map<Object, InjectableViewEntity>) injector;

    final ReferenceResolverService resolvers = context.getReferenceResolvers();
    final Map<Object, Object> references = new LinkedHashMap<>();

    for (final Map.Entry<Object,  InjectableViewEntity> entity : entities.entrySet()) {
      references.put(entity.getKey(), entity.getValue() != null ?
          resolvers.resolve(entity.getValue().getType(), entity.getValue()) : null);
    }

    return references;
  }

}
