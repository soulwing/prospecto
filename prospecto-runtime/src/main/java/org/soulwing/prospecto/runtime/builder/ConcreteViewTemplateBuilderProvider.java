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

import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.runtime.template.ComposableViewTemplate;
import org.soulwing.prospecto.runtime.template.ConcreteViewTemplate;
import org.soulwing.prospecto.runtime.template.RootArrayOfObjectNode;
import org.soulwing.prospecto.runtime.template.RootArrayOfReferencesNode;
import org.soulwing.prospecto.runtime.template.RootArrayOfValuesNode;
import org.soulwing.prospecto.runtime.template.RootMapOfObjectsNode;
import org.soulwing.prospecto.runtime.template.RootMapOfReferencesNode;
import org.soulwing.prospecto.runtime.template.RootMapOfValuesNode;
import org.soulwing.prospecto.runtime.template.RootObjectNode;
import org.soulwing.prospecto.runtime.template.RootReferenceNode;
import org.soulwing.prospecto.spi.ViewTemplateBuilderProvider;

/**
 * A {@link ViewTemplateBuilderProvider} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilderProvider
    implements ViewTemplateBuilderProvider {

  private final ViewTemplateBuilderFactory builderFactory;

  @SuppressWarnings("unused")
  public ConcreteViewTemplateBuilderProvider() {
    this(new ConcreteViewTemplateBuilderFactory());
  }

  ConcreteViewTemplateBuilderProvider(
      ViewTemplateBuilderFactory builderFactory) {
    this.builderFactory = builderFactory;
  }

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType) throws ViewTemplateException {
    return builderFactory.newBuilder(
        new RootObjectNode(name, namespace, modelType));
  }

  @Override
  public ViewTemplateBuilder reference(String name, String namespace,
      Class<?> modelType) throws ViewTemplateException {
    return builderFactory.newBuilder(
        new RootReferenceNode(name, namespace, modelType));
  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, Class<?> modelType) throws ViewTemplateException {
    return builderFactory.newBuilder(
        new RootArrayOfObjectNode(name, elementName, namespace,
            modelType));
  }

  @Override
  public ViewTemplate arrayOfObjects(String name, String elementName,
      String namespace, ViewTemplate template) throws ViewTemplateException {
    return ((ComposableViewTemplate) template)
        .arrayOfObjectsTemplate(name, elementName, namespace);
  }

  @Override
  public ViewTemplateBuilder arrayOfReferences(String name, String elementName,
      String namespace, Class<?> modelType) throws ViewTemplateException {
    return builderFactory.newBuilder(
        new RootArrayOfReferencesNode(name, elementName, namespace,
            modelType));
  }

  @Override
  public ViewTemplate arrayOfReferences(String name, String elementName,
      String namespace, ViewTemplate template) throws ViewTemplateException {
    return ((ComposableViewTemplate) template)
        .arrayOfReferencesTemplate(name, elementName, namespace);
  }

  @Override
  public ViewTemplate arrayOfValues(String name, String elementName,
      String namespace) throws ViewTemplateException {
    return new ConcreteViewTemplate(
        new RootArrayOfValuesNode(name, elementName, namespace));
  }

  @Override
  public ViewTemplateBuilder mapOfObjects(String name,
      String namespace, Class<?> keyType, Class<?> modelType) throws ViewTemplateException {
    return builderFactory.newBuilder(
        new RootMapOfObjectsNode(name, namespace, keyType, modelType));
  }

  @Override
  public ViewTemplate mapOfObjects(String name,
      String namespace, Class<?> keyType, ViewTemplate template) throws ViewTemplateException {
    return ((ComposableViewTemplate) template)
        .mapOfObjectsTemplate(name, namespace, keyType);
  }

  @Override
  public ViewTemplateBuilder mapOfReferences(String name,
      String namespace, Class<?> keyType, Class<?> modelType)
      throws ViewTemplateException {
    return builderFactory.newBuilder(
        new RootMapOfReferencesNode(name, namespace,
            keyType, modelType));
  }

  @Override
  public ViewTemplate mapOfReferences(String name,
      String namespace, Class<?> keyType, ViewTemplate template) throws ViewTemplateException {
    return ((ComposableViewTemplate) template)
        .mapOfReferencesTemplate(name, namespace, keyType);
  }

  @Override
  public ViewTemplate mapOfValues(String name,
      String namespace, Class<?> keyType, Class<?> componentType)
      throws ViewTemplateException {
    return new ConcreteViewTemplate(
        new RootMapOfValuesNode(name, namespace, keyType, componentType));
  }

}
