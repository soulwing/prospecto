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
package org.soulwing.prospecto.runtime.builder;

import java.beans.IntrospectionException;

import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ValueConverter;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.AccessorFactory;
import org.soulwing.prospecto.runtime.node.EventGeneratingViewNode;

/**
 * A configurator for a view node.
 *
 * TODO: I hate this abstraction and it's relationship to ConcreteViewTemplateBuilder.
 * @author Carl Harris
 */
class ViewNodeConfigurator {

  private final EventGeneratingViewNode target;
  private final Class<?> declaringClass;

  private String name;
  private AccessType accessType = AccessType.FIELD;
  private Class<? extends ValueConverter> converterClass;

  public ViewNodeConfigurator(EventGeneratingViewNode target, Class<?> declaringClass,
      String defaultName) {
    this.target = target;
    this.name = defaultName;
    this.declaringClass = declaringClass;
  }

  public ViewNodeConfigurator(ViewNodeConfigurator source,
      EventGeneratingViewNode target) {
    this(target, source.declaringClass, source.name);
    this.accessType = source.accessType;
    this.converterClass = source.converterClass;
  }

  public void setSource(String name) {
    this.name = name;
  }

  public void setAccessType(AccessType accessType) {
    this.accessType = accessType;
  }

  public void setConverter(Class<? extends ValueConverter> converterClass) {
    this.converterClass = converterClass;
  }

  public Accessor configure() {
    try {
      final Accessor accessor = newAccessor(declaringClass);
      target.setAccessor(accessor);
      return accessor;
    }
    catch (Exception ex) {
      throw new ViewTemplateException(ex);
    }
  }

  protected Accessor newAccessor(Class<?> declaringClass)
      throws NoSuchFieldException, NoSuchMethodException, IntrospectionException,
      InstantiationException, IllegalAccessException {
    final Accessor accessor = AccessorFactory.accessor(declaringClass,
        name, accessType);
    if (converterClass != null) {
      return AccessorFactory.converter(accessor, converterClass);
    }
    return accessor;
  }

}
