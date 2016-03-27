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

  public static final String DISCRIMINATOR_FLAG_KEY = "hasDiscriminator";

  private final List<AbstractViewNode> children;

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType element model type
   */
  protected ContainerViewNode(String name, String namespace, Class<?> modelType) {
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
    super(name, namespace, modelType);
    this.children = children;
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


    if (get(DISCRIMINATOR_FLAG_KEY, Boolean.class) == Boolean.TRUE) {
      final Discriminator discriminator =
          getDiscriminator(model.getClass(), context);
      assert discriminator != null;
      events.add(newEvent(View.Event.Type.DISCRIMINATOR,
          discriminator.getName(), discriminator.getValue()));
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
    final ViewNodeEvent nodeEvent = new ViewNodeEvent(this, entity, context);
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
          throw new ModelEditorException("found no child named '" + name + "'");
        }
        if (child instanceof UpdatableViewNode) {
          final Object value = ((UpdatableViewNode) child)
              .toModelValue(entity, event, events, context);
          if (value != UndefinedValue.INSTANCE) {
            entity.put(name, value, (UpdatableViewNode) child);
          }
        }
      }
      event = events.removeFirst();
    }

    final Object valueToInject = context.getListeners().willInjectValue(
         new ViewNodePropertyEvent(this, parentEntity, entity, context));

    context.getListeners().propertyVisited(
        new ViewNodePropertyEvent(this, parentEntity, valueToInject, context));

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
    return new ConcreteMutableViewEntity(getDiscriminatorStrategy(context)
        .toSubtype(getModelType(), discriminator));
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

  private Discriminator getDiscriminator(Class<?> subtype,
      ScopedViewContext context) {
    DiscriminatorStrategy strategy = getDiscriminatorStrategy(context);
    return strategy.toDiscriminator(getModelType(), subtype);
  }

  private DiscriminatorStrategy getDiscriminatorStrategy(
      ScopedViewContext context) {
    DiscriminatorStrategy strategy = get(DiscriminatorStrategy.class);
    if (strategy == null) {
      strategy = context.get(DiscriminatorStrategy.class);
    }
    if (strategy == null) {
      throw new AssertionError("discriminator strategy is required");
    }
    return strategy;
  }

}
