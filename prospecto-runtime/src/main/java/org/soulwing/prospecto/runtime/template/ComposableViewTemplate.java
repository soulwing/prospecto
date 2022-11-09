/*
 * File created on Apr 5, 2016
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

import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;

/**
 * A view template that can be composed in another view template.
 *
 * @author Carl Harris
 */
public interface ComposableViewTemplate extends ViewTemplate {

  /**
   * Extracts a view node of object type from the root of this template for
   * use as a subview in another template.
   * @param name name for the node (as it will appear in the calling template)
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @return new view node containing the children of the root node of this
   *    template
   * @see ViewTemplateBuilder#object(String, String, ViewTemplate)
   */
  AbstractContainerNode object(String name, String namespace);

  /**
   * Extracts a view node of reference type from the root of this template for
   * use as a subview in another template.
   * @param name name for the node (as it will appear in the calling template)
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @return new view node containing the children of the root node of this
   *    template
   * @see ViewTemplateBuilder#reference(String, String, ViewTemplate)
   */
  AbstractContainerNode reference(String name, String namespace);

  /**
   * Extracts a view node of array-of-objects type from the root of this
   * template for use as a subview in another template.
   * @param name name for the node (as it will appear in the calling template)
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   *    in only some view types (e.g. XML)
   * @return new view node containing the children of the root node of this
   *    template
   * @see ViewTemplateBuilder#arrayOfObjects(String, String, String, ViewTemplate)
   */
  AbstractContainerNode arrayOfObjects(String name, String elementName,
      String namespace);

  /**
   * Extracts a view node of array-of-references type from the root of this
   * template for use as a subview in another template.
   * @param name name for the node (as it will appear in the calling template)
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   *    in only some view types (e.g. XML)
   * @return new view node containing the children of the root node of this
   *    template
   * @see ViewTemplateBuilder#arrayOfReferences(String, String, String, ViewTemplate)
   */
  AbstractContainerNode arrayOfReferences(String name, String elementName,
      String namespace);

  /**
   * Extracts a view template of array-of-objects type from the root of this
   * template.
   * @param name name for the root (as it will appear in the calling template)
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   *    in only some view types (e.g. XML)
   * @return new view template containing the children of the root node of this
   *    template
   * @see org.soulwing.prospecto.ViewTemplateBuilderProducer#arrayOfObjects(String, String, String, ViewTemplate)
   */
  ViewTemplate arrayOfObjectsTemplate(String name, String elementName,
      String namespace);

  /**
   * Extracts a view template of array-of-references type from the root of this
   * template.
   * @param name name for the root (as it will appear in the calling template)
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   *    in only some view types (e.g. XML)
   * @return new view template containing the children of the root node of this
   *    template
   * @see org.soulwing.prospecto.ViewTemplateBuilderProducer#arrayOfReferences(String, String, String, ViewTemplate)
   */
  ViewTemplate arrayOfReferencesTemplate(String name, String elementName,
      String namespace);

  /**
   * Extracts a view node of map-of-objects type from the root of this
   * template for use as a subview in another template.
   *
   * @param name name for the node (as it will appear in the calling template)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   * in only some view types (e.g. XML)
   * @param keyType
   * @return new view node containing the children of the root node of this
   * template
   * @see ViewTemplateBuilder#mapOfObjects(String, String, Class, ViewTemplate)
   */
  AbstractContainerNode mapOfObjects(String name, String namespace, Class<?> keyType);

  /**
   * Extracts a view node of map-of-references type from the root of this
   * template for use as a subview in another template.
   *
   * @param name name for the node (as it will appear in the calling template)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   * in only some view types (e.g. XML)
   * @param keyType
   * @return new view node containing the children of the root node of this
   * template
   * @see ViewTemplateBuilder#mapOfReferences(String, String, Class, ViewTemplate)
   */
  AbstractContainerNode mapOfReferences(String name, String namespace, Class<?> keyType);

  /**
   * Extracts a view template of map-of-objects type from the root of this
   * template.
   *
   * @param name name for the root (as it will appear in the calling template)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   * in only some view types (e.g. XML)
   * @param keyType
   * @return new view template containing the children of the root node of this
   * template
   * @see org.soulwing.prospecto.ViewTemplateBuilderProducer#mapOfObjects(String, String, Class, ViewTemplate)
   */
  ViewTemplate mapOfObjectsTemplate(String name, String namespace, Class<?> keyType);

  /**
   * Extracts a view template of map-of-references type from the root of this
   * template.
   *
   * @param name name for the root (as it will appear in the calling template)
   * @param namespace namespace for {@code name} and {@code elementName}; used
   * in only some view types (e.g. XML)
   * @param keyType
   * @return new view template containing the children of the root node of this
   * template
   * @see org.soulwing.prospecto.ViewTemplateBuilderProducer#mapOfReferences(String, String, Class, ViewTemplate)
   */
  ViewTemplate mapOfReferencesTemplate(String name, String namespace, Class<?> keyType);

}
