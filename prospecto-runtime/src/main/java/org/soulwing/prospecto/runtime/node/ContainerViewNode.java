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
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

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
    AbstractViewNode node = null;
    Iterator<AbstractViewNode> i = children.iterator();
    while (node == null && i.hasNext()) {
      final AbstractViewNode child = i.next();
      if (child instanceof SubtypeNode
          && modelType.isAssignableFrom(((SubtypeNode) child).getModelType())) {
        node = ((SubtypeNode) child).getChild(modelType, name);
      }
    }

    i = children.iterator();
    while (node == null && i.hasNext()) {
      final AbstractViewNode child = i.next();
      if (name.equals(child.getName())) {
        node = child;
      }
    }

    return node;
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

  protected final void updateChildren(Object target, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
    context.put(target);
    final View.Event.Type endEventType = triggerEvent.getType().complement();
    View.Event event = events.removeFirst();
    while (event.getType() != endEventType) {
      if (event.getType() == View.Event.Type.DISCRIMINATOR) {
        event = events.removeFirst();
        continue;
      }

      final String name = event.getName();
      final AbstractViewNode node = getChild(target.getClass(), name);
      if (node == null) {
        // FIXME: configuration should allow us to ignore it also
        throw new ModelEditorException("unrecognized child name '" + name
            + "' in event: " + event);
      }

      // Special case: if the node is a container and we have a VALUE type
      // event with a null value, we treat this as an indication to remove the
      // subtree from the model.
      if (node instanceof ContainerViewNode
          && View.Event.Type.VALUE.equals(event.getType())) {
        if (event.getValue() != null) {
          throw new ModelEditorException("expected an object, not a value");
        }
        ((ContainerViewNode) node).clear(target);
      }
      else {
        node.update(target, event, events, context);
      }

      event = events.removeFirst();
    }
    context.remove(target);
  }

  protected void clear(Object target) throws Exception {
    // FIXME -- need to tell some handler we're removing it
    getAccessor().set(target, null);
  }

  protected <T> T createChild(Class<T> modelType, Deque<View.Event> events,
      ScopedViewContext context) throws Exception {
    final T model = newChild(modelType, events.iterator(), context);
    setChildValues(model, events.iterator(), context);
    return model;
  }

  private <T> T newChild(Class<T> modelType, Iterator<View.Event> i,
      ScopedViewContext context) throws Exception {
    final Class<T> subtype = getModelSubtype(modelType, i, context);
    try {
      return subtype.newInstance();
    }
    catch (InstantiationException | IllegalAccessException ex) {
      throw new ModelEditorException("cannot create an instance of type "
          + subtype.getName());
    }
  }

  private <T> void setChildValues(T model,
      Iterator<View.Event> i, ScopedViewContext context) throws Exception {
    while (i.hasNext()) {
      final View.Event event = i.next();
      if (View.Event.Type.END_OBJECT.equals(event.getType())) {
        break;
      }
      else if (View.Event.Type.VALUE.equals(event.getType())) {
        final AbstractViewNode node = getChild(model.getClass(), event.getName());
        if (node == null) {
          // FIXME
          throw new ModelEditorException("can't find node named "
              + event.getName());
        }
        if (node instanceof ValueViewNode) {
          node.update(model, event, null, context);
        }
      }
      else {
        skipEvent(event, i);
      }
    }
  }

  private <T> Class<T> getModelSubtype(Class<T> modelType,
      Iterator<View.Event> i,
      ScopedViewContext context) throws Exception {

    View.Event event = findDiscriminatorEvent(i);
    if (event == null) return modelType;

    final DiscriminatorStrategy strategy = getStrategy(context);
    return strategy == null ?
        modelType : strategy.toSubtype(modelType,
            new Discriminator(event.getName(), event.getValue()));
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
    DiscriminatorStrategy strategy = getStrategy(context);
    if (strategy == null) return null;
    return strategy.toDiscriminator(getModelType(), subtype);
  }

  private DiscriminatorStrategy getStrategy(ScopedViewContext context) {
    DiscriminatorStrategy strategy = get(DiscriminatorStrategy.class);
    if (strategy == null) {
      strategy = context.get(DiscriminatorStrategy.class);
    }
    return strategy;
  }

}
