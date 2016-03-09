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
package org.soulwing.prospecto.demo;

/**
 * A demo type representing a vendor.
 *
 * @author Carl Harris
 */
public class Vendor {

  private Long id;

  private String name;

  private String taxId;

  /**
   * Gets the {@code id} property.
   * @return property value
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the {@code id} property.
   * @param id the property value to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the {@code name} property.
   * @return property value
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the {@code name} property.
   * @param name the property value to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the {@code taxId} property.
   * @return property value
   */
  public String getTaxId() {
    return taxId;
  }

  /**
   * Sets the {@code taxId} property.
   * @param taxId the property value to set
   */
  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }
}
