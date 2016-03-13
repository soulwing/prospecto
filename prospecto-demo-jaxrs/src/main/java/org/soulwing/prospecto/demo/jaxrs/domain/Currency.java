/*
 * File created on Mar 13, 2016
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
package org.soulwing.prospecto.demo.jaxrs.domain;

/**
 * A currency descriptor.
 *
 * @author Carl Harris
 */
public enum Currency {


  USD(2, '$', true),
  EUR(2, '\u20A0', false);

  private final int scale;
  private final char symbol;
  private final boolean prefix;

  Currency(int scale, char symbol, boolean prefix) {
    this.scale = scale;
    this.symbol = symbol;
    this.prefix = prefix;
  }

  /**
   * Gets the {@code scale} property.
   * @return property value
   */
  public int getScale() {
    return scale;
  }

  /**
   * Gets the {@code symbol} property.
   * @return property value
   */
  public char getSymbol() {
    return symbol;
  }

  /**
   * Gets the {@code prefix} property.
   * @return property value
   */
  public boolean isPrefix() {
    return prefix;
  }

}
