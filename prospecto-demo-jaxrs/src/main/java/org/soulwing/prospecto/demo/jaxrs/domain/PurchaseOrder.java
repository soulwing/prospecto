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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A purchase order entity.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "purchase_order")
@Access(AccessType.FIELD)
public class PurchaseOrder extends AbstractEntity {

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_date")
  private Date creationDate = new Date();

  @Temporal(TemporalType.DATE)
  @Column(name = "due_date")
  private Date dueDate;

  @ManyToOne(fetch = FetchType.LAZY)
  private Vendor vendor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ordered_by")
  private Person orderedBy;

  @Column(length = 1024)
  private String comment;

  @OrderBy("lineNumber ASC")
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      mappedBy = "order")
  private List<PurchaseItem> items = new ArrayList<>();

  /**
   * Gets the {@code creationDate} property.
   * @return property value
   */
  public Date getCreationDate() {
    return creationDate;
  }

  /**
   * Sets the {@code creationDate} property.
   * @param creationDate the property value to set
   */
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * Gets the {@code dueDate} property.
   * @return property value
   */
  public Date getDueDate() {
    return dueDate;
  }

  /**
   * Sets the {@code dueDate} property.
   * @param dueDate the property value to set
   */
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  /**
   * Gets the {@code vendor} property.
   * @return property value
   */
  public Vendor getVendor() {
    return vendor;
  }

  /**
   * Sets the {@code vendor} property.
   * @param vendor the property value to set
   */
  public void setVendor(Vendor vendor) {
    this.vendor = vendor;
  }

  /**
   * Gets the {@code orderedBy} property.
   * @return property value
   */
  public Person getOrderedBy() {
    return orderedBy;
  }

  /**
   * Sets the {@code orderedBy} property.
   * @param orderedBy the property value to set
   */
  public void setOrderedBy(Person orderedBy) {
    this.orderedBy = orderedBy;
  }

  /**
   * Gets the {@code comment} property.
   * @return property value
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the {@code comment} property.
   * @param comment the property value to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Gets the {@code items} property.
   * @return property value
   */
  public Collection<PurchaseItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Adds a line item to this order.
   * @param item the item to add
   */
  public void addItem(PurchaseItem item) {
    items.add(item);
    item.setOrder(this);
    item.setLineNumber(items.size());
  }

  /**
   * Removes a line item from this order.
   * @param item the item to remove
   */
  public void removeItem(PurchaseItem item) {
    if (items.remove(item)) {
      item.setOrder(null);
    }
  }

  public BigDecimal getTotal() {
    BigDecimal total = BigDecimal.ZERO;
    for (final PurchaseItem item : getItems()) {
      total = total.add(item.getPrice());
    }
    return total;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof PurchaseOrder)) return false;
    return super.equals(obj);
  }

  /**
   * A builder that produces a {@link PurchaseOrder}.
   */
  public static class Builder {

    private PurchaseOrder order = new PurchaseOrder();

    public static Builder with() {
      return new Builder();
    }

    /**
     * Configures the {@code dueDate} property
     * @param dueDate the property value to set
     * @return this builder
     */
    public Builder dueDate(Date dueDate) {
      order.setDueDate(dueDate);
      return this;
    }

    /**
     * Configures the {@code vendor} property
     * @param vendor the property value to set
     * @return this builder
     */
    public Builder vendor(Vendor vendor) {
      order.setVendor(vendor);
      return this;
    }

    /**
     * Configures the {@code orderedBy} property
     * @param orderedBy the property value to set
     * @return this builder
     */
    public Builder orderedBy(Person orderedBy) {
      order.setOrderedBy(orderedBy);
      return this;
    }

    /**
     * Configures the {@code comment} property
     * @param comment the property value to set
     * @return this builder
     */
    public Builder comment(String comment) {
      order.setComment(comment);
      return this;
    }

    /**
     * Creates a builder for a line item.
     * @return line item builder
     */
    public PurchaseItem.Builder item() {
      return new PurchaseItem.Builder(order, this);
    }

    /**
     * Creates the entity.
     * @return entity instance
     */
    public PurchaseOrder build() {
      return order;
    }

  }

}
