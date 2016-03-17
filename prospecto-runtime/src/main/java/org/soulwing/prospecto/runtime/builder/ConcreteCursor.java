/*
 * File created on Mar 15, 2016
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
package org.soulwing.prospecto.runtime.builder;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.accessor.ReflectionAccessorFactory;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A {@link Cursor} implementation.
 *
 * @author Carl Harris
 */
class ConcreteCursor implements Cursor {

  private final Class<?> modelType;
  private final AccessorFactory accessorFactory;

  private AbstractViewNode node;
  private String modelName;
  private AccessType accessType = AccessType.PROPERTY;

  ConcreteCursor(Class<?> modelType) {
    this(modelType, new ReflectionAccessorFactory());
  }

  ConcreteCursor(ConcreteCursor cursor, Class<?> modelType) {
    this(modelType, cursor.accessorFactory);
    this.node = cursor.node;
    this.accessType = cursor.getAccessType();
  }

  ConcreteCursor(Class<?> modelType, AccessorFactory accessorFactory) {
    this.modelType = modelType;
    this.accessorFactory = accessorFactory;
  }

  @Override
  public AbstractViewNode getNode() {
    return node;
  }

  @Override
  public Class<?> getModelType() {
    return modelType;
  }

  @Override
  public String getModelName() {
    return modelName;
  }

  @Override
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  @Override
  public AccessType getAccessType() {
    return accessType;
  }

  @Override
  public void setAccessType(AccessType accessType) {
    this.accessType = accessType;
  }

  @Override
  public void advance() {
    advance(null, null);
  }

  @Override
  public void advance(AbstractViewNode node) {
    advance(node, node.getName());
  }

  @Override
  public void advance(AbstractViewNode node, String modelName) {
    update();
    this.node = node;
    this.modelName = modelName;
  }

  private void update() {
    if (modelName == null) return;
    try {
      node.setAccessor(accessorFactory.newAccessor(modelType, modelName,
          accessType));
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  @Override
  public Cursor copy(Class<?> modelType) {
    return new ConcreteCursor(this, modelType);
  }

}
