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
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A {@link Cursor} implementation.
 *
 * @author Carl Harris
 */
class ConcreteCursor implements Cursor {

  private final Class<?> modelType;

  private AbstractViewNode node;
  private String modelName;
  private AccessType accessType = AccessType.PROPERTY;

  ConcreteCursor(Class<?> modelType) {
    this.modelType = modelType;
  }

  ConcreteCursor(Cursor cursor, Class<?> modelType) {
    this.modelType = modelType;
    this.accessType = cursor.getAccessType();
  }

  /**
   * Gets the {@code modelType} property.
   * @return property value
   */
  @Override
  public Class<?> getModelType() {
    return modelType;
  }

  /**
   * Gets the {@code node} property.
   * @return property value
   */
  @Override
  public AbstractViewNode getNode() {
    return node;
  }

  /**
   * Gets the {@code modelName} property.
   * @return property value
   */
  @Override
  public String getModelName() {
    return modelName;
  }

  /**
   * Sets the {@code modelName} property.
   * @param modelName the property value to set
   */
  @Override
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  /**
   * Gets the {@code accessType} property.
   * @return property value
   */
  @Override
  public AccessType getAccessType() {
    return accessType;
  }

  /**
   * Sets the {@code accessType} property.
   * @param accessType the property value to set
   */
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

  @Override
  public void update() {
    if (modelName == null) return;
    if (modelType == null) {
      throw new AssertionError("modelType is required");
    }
    try {
      node.setAccessor(AccessorFactory.accessor(modelType, modelName,
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
