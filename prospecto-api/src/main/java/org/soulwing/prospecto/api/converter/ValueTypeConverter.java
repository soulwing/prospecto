/*
 * File created on Mar 12, 2016
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
package org.soulwing.prospecto.api.converter;

import org.soulwing.prospecto.api.ViewContext;

/**
 * A converter that transforms simple model objects into a suitable
 * representation for a view node of value type and vice-versa.
 * <p>
 * View nodes support values represented as strings, numbers, or booleans. The
 * {@link Coerce} utility provided in the Prospecto API supports type coercion
 * from many Java value types to one of these view representations. A converter
 * that implements this interface allows other model types to be represented in
 * value nodes.
 * <p>
 * Almost any model type that has a well-defined lexical representation can be
 * transformed using a converter so that it may be used in view node of value
 * type. For example, your model might define a Money type that describes an
 * amount of money in a given currency. You could make a converter that
 * creates a string representation of the Money type; the string consists of
 * both the number representing the amount and the currency designator.
 * <p>
 * In many cases, you can <strong>avoid the need to create a custom
 * converter</strong> by
 * <p>
 * A converter's {@link #supports(Class)} method is used to determine whether
 * a given model type can be converted. This method could be implemented using
 * {@link #equals(Object)} in which case the converter supports exactly one
 * model type (in particular such a converter rejects subclasses of the model
 * type). Alternatively, the {@link #supports(Class)} method could be
 * implemented using {@link Class#isAssignableFrom(Class)}, in which case the
 * converter claims support for the given type and <em>all</em> of its subtypes.
 * There may be other useful strategies in determining whether to support a
 * given model type; for example, all model types with a particular annotation.
 * In any case, <em>care must be taken in choosing both the base type to
 * support and the order in which converters are consulted</em>, so that the
 * appropriate converter is used for given model type.
 * <p>
 * A converter <em>must</em> have a no-arg constructor in order to fully support
 * all view template configurations. The framework provides the ability to
 * configure the converter after it is constructed by injecting values of
 * simple types via JavaBeans-style accessors. Additionally the framework will
 * invoke any methods annotated with {@link javax.annotation.PostConstruct}
 * after any configuration properties have been injected.
 * <p>
 * A converter instance will be used to perform an arbitrary number of type
 * conversions by multiple concurrent threads. The best way to satisfy these
 * requirements is for the converter to use a <em>stateless</em> design; i.e.
 * it should not modify <em>any</em> instance variable after the converter is
 * created and configured. Designs using thread synchronization mechanisms are
 * also possible, but may have a significant impact on view generation
 * performance.
 *
 * @param <V> value type; must be one of String, Number (or any of its
 * subtypes), Boolean, Enum, or Date (or any of its subtypes)
 *
 * @author Carl Harris
 */
public interface ValueTypeConverter {

  /**
   * Tests whether this converter supports a given model type.
   * @param type model type
   * @return {@code true} if this converter can transform between an instance of
   *    {@code type} and an instance of type {@code V}
   */
  boolean supports(Class<?> type);

  /**
   * Gets the model type understood by this converter.
   * @return view model type which must be one of the data types supported by
   *   the {@link Coerce} utility.
   */
  Class<?> getType();

  /**
   * Converts a model object (generally of a simple type) to a suitable
   * representation for a view node of value type.
   * @param modelValue the subject model object (never {@code null})
   * @param context view context
   * @return view value
   * @throws Exception if conversion fails
   */
  Object toViewValue(Object modelValue, ViewContext context) throws Exception;

  /**
   * Converts a value from a view into a model object.
   * @param viewValue value node representation (never {@code null})
   * @param context view context
   * @return model value
   * @throws Exception if conversion fails
   */
  Object toModelValue(Object viewValue, ViewContext context) throws Exception;

}

