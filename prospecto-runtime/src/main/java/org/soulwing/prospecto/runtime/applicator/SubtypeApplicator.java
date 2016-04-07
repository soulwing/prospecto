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

import java.util.Deque;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.node.SubtypeNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.ConcreteViewEntityFactory;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;

/**
 * An applicator for a subtype node.
 *
 * @author Carl Harris
 */
class SubtypeApplicator extends AbstractContainerApplicator<SubtypeNode> {

  SubtypeApplicator(SubtypeNode node, List<ViewEventApplicator> children) {
    super(node, children, ConcreteViewEntityFactory.INSTANCE, ConcreteTransformationService.INSTANCE);
  }

  @Override
  Object onToModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public void inject(Object target, Object value) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    throw new UnsupportedOperationException();
  }

}
