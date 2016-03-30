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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An entity that represents a phone number.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "phone")
@Access(AccessType.FIELD)
public class Phone extends AbstractEntity {

  private String label;

  @Column(name = "phone_number")
  private String number;

  @Column(name = "text_enabled")
  private boolean textEnabled;

  /**
   * Gets the {@code label} property.
   * @return property value
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the {@code label} property.
   * @param label the property value to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Gets the {@code number} property.
   * @return property value
   */
  public String getNumber() {
    return number;
  }

  /**
   * Sets the {@code number} property.
   * @param number the property value to set
   */
  public void setNumber(String number) {
    this.number = number;
  }

  /**
   * Gets the {@code textEnabled} property.
   * @return property value
   */
  public boolean isTextEnabled() {
    return textEnabled;
  }

  /**
   * Sets the {@code textEnabled} property.
   * @param textEnabled the property value to set
   */
  public void setTextEnabled(boolean textEnabled) {
    this.textEnabled = textEnabled;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Phone)) return false;
    return super.equals(obj);
  }

}
