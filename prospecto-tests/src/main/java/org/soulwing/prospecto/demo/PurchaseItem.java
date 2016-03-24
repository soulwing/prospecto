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

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A demo type representing a line item on a purchase order.
 *
 * @author Carl Harris
 */
public class PurchaseItem {

  private Long id;

  private PurchaseOrder order;

  private String description;

  private Integer quantity;

  private BigDecimal unitPrice;

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
   * Gets the {@code order} property.
   * @return property value
   */
  public PurchaseOrder getOrder() {
    return order;
  }

  /**
   * Sets the {@code order} property.
   * @param order the property value to set
   */
  public void setOrder(PurchaseOrder order) {
    this.order = order;
  }

  /**
   * Gets the {@code description} property.
   * @return property value
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the {@code description} property.
   * @param description the property value to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the {@code quantity} property.
   * @return property value
   */
  public Integer getQuantity() {
    return quantity;
  }

  /**
   * Sets the {@code quantity} property.
   * @param quantity the property value to set
   */
  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  /**
   * Gets the {@code unitPrice} property.
   * @return property value
   */
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  /**
   * Sets the {@code unitPrice} property.
   * @param unitPrice the property value to set
   */
  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (id == null) return false;
    if (!(obj instanceof PurchaseItem)) return false;
    return id.equals(((PurchaseItem) obj).id);
  }

}
