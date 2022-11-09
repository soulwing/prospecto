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
import javax.json.JsonValue;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.template.UpdatableValueNode;
import org.soulwing.prospecto.api.template.ValueNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.listener.ConcreteTransformationService;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An applicator for a value node.
 *
 * @author Carl Harris
 */
class ValueApplicator extends AbstractViewEventApplicator<ValueNode>
    implements InjectableViewEntity.ValueInjector {

  private final TransformationService transformationService;

  ValueApplicator(ValueNode node) {
    this(node, ConcreteTransformationService.INSTANCE);
  }

  ValueApplicator(ValueNode node,
      TransformationService transformationService) {
    super(node);
    this.transformationService = transformationService;
  }

  @Override
  Object onToModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {

    final Object value = consumeValue(triggerEvent, events);
    return transformationService.valueToInject(parentEntity, node.getDataType(),
          value, node, context);
  }

  private Object consumeValue(View.Event triggerEvent,
      Deque<View.Event> events) {
    if (JsonValue.class.isAssignableFrom(node.getDataType())) {
      return JsonValueApplicatorSupport.INSTANCE
          .consumeValue(triggerEvent, events);
    }
    return ValueApplicatorSupport.INSTANCE.consumeValue(triggerEvent, events);
  }

  @Override
  public void inject(Object target, Object value) throws Exception {
    if (!(node instanceof UpdatableValueNode)) return;
    if (((UpdatableValueNode) node).getAllowedModes().contains(AccessMode.WRITE)) {
      ((UpdatableValueNode) node).setValue(target, value);
    }
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    inject(target, value);
  }

}
