/*
 * File created on Mar 29, 2016
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable containing information about a player's uniform.
 *
 * @author Carl Harris
 */
@Embeddable
public class UniformInfo implements Serializable {

  @Column(name = "jersey_size")
  private Size jerseySize;

  @Column(name = "preferred_jersey_numbers")
  private TokenList preferredNumbers = TokenList.empty();

  /**
   * Gets the {@code jerseySize} property.
   * @return property value
   */
  public Size getJerseySize() {
    return jerseySize;
  }

  /**
   * Sets the {@code jerseySize} property.
   * @param size the property value to set
   */
  public void setJerseySize(Size size) {
    this.jerseySize = size;
  }

  /**
   * Gets the {@code preferredNumbers} property.
   * @return property value
   */
  public TokenList getPreferredNumbers() {
    return preferredNumbers;
  }

  /**
   * Sets the {@code preferredNumbers} property.
   * @param preferredNumbers the property value to set
   */
  public void setPreferredNumbers(TokenList preferredNumbers) {
    this.preferredNumbers = preferredNumbers;
  }

}
