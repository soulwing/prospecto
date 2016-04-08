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
 * holds an instance of a polymorphic type. It has a <em>value</em> that
 * provides an object that is used to identify the subtype represented in the
 * node.
 *
 * @author Carl Harris
 */
public class Discriminator {

  private final Object value;

  /**
   * Constructs a new discriminator using the default name and the specified
   * value.
   * @param value discriminator value
   */
  public Discriminator(Object value) {
    this.value = value;
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
