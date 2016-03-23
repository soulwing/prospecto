/*
 * File created on Mar 16, 2016
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
package org.soulwing.prospecto.api.discriminator;

/**
 * An immutable value holder that describes a subtype discriminator.
 * <p>
 * A discriminator is used in an object or array-of-objects node when the node
 * holds an instance of a polymorphic type. It has a <em>name</em> that is
 * used to identify the discriminator in the view and a <em>value</em> that
 * provides an object that is used to identify the subtype represented in the
 * node.
 * <p>
 * Some textual representations (notably XML) have an existing mechanism to
 * distinguish subtypes. When a view is transformed into such a textual
 * representation, the discriminator's <em>name</em> property may be ignored
 * in favor of using a representation-specific mechanism. For example, in XML,
 * the {@code xsi:type} attribute might be used instead of the given name.
 * Alternatively, an XML representation might use schema substitution groups
 * in which case the discriminator's <em>value</em> is used as the surrounding
 * element name for the node.
 *
 * @author Carl Harris
 */
public class Discriminator {

  public static final String DEFAULT_NAME = "type";

  private final String name;
  private final Object value;

  /**
   * Constructs a new discriminator using the default name and the specified
   * value.
   * @param value discriminator value
   */
  public Discriminator(Object value) {
    this(DEFAULT_NAME, value);
  }

  /**
   * Constructs a new discriminator using the specified name and value.
   * @param name discriminator name; not used by some textual representations
   *    (e.g. XML, which has a predefined {@code xsi:type} attribute for this
   *    purpose
   * @param value discriminator value
   */
  public Discriminator(String name, Object value) {
    if (name == null || value == null) {
      throw new NullPointerException("name and value are required");
    }
    this.name = name;
    this.value = value;
  }

  /**
   * Gets the name of the discriminator that will appear in a view.
   * @return discriminator name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the {@code value} property.
   * @return property value
   */
  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }

}
