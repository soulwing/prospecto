/*
 * File created on Mar 10, 2016
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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.scope.MutableScope;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.scope.ConcreteMutableScope;

/**
 * An abstract base for {@link ViewNode} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractViewNode implements ViewNode, MutableScope {

  private final MutableScope scope = new ConcreteMutableScope();

  private final String name;
  private final String namespace;
  private final Class<?> modelType;

  private AbstractViewNode parent;
  private Accessor accessor;

  /**
   * Constructs a new instance.
   * @param name node name (may be {@code null})
   * @param namespace namespace (may be {@code null})
   * @param modelType associated model type (may be {@code null})
   */
  protected AbstractViewNode(String name,
      String namespace, Class<?> modelType) {
    this.name = name;
    this.namespace = namespace;
    this.modelType = modelType;
  }

  public AbstractViewNode getParent() {
    return parent;
  }

  public void setParent(AbstractViewNode parent) {
    this.parent = parent;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getNamespace() {
    return namespace;
  }

  @Override
  public Class<?> getModelType() {
    return modelType;
  }

  public final List<View.Event> evaluate(Object model,
      ScopedViewContext context) throws Exception {

    final ViewNodeEvent nodeEvent = new ViewNodeEvent(
        ViewNodeEvent.Mode.VIEW_GENERATION, this, model, context);
    final List<View.Event> viewEvents = new LinkedList<>();

    context.push(name, modelType);
    if (context.getListeners().shouldVisitNode(nodeEvent)) {
      viewEvents.addAll(onEvaluate(model, context));
      context.getListeners().nodeVisited(nodeEvent);
    }
    context.pop();

    return viewEvents;
  }

  protected abstract List<View.Event> onEvaluate(Object source,
      ScopedViewContext context) throws Exception;

  protected View.Event newEvent(View.Event.Type type) {
    return newEvent(type, name, null);
  }

  protected View.Event newEvent(View.Event.Type type, String name) {
    return newEvent(type, name, null);
  }

  protected View.Event newEvent(View.Event.Type type, String name,
      Object value) {
    return new ConcreteViewEvent(type, name, namespace, value);
  }

  @Override
  public <T> T get(Class<T> type) {
    return scope.get(type);
  }

  @Override
  public <T> T get(String name, Class<T> type) {
    return scope.get(name, type);
  }

  @Override
  public void put(Object obj) {
    scope.put(obj);
  }

  @Override
  public Object put(String name, Object obj) {
    return scope.put(name, obj);
  }

  @Override
  public void putAll(Iterable<?> objs) {
    scope.putAll(objs);
  }

  @Override
  public void putAll(Map<String, ?> objs) {
    scope.putAll(objs);
  }

  @Override
  public boolean remove(Object obj) {
    return scope.remove(obj);
  }

}
