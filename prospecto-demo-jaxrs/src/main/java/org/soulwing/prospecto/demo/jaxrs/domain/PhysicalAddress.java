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
 * An entity that represents the address of a geographic location in the
 * United States of America.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "physical_address")
@Access(AccessType.FIELD)
public class PhysicalAddress extends AbstractEntity {

  @Column(name = "street_address")
  private String streetAddress;

  private String municipality;

  private String state;

  @Column(name = "postal_code")
  private String postalCode;

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public String getMunicipality() {
    return municipality;
  }

  /**
   * Sets the {@code municipality} property.
   * @param municipality the property value to set
   */
  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  /**
   * Gets the {@code state} property.
   * @return property value
   */
  public String getState() {
    return state;
  }

  /**
   * Sets the {@code state} property.
   * @param state the property value to set
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Gets the {@code postalCode} property.
   * @return property value
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Sets the {@code postalCode} property.
   * @param postalCode the property value to set
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof PhysicalAddress && super.equals(obj);
  }

}
