/*
 * File created on Mar 28, 2016
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
package org.soulwing.prospecto.runtime.accessor;

import java.beans.IntrospectionException;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplateException;

/**
 * An {@link AccessorBuilder} that builds accessors that utilize reflection.
 *
 * @author Carl Harris
 */
class ReflectionAccessorBuilder implements AccessorBuilder {

  private final Class<?> modelType;

  private String propertyName;
  private AccessType accessType = AccessType.PROPERTY;

  ReflectionAccessorBuilder(Class<?> modelType) {
    this.modelType = modelType;
  }

  @Override
  public AccessorBuilder propertyName(String name) {
    this.propertyName = name;
    return this;
  }

  @Override
  public AccessorBuilder accessType(AccessType type) {
    this.accessType = type;
    return this;
  }

  @Override
  public Accessor build() {
    try {
      switch (accessType) {
        case FIELD:
          return ReflectionAccessorFactory.field(modelType, propertyName);
        case PROPERTY:
          return ReflectionAccessorFactory.property(modelType, propertyName);
        default:
          throw new IllegalArgumentException("unrecognized access type: "
              + accessType.name());
      }
    }
    catch (IntrospectionException ex) {
      throw new ViewTemplateException(ex);
    }
    catch (NoSuchFieldException | NoSuchMethodException ex) {
      throw new ViewTemplateException("property '" + propertyName
          + "' not found on type " + modelType.getName());
    }
  }

}
