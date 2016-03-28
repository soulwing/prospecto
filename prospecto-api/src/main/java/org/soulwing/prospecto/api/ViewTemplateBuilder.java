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

import java.util.EnumSet;
import java.util.Map;

import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;

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
   * @return template builder for new object node
   */
  ViewTemplateBuilder object(String name, Class<?> modelType);

  /**
   * Adds an object node at the cursor using the root node of the given
   * template.
   * <p>
   * <strong>NOTE</strong>: this method returns the same builder instance,
   * rather than a sub-builder; the template provides the full structure of
   * the object node.
   *
   * @param name name for the node in the view produced by this builder
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder object(String name, ViewTemplate template);

  /**
   * Adds an object node at the cursor.
   * @param name name for the object in the view
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @param modelType the data type for the corresponding model
   * @return template builder for new object node
   */
  ViewTemplateBuilder object(String name, String namespace, Class<?> modelType);

  /**
   * Adds an object node at the cursor using the root node of the given
   * template.
   * <p>
   * <strong>NOTE</strong>: this method returns the same builder instance,
   * rather than a sub-builder; the template provides the full structure of
   * the object node.
   *
   * @param name name for the node in the view produced by this builder
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder object(String name, String namespace,
      ViewTemplate template);

  /**
   * Adds a reference object node at the cursor.
   * @param name name for the object in the view
   * @param modelType the data type for the corresponding model
   * @return template builder for new object node
   */
  ViewTemplateBuilder reference(String name, Class<?> modelType);

  /**
   * Adds a reference object node at the cursor using the root node of the given
   * template.
   * <p>
   * <strong>NOTE</strong>: this method returns the same builder instance,
   * rather than a sub-builder; the template provides the full structure of
   * the object node.
   *
   * @param name name for the node in the view produced by this builder
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder reference(String name, ViewTemplate template);

  /**
   * Adds a reference object node at the cursor.
   * @param name name for the object in the view
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @param modelType the data type for the corresponding model
   * @return template builder for new object node
   */
  ViewTemplateBuilder reference(String name, String namespace,
      Class<?> modelType);

  /**
   * Adds a reference object node at the cursor using the root node of the given
   * template.
   * <p>
   * <strong>NOTE</strong>: this method returns the same builder instance,
   * rather than a sub-builder; the template provides the full structure of
   * the object node.
   *
   * @param name name for the node in the view produced by this builder
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder reference(String name, String namespace,
      ViewTemplate template);

  /**
   * Adds an array-of-objects node at the cursor.
   * @param name name for the array in the view
   * @param modelType the data type for the elements of the array
   * @return template builder for new array-of-objects node
   */
  ViewTemplateBuilder arrayOfObjects(String name, Class<?> modelType);

  /**
   * Adds an array-of-objects node at the cursor using the root node of the
   * given template.
   * <p>
   * <strong>NOTE</strong>: this method returns the same builder instance,
   * rather than a sub-builder; the template provides the full structure of
   * the object node.
   *
   * @param name name for the array in the view
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder arrayOfObjects(String name, ViewTemplate template);

  /**
   * Adds an array-of-objects node at the cursor.
   * @param name name for the array in the view
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param modelType the data type for the elements of the array
   * @return template builder for new array-of-objects node
   */
  ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      Class<?> modelType);

  /**
   * Adds an array-of-objects node at the cursor using the root node of the
   * given template.
   * <p>
   * <strong>NOTE</strong>: this method returns the same builder instance,
   * rather than a sub-builder; the template provides the full structure of
   * the object node.
   *
   * @param name name for the array in the view
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      ViewTemplate template);

  /**
   * Adds an array-of-objects node  at the cursor.
   * @param name name for the array in the view
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param modelType the data type for the elements of the array
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @return template builder for new array-of-objects node
   */
  ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, Class<?> modelType);

  /**
   * Adds an array-of-objects node at the cursor using the root node of the
   * given template.
   * <p>
   * <strong>NOTE</strong>: this method returns the same builder instance,
   * rather than a sub-builder; the template provides the full structure of
   * the object node.
   *
   * @param name name for the array in the view
   * @param elementName name for the elements in the array; used in only some
   *    some view types (e.g. XML)
   * @param namespace namespace for {@code name} and {@code elementName};
   *    used in only some view types (e.g. XML)
   * @param template source template
   * @return this builder
   */
  ViewTemplateBuilder arrayOfObjects(String name, String elementName,
      String namespace, ViewTemplate template);

  /**
   * Adds an envelope node at the cursor.
   * <p>
   * An envelope inserts an extra node of type object in the view, but the
   * source model for properties of the object is the parent node.
   * @param name name for the node in the view
   * @return template builder for new envelope
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
   * @return template builder for new envelope
   */
  ViewTemplateBuilder envelope(String name, String namespace);

  /**
   * Introduces a subtype of the type associated with the current object or
   * array-of-objects node.
   * <p>
   * This allows properties of a subtype to be included in the object or
   * array-of-objects node being described.
   * <p>
   * A discriminator must be added to the object or array-of-objects node
   * before a subtyoe can be introduced.
   * s
   * @param subtype class which must be a descendant of the model type specified
   *   for the parent object or array-of-objects node
   * @return template builder for the new subtype
   */
  ViewTemplateBuilder subtype(Class<?> subtype);

  /**
   * Adds a subtype discriminator at the cursor.
   * <p>
   * When the view is evaluated a {@link DiscriminatorStrategy} will be
   * retrieved from the view context to produce the discriminator name/value.
   * If no strategy is configured a default implementation will be provided.
   * <p>
   * A discriminator (if present) must be added as the first child of an object
   * or array-of-objects node that has multiple subtypes.
   *
   * @return this builder
   */
  ViewTemplateBuilder discriminator();

  /**
   * Adds a subtype discriminator at the cursor.
   * <p>
   * A discriminator (if present) must be added as the first child of an object
   * or array-of-objects node that has multiple subtypes.
   * @param discriminatorClass discriminator strategy class
   * @param configuration name-value pairs that will be used to configure the
   *   strategy instance after it has been constructed; each pair specifies
   *   the name of a configuration property and the corresponding value
   * @return this builder
   */
  ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Object... configuration);

  /**
   * Adds a subtype discriminator at the cursor.
   * <p>
   * A discriminator (if present) must be added as the first child of an object
   * or array-of-objects node that has multiple subtypes.
   * @param discriminatorClass discriminator strategy class
   * @param configuration name-value pairs that will be used to configure the
   *   strategy instance after it has been constructed; each pair specifies
   *   the name of a configuration property and the corresponding value
   * @return this builder
   */
  ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Map configuration);

  /**
   * Adds a subtype discriminator at the cursor.
   * <p>
   * A discriminator (if present) must be added as the first child of an object
   * or array-of-objects node that has multiple subtypes.
   * @param discriminator discriminator strategy
   * @return this builder
   */
  ViewTemplateBuilder discriminator(DiscriminatorStrategy discriminator);

  /**
   * Adds a URL node at the cursor.
   * <p>
   * When generating a view from a template that includes a URL node, the
   * {@link ViewContext} must contain a {@link UrlResolver}.
   *
   * @return this builder
   * @see UrlResolver
   */
  ViewTemplateBuilder url();

  /**
   * Adds a URL node at the cursor.
   * <p>
   * When generating a view from a template that includes a URL node, the
   * {@link ViewContext} must contain a {@link UrlResolver}.
   *
   * @param name name for the node in the view
   * @return this builder
   * @see UrlResolver
   */
  ViewTemplateBuilder url(String name);

  /**
   * Adds a URL node at the cursor.
   * <p>
   * When generating a view from a template that includes a URL node, the
   * {@link ViewContext} must contain a {@link UrlResolver}.
   *
   * @param name name for the node in the view
   * @param namespace namespace for {@code name}; used in only some view
   *    types (e.g. XML)
   * @return this builder
   * @see UrlResolver
   */
  ViewTemplateBuilder url(String name, String namespace);

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
   * Specifies the access modes that will be allowed on the model property
   * associated with the node at the cursor.
   * @param modes the modes to allow
   * @return this builder
   */
  ViewTemplateBuilder allow(AccessMode mode, AccessMode... modes);

  /**
   * Specifies the access modes that will be allowed on the model property
   * associated with the node at the cursor.
   * @param modes the modes to allow
   * @return this builder
   */
  ViewTemplateBuilder allow(EnumSet<AccessMode> modes);

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
      Map configuration);

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
   * Puts an attribute into the scope associated with the node at the cursor.
   * @param value the attribute value to put
   * @return this builder
   */
  ViewTemplateBuilder attribute(Object value);

  /**
   * Puts an attribute into the scope associated with the node at the cursor.
   * @param name name for the attribute
   * @param value the attribute value to put
   * @return this builder
   */
  ViewTemplateBuilder attribute(String name, Object value);

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
   * @throws ViewTemplateException if this method is invoked when the cursor
   *    is positioned on a node that not a child of the root node
   */
  ViewTemplate build();

}
