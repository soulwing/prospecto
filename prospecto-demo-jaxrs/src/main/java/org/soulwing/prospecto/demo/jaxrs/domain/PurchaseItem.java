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

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * An entity that represents a line item on a purchase order.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "purchase_item")
@Access(AccessType.FIELD)
public class PurchaseItem extends AbstractEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  private PurchaseOrder order;

  @Column(name = "line_number")
  private int lineNumber;

  @Column(name = "item_id")
  private String itemId;

  private String description;

  private BigDecimal quantity = BigDecimal.ZERO;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "amount",
          column = @Column(name = "unit_price_amount")),
      @AttributeOverride(name = "currency",
          column = @Column(name = "unit_price_currency"))
  })
  private Money unitPrice = new Money();

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
   * Gets the {@code lineNumber} property.
   * @return property value
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Sets the {@code lineNumber} property.
   * @param lineNumber the property value to set
   */
  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  /**
   * Gets the {@code itemId} property.
   * @return property value
   */
  public String getItemId() {
    return itemId;
  }

  /**
   * Sets the {@code itemId} property.
   * @param itemId the property value to set
   */
  public void setItemId(String itemId) {
    this.itemId = itemId;
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
  public BigDecimal getQuantity() {
    return quantity;
  }

  /**
   * Sets the {@code quantity} property.
   * @param quantity the property value to set
   */
  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  /**
   * Gets the {@code unitPrice} property.
   * @return property value
   */
  public Money getUnitPrice() {
    return unitPrice;
  }

  /**
   * Sets the {@code unitPrice} property.
   * @param unitPrice the property value to set
   */
  public void setUnitPrice(Money unitPrice) {
    this.unitPrice = unitPrice;
  }

  public Money getPrice() {
    return unitPrice.multiply(quantity);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof PurchaseItem)) return false;
    return super.equals(obj);
  }

  /**
   * A builder that produces a {@link PurchaseItem}.
   */
  public static class Builder {

    private final PurchaseItem item = new PurchaseItem();

    private final PurchaseOrder order;

    private final PurchaseOrder.Builder parent;

    public Builder(PurchaseOrder order, PurchaseOrder.Builder parent) {
      this.order = order;
      this.parent = parent;
    }

    /**
     * Configures the {@code itemId} property
     * @param itemId the property value to set
     * @return this builder
     */
    public Builder itemId(String itemId) {
      item.setItemId(itemId);
      return this;
    }

    /**
     * Configures the {@code description} property
     * @param description the property value to set
     * @return this builder
     */
    public Builder description(String description) {
      item.setDescription(description);
      return this;
    }

    /**
     * Configures the {@code quantity} property
     * @param quantity the property value to set
     * @return this builder
     */
    public Builder quantity(BigDecimal quantity) {
      item.setQuantity(quantity);
      return this;
    }

    /**
     * Configures the {@code unitPrice} property
     * @param unitPrice the property value to set
     * @return this builder
     */
    public Builder unitPrice(BigDecimal unitPrice) {
      if (order.getCurrency() == null) {
        throw new IllegalStateException("order currency is not set");
      }
      item.setUnitPrice(new Money(unitPrice, order.getCurrency()));
      return this;
    }

    /**
     * Creates the entity and returns to the parent builder.
     * @return entity instance
     */
    public PurchaseOrder.Builder end() {
      order.addItem(item);
      return parent;
    }

  }

}
