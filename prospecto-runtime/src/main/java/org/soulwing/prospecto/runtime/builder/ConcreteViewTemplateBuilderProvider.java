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
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.runtime.node.RootArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.RootArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.RootObjectNode;
import org.soulwing.prospecto.spi.ViewTemplateBuilderProvider;

/**
 * A {@link ViewTemplateBuilderProvider} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilderProvider
    implements ViewTemplateBuilderProvider {

  private final ViewTemplateBuilderFactory builderFactory;

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
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, Class<?> modelType) throws ViewTemplateException {
    return builderFactory.newBuilder(
        new RootArrayOfObjectNode(name, elementName, namespace,
            modelType));
  }

  @Override
  public ViewTemplate arrayOfObjects(String name, String elementName,
      String namespace, ViewTemplate template) throws ViewTemplateException {
    final ConcreteViewTemplate templateImpl = templateImpl(template);
    final ObjectNode templateRoot = templateRoot(templateImpl);
    RootArrayOfObjectNode root = new RootArrayOfObjectNode(name, elementName,
        namespace, ((ConcreteViewTemplate) template).getRoot().getModelType());
    root.addChildren(templateRoot.getChildren());
    return new ConcreteViewTemplate(root);
  }

  @Override
  public ViewTemplate arrayOfValues(String name, String elementName,
      String namespace) throws ViewTemplateException {
    return new ConcreteViewTemplate(
        new RootArrayOfValueNode(name, elementName, namespace));
  }

  private ConcreteViewTemplate templateImpl(ViewTemplate template) {
    if (!(template instanceof ConcreteViewTemplate)) {
      throw new IllegalArgumentException("unsupported template");
    }
    return (ConcreteViewTemplate) template;
  }

  private ObjectNode templateRoot(ConcreteViewTemplate template) {
    if (!(template.getRoot() instanceof ObjectNode)) {
      throw new IllegalArgumentException(
          "template must be for a view of type `object`");
    }
    return (ObjectNode) template.getRoot();
  }

}
