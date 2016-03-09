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
import org.soulwing.prospecto.runtime.accessor.RootAccessor;
import org.soulwing.prospecto.runtime.accessor.RootMultiValuedAccessor;
import org.soulwing.prospecto.runtime.builder.ConcreteViewTemplate;
import org.soulwing.prospecto.runtime.builder.ConcreteViewTemplateBuilder;
import org.soulwing.prospecto.runtime.node.ArrayOfObjectNode;
import org.soulwing.prospecto.runtime.node.ArrayOfValueNode;
import org.soulwing.prospecto.runtime.node.ObjectNode;
import org.soulwing.prospecto.spi.ViewTemplateBuilderProvider;

/**
 * A {@link ViewTemplateBuilderProvider} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilderProvider
    implements ViewTemplateBuilderProvider {

  @Override
  public ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType) throws ViewTemplateException {
    final ObjectNode target = new ObjectNode(name, namespace,
        modelType, new RootAccessor());
    return new ConcreteViewTemplateBuilder(modelType, target);

  }

  @Override
  public ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, Class<?> modelType) throws ViewTemplateException {
    final ArrayOfObjectNode target =
        new ArrayOfObjectNode(name, elementName, namespace, modelType,
            new RootMultiValuedAccessor());
    return new ConcreteViewTemplateBuilder(modelType, target);
  }

  @Override
  public ViewTemplate arrayOfValues(String name, String elementName,
      String namespace) throws ViewTemplateException {
    return new ConcreteViewTemplate(
        new ArrayOfValueNode(name, elementName, namespace,
            new RootMultiValuedAccessor()));
  }

}
