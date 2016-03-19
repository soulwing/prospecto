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
package org.soulwing.prospecto.spi;

import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;

/**
 * A {@link ViewTemplateBuilder} provider.
 * <p>
 * An object of this type provides a template builder implementation, and the
 * object provides factory methods to create the necessary builders.
 *
 * @author Carl Harris
 */
public interface ViewTemplateBuilderProvider {

  /**
   * Creates a builder for a view whose root node is an object node.
   * @param name name for the object in the view (may be {@code null})
   * @param namespace namespace for {@code name} (may by {@code null})
   * @param modelType model type to associate with the root view node
   * @return template builder
   * @throws ViewTemplateException
   */
  ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType) throws ViewTemplateException;

  /**
   * Creates a builder for a view whose root node is an array-of-objects node.
   * @param name name for the object in the view (may be {@code null})
   * @param elementName name for the elements in the array (may be {@code null})
   * @param namespace namespace for {@code name} (may by {@code null})
   * @param modelType model type to associate with the root view node
   * @return template builder
   * @throws ViewTemplateException
   */
  ViewTemplateBuilder arrayOfObjects(String name,
      String elementName, String namespace, Class<?> modelType)
      throws ViewTemplateException;

  /**
   * Creates a view template whose root node is an array-of-objects node
   * using the children of the root node of the given object template.
   * @param name name for the object in the view (may be {@code null})
   * @param elementName name for the elements in the array (may be {@code null})
   * @param namespace namespace for {@code name} (may by {@code null})
   * @param template source template (which must have a root node of type object)
   * @return template builder
   * @throws ViewTemplateException
   */
  ViewTemplate arrayOfObjects(String name,
      String elementName, String namespace, ViewTemplate template)
      throws ViewTemplateException;

  /**
   * Creates a view template whose root node is an array-of-values node.
   * @param name name for the object in the view (may be {@code null})
   * @param elementName name for the elements in the array (may be {@code null})
   * @param namespace namespace for {@code name} (may by {@code null})
   * @return view template
   * @throws ViewTemplateException
   */
  ViewTemplate arrayOfValues(String name,
      String elementName, String namespace) throws ViewTemplateException;

}
