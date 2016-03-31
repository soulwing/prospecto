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

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.ConcreteMutableViewEntity;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A view node that contains other view nodes.
 *
 * @author Carl Harris
 */
public abstract class ContainerViewNode extends AbstractViewNode
    implements UpdatableViewNode {

  private final List<AbstractViewNode> children;
  private final DiscriminatorEventFactory discriminatorEventFactory;

  private Accessor accessor;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   */
  protected ContainerViewNode(String name, String namespace,
      Class<?> modelType) {
    this(name, namespace, modelType, new ArrayList<AbstractViewNode>());
  }

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   * @param children node children
   */
  protected ContainerViewNode(String name, String namespace, Class<?> modelType,
      List<AbstractViewNode> children) {
    this(name, namespace, modelType, children,
        new ConcreteDiscriminatorEventFactory());
  }

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   * @param children node children
   * @param discriminatorEventFactory discriminator strategy locator
   */
  protected ContainerViewNode(String name, String namespace, Class<?> modelType,
      List<AbstractViewNode> children,
      DiscriminatorEventFactory discriminatorEventFactory) {
    super(name, namespace, modelType);
    this.children = children;
    this.discriminatorEventFactory = discriminatorEventFactory;
  }

  @Override
  public Accessor getAccessor() {
    return accessor;
  }

  @Override
  public void setAccessor(Accessor accessor) {
    this.accessor = accessor;
  }

  public List<AbstractViewNode> getChildren() {
    return children;
  }

  public AbstractViewNode getChild(Class<?> modelType, String name) {
    Iterator<AbstractViewNode> i = children.iterator();
    while (i.hasNext()) {
      final AbstractViewNode child = i.next();
      if (child instanceof SubtypeNode
          && modelType.isAssignableFrom(((SubtypeNode) child).getModelType())) {
        final AbstractViewNode node =
            ((SubtypeNode) child).getChild(modelType, name);
        if (node != null) return node;
      }
    }

    i = children.iterator();
    while (i.hasNext()) {
      final AbstractViewNode child = i.next();
      if (name.equals(child.getName())) {
        return child;
      }
    }

    return null;
  }

  public void addChild(AbstractViewNode child) {
    children.add(child);
    child.setParent(this);
  }

  public void addChildren(List<AbstractViewNode> children) {
    for (AbstractViewNode child : children) {
      addChild(child);
    }
  }

  protected final List<View.Event> evaluateChildren(Object model,
      ScopedViewContext context) throws Exception {
    context.put(model);
    final List<View.Event> events = new LinkedList<>();
    if (discriminatorEventFactory.isDiscriminatorNeeded(this)) {
      events.add(discriminatorEventFactory.newDiscriminatorEvent(this,
          model.getClass(), context));
    }
    for (AbstractViewNode child : getChildren()) {
      events.addAll(child.evaluate(model, context));
    }
    context.remove(model);
    return events;
  }

  @Override
  public Object toModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
    context.push(getName(), getModelType());
    final MutableViewEntity entity = newEntity(events, context);
    final ViewNodeEvent nodeEvent = new ViewNodeEvent(
        ViewNodeEvent.Mode.MODEL_UPDATE, this, entity, context);
    if (!context.getListeners().shouldVisitNode(nodeEvent)) {
      return UndefinedValue.INSTANCE;
    }
    final View.Event.Type endEventType = triggerEvent.getType().complement();
    View.Event event = events.removeFirst();
    while (event != null && event.getType() != endEventType) {
      if (event.getType() != View.Event.Type.DISCRIMINATOR
          && event.getType() != View.Event.Type.URL) {
        final String name = event.getName();
        if (name == null) {
          throw new ModelEditorException("unexpected un-named event: " + event);
        }
        final AbstractViewNode child = getChild(entity.getType(), name);
        if (child == null) {
          // TODO -- configuration should allow it to be ignored
          throw new ModelEditorException("found no child named '" + name + "'"
              + " in node " + this.getName());
        }
        if (child instanceof UpdatableViewNode) {
          if (child instanceof ContainerViewNode
              && event.getType() == View.Event.Type.VALUE) {
            if (event.getValue() != null) {
              throw new ModelEditorException(
                  "scalar value for object node must be null");
            }
            entity.put(name, null, (UpdatableViewNode) child);
          }
          else {
            final Object value = ((UpdatableViewNode) child)
                .toModelValue(entity, event, events, context);
            if (value != UndefinedValue.INSTANCE) {
              entity.put(name, value, (UpdatableViewNode) child);
            }
          }
        }
      }
      event = events.removeFirst();
    }

    final Object valueToInject = context.getListeners().willInjectValue(
         new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, this,
            parentEntity, entity, context));

    context.getListeners().propertyVisited(
        new ViewNodePropertyEvent(ViewNodeEvent.Mode.MODEL_UPDATE, this,
        parentEntity, valueToInject, context));

    context.getListeners().nodeVisited(nodeEvent);

    context.pop();

    return valueToInject;
  }

  private MutableViewEntity newEntity(Deque<View.Event> events,
      ScopedViewContext context) throws Exception {
    View.Event event = findDiscriminatorEvent(events.iterator());
    if (event == null) {
      return new ConcreteMutableViewEntity(getModelType());
    }

    final Discriminator discriminator = new Discriminator(event.getName(),
        event.getValue());

    final DiscriminatorStrategy strategy =
        discriminatorEventFactory.getStrategyLocator()
            .findStrategy(this, context);

    return new ConcreteMutableViewEntity(
        strategy.toSubtype(getModelType(), discriminator));
  }

  private View.Event findDiscriminatorEvent(Iterator<View.Event> i) {
    // TODO:
    // Making a discriminator (if present) the first child of a structural node
    // in a view should perhaps be a responsibility of the ViewReader. This
    // would avoid having to look potentially very far ahead in the event stream.
    View.Event event = null;
    while (i.hasNext()) {
      event = i.next();
      if (View.Event.Type.DISCRIMINATOR == event.getType()) break;
      skipEvent(event, i);
    }
    if (event == null) return null;
    if (event.getType() != View.Event.Type.DISCRIMINATOR) return null;
    return event;
  }

  private static void skipEvent(View.Event event,
      Iterator<View.Event> i) {
    final View.Event.Type complementType = event.getType().complement();
    while (i.hasNext() && event.getType() != complementType) {
      event = i.next();
    }
  }

}
