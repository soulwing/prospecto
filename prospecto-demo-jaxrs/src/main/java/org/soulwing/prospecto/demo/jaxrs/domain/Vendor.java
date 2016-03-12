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
package org.soulwing.prospecto.demo.jaxrs.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An entity that represents a vendor.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "vendor")
@Access(AccessType.FIELD)
public class Vendor extends AbstractEntity {

  @Column(name = "vendor_name")
  private String name;

  @Column(name = "tax_id")
  private String taxId;

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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Vendor)) return false;
    return super.equals(obj);
  }

  /**
   * A builder that produces a {@link Vendor}.
   */
  public static class Builder {

    private final Vendor vendor = new Vendor();

    public static Builder with() {
      return new Builder();
    }

    /**
     * Configures the {@code name} property
     * @param name the property value to set
     * @return this builder
     */
    public Builder name(String name) {
      vendor.setName(name);
      return this;
    }

    /**
     * Configures the {@code taxId} property
     * @param taxId the property value to set
     * @return this builder
     */
    public Builder taxId(String taxId) {
      vendor.setTaxId(taxId);
      return this;
    }

    /**
     * Creates the entity.
     * @return entity instance
     */
    public Vendor build() {
      return vendor;
    }

  }

}
