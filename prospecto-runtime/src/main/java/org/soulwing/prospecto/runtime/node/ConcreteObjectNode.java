/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.runtime.node;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.node.ObjectNode;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.association.ConcreteToOneAssociationUpdater;
import org.soulwing.prospecto.runtime.association.ToOneAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A view node that represents an object.
 *
 * @author Carl Harris
 */
public class ConcreteObjectNode extends ConcreteContainerNode
    implements UpdatableViewNode, ObjectNode {

  private final ToOneAssociationUpdater associationUpdater;
  private final UpdatableViewNodeTemplate template;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type associated with node
   */
  public ConcreteObjectNode(String name, String namespace, Class<?> modelType) {
    this(name, namespace, modelType, new ConcreteToOneAssociationUpdater(),
        ConcreteUpdatableViewNodeTemplate.INSTANCE);
  }

  ConcreteObjectNode(String name, String namespace, Class<?> modelType,
      ToOneAssociationUpdater associationUpdater,
      UpdatableViewNodeTemplate template) {
    super(name, namespace, modelType);
    this.associationUpdater = associationUpdater;
    this.template = template;
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitObject(this, state);
  }

  @Override
  protected List<View.Event> onEvaluate(Object source,
      ScopedViewContext context) throws Exception {
    final List<View.Event> events = new LinkedList<>();
    final Object model = getAccessor().get(source);
    if (model == UndefinedValue.INSTANCE) return Collections.emptyList();

    if (model != null) {
      events.add(newEvent(View.Event.Type.BEGIN_OBJECT));
      events.addAll(evaluateChildren(model, context));
      events.add(newEvent(View.Event.Type.END_OBJECT));
    }
    else {
      events.add(newEvent(View.Event.Type.VALUE));
    }
    return events;
  }

  @Override
  public Object toModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
    return template.toModelValue(this, parentEntity, context,
        new ObjectNodeUpdateMethod(this, triggerEvent, events, context));
  }

  @Override
  public void inject(Object target, Object value) throws Exception {
    final MutableViewEntity entity = (MutableViewEntity) value;
    for (final String name : entity.nameSet()) {
      final ViewNode child = getChild(entity.getType(), name);
      if (child instanceof ConcreteValueNode) {
        ((ConcreteValueNode) child).inject(target, entity.get(name));
      }
    }
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    associationUpdater.update(this, target,
        (MutableViewEntity) value, getAccessor(), context);
  }

}
