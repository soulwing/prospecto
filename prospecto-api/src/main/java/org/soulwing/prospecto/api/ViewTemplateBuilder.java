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
package org.soulwing.prospecto.api;

import java.util.Map;

import org.soulwing.prospecto.api.converter.ValueTypeConverter;

/**
 * A builder for a {@link ViewTemplate}.
 * <p>
 * The builder maintains a tree structure of nodes that correspond to the nodes
 * of a view.
 * <p>
 * There are four fundamental node types:
 * <ul>
 *   <li>value &mdash; a single value of a type that has a simple textual
 *       representation; e.g. string, number, boolean, etc</li>
 *   <li>object &mdash; a collection of named properties each of which has an
 *       associated node</li>
 *   <li>array-of-values &mdash; an ordered collection of values of a single type
 *       that has a simple textual representation; e.g. string, number, boolean,
 *       etc</li>
 *   <li>array-of-object &mdash; an ordered collection of objects the same
 *       "type"; the type is a collection of named properties each of which has
 *       an associated node</li>
 * </ul>
 * The builder has a <em>cursor</em> which initially points at the root node.
 * Methods that add a new node act at this cursor, and subsequently reposition
 * the cursor such that it points at the newly added node.
 * <p>
 * After adding a node of type <em>object</em> or <em>array-of-object</em> the
 * cursor is positioned such that the next node added will be the first child
 * of the object/array-of-object node. A subsequently added node will be the
 * next sibling of the first child, etc. When all siblings have been added to
 * a given object or array-of-object, the {@link #end()} method can be used to
 * reposition the cursor such that it points at the last child added to the
 * parent node.
 *
 * @author Carl Harris
 */
public interface ViewTemplateBuilder {

  /**
   * Adds a value node at the cursor.
   * @param name name for the value in the view
   * @return this builder
   */
  ViewTemplateBuilder value(String name);

  /**
   * Adds a value node at the cursor.
   * @param name name for the value in the view
   * @param namespace namespace for {@code name}; used in only some view
   *    types (e.g. XML)
   * @return this builder
   */
  ViewTemplateBuilder value(String name, String namespace);

  /**
   * Adds an array-of-values node at the cursor.
   * @param name name for the array in the view
   * @return this builder
   */
  ViewTemplateBuilder arrayOfValues(String name);

  /**
   * Adds an array-of-values at the cursor.
   * @param name name for the array in the view
   * @param elementName name for the elements in the view; used in only
   *    some view types (e.g. XML)
   * @return this builder
   */
  ViewTemplateBuilder arrayOfValues(String name, String elementName);

  /**
   * Adds an array-of-values at the cursor.
   * @param name name for the array in the view
   * @param elementName name for the elements in the view; used in only
   *    some view types (e.g. XML)
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @return this builder
   */
  ViewTemplateBuilder arrayOfValues(String name, String elementName,
      String namespace);

  /**
   * Adds an object node at the cursor.
   * @param name name for the object in the view
   * @param modelType the data type for the corresponding model
   * @return this builder
   */
  ViewTemplateBuilder object(String name, Class<?> modelType);

  /**
   * Adds an object node at the cursor.
   * @param name name for the object in the view
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @param modelType the data type for the corresponding model
   * @return this builder
   */
  ViewTemplateBuilder object(String name, String namespace,
      Class<?> modelType);

  /**
   * Adds an array-of-objects node at the cursor.
   * @param name name for the array in the view
   * @param modelType the data type for the elements of the array
   * @return this builder
   */
  ViewTemplateBuilder arrayOfObjects(String name, Class<?> modelType);

  /**
   * Adds an array-of-objects node at the cursor.
   * @param name name for the array in the view
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param modelType the data type for the elements of the array
   * @return this builder
   */
  ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      Class<?> modelType);

  /**
   * Adds an array-of-objects node  at the cursor.
   * @param name name for the array in the view
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param modelType the data type for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @return this builder
   */
  ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, Class<?> modelType);

  /**
   * Adds a node at the cursor using the root node of the specified template.
   * @param name name for the node in the view
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder subview(String name, ViewTemplate template);

  /**
   * Adds a URL node at the cursor.
   * TODO: provide description/reference on how this works
   * @return this builder
   */
  ViewTemplateBuilder url();

  /**
   * Adds a URL node at the cursor.
   * TODO: provide description/reference on how this works
   * @param name name for the node in the view
   * @return this builder
   */
  ViewTemplateBuilder url(String name);

  /**
   * Adds a URL node at the cursor.
   * TODO: provide description/reference on how this works
   * @param name name for the node in the view
   * @param namespace namespace for {@code name}; used in only some view 
   *    types (e.g. XML)
   * @return this builder
   */
  ViewTemplateBuilder url(String name, String namespace);

  /**
   * Adds an envelope node at the cursor.
   * <p>
   * An envelope inserts an extra node of type object in the view, but the
   * source model for properties of the object is the parent node.
   * @param name name for the node in the view
   * @return this builder
   */
  ViewTemplateBuilder envelope(String name);

  /**
   * Adds an envelope node at the cursor.
   * <p>
   * An envelope inserts an extra node of type object in the view, but the
   * source model for properties of the object is the parent node.
   * @param name name for the node in the view
   * @param namespace namespace for {@code name}; used in only some view
   *    types (e.g. XML)
   * @return this builder
   */
  ViewTemplateBuilder envelope(String name, String namespace);

  /**
   * Specifies the name of the model property that is the source for the node
   * at the cursor.
   * @param name model property name
   * @return this builder
   */
  ViewTemplateBuilder source(String name);

  /**
   * Specifies the access type that will be used to access the model property
   * associated with the node at the cursor.
   * @param accessType access type
   * @return this builder
   */
  ViewTemplateBuilder accessType(AccessType accessType);

  /**
   * Specifies a converter that will be used to transform the model property
   * associated with the node at the cursor.
   * <p>
   * An instance of the specified converter will be constructed and configured
   * and will be used by only the value node at the cursor <em>instead of</em>
   * consulting converters registered with the view context.
   *
   * @param converterClass converter class
   * @param configuration name-value pairs that will be used to configure the
   *   converter instance after it has been constructed; each pair specifies
   *   the name of a configuration property and the corresponding value
   * @return this builder
   */
  ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Object... configuration);

  /**
   * Specifies a converter that will be used to transform the model property
   * associated with the node at the cursor.
   * <p>
   * An instance of the specified converter will be constructed and configured
   * and will be used by only the value node at the cursor <em>instead of</em>
   * consulting converters registered with the view context.
   *
   * @param converterClass converter class
   * @param configuration name-value pairs that will be used to configure the
   *   converter instance after it has been constructed; each pair specifies
   *   the name of a configuration property and the corresponding value
   * @return this builder
   */
  ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Map<String, Object> configuration);

  /**
   * Specifies a converter that will be used to transform the model property
   * associated with the node at the cursor.
   * <p>
   * The given converter instance will be used by the value node at the
   * cursor <em>instead of</em> consulting converters registered with the view
   * context.
   *
   * @param converter converter class
   * @return this builder
   */
  ViewTemplateBuilder converter(ValueTypeConverter<?> converter);

  /**
   * Moves the cursor to the parent of the cursor node.
   * <p>
   * If the cursor is already positioned at the root node, this method has no
   * effect.
   * @return this builder
   */
  ViewTemplateBuilder end();

  /**
   * Builds the template that corresponds to the node tree structure contained
   * in this builder instance.
   * @return view template
   */
  ViewTemplate build();

}
