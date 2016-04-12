/*
 * File created on Apr 8, 2016
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
package org.soulwing.prospecto.runtime.template;

import java.util.EnumSet;
import java.util.Map;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.scope.MutableScope;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.scope.ConcreteMutableScope;

/**
 * An abstract base for {@link ViewNode} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractViewNode implements ViewNode, MutableScope {

  private final ConcreteMutableScope scope = new ConcreteMutableScope();

  private final String name;
  private final String namespace;
  private final Class<?> modelType;

  private AbstractViewNode parent;
  private Accessor accessor;
  private EnumSet<AccessMode> allowedModes = EnumSet.allOf(AccessMode.class);

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

  public abstract Object accept(ViewNodeVisitor visitor, Object state);

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

  public Accessor getAccessor() {
    return accessor;
  }

  public void setAccessor(Accessor accessor) {
    this.accessor = accessor;
  }

  public EnumSet<AccessMode> getAllowedModes() {
    return allowedModes;
  }

  public void setAllowedModes(EnumSet<AccessMode> allowedModes) {
    this.allowedModes = allowedModes;
  }

  public EnumSet<AccessMode> getSupportedModes() {
    final Accessor accessor = getAccessor();
    return accessor != null ?
        accessor.getSupportedModes() : EnumSet.allOf(AccessMode.class);
  }

  public String getPropertyName() {
    return getAccessor().getName();
  }

  public final void validateAccessModes() throws ViewTemplateException {
    final EnumSet<AccessMode> modes = EnumSet.allOf(AccessMode.class);
    modes.retainAll(getAllowedModes());
    modes.removeAll(getSupportedModes());
    if (!modes.isEmpty()) {
      throw new ViewTemplateException(String.format(
          "model property %s.%s does not support mode(s) %s",
          getModelType().getSimpleName(), getPropertyName(), modes));
    }
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

  public void putAll(AbstractViewNode node) {
    scope.putAll(node.scope);
  }

  @Override
  public String toString() {
    return getName();
  }

}
