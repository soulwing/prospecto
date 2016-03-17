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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A demo type representing a purchase order.
 *
 * @author Carl Harris
 */
public class PurchaseOrder {

  public enum FreeOnBoard {
    ORIGIN,
    DESTINATION;
  }

  private Long id;

  private Date creationDate;

  private Date dueDate;

  private Vendor vendor;

  private Purchaser purchaser;

  private String terms;

  private FreeOnBoard fob;

  private List<PurchaseItem> items = new ArrayList<>();

  private List<String> approvers = new ArrayList<>();

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
   * Gets the {@code purchaser} property.
   * @return property value
   */
  public Purchaser getPurchaser() {
    return purchaser;
  }

  /**
   * Sets the {@code purchaser} property.
   * @param purchaser the property value to set
   */
  public void setPurchaser(Purchaser purchaser) {
    this.purchaser = purchaser;
  }

  /**
   * Gets the {@code terms} property.
   * @return property value
   */
  public String getTerms() {
    return terms;
  }

  /**
   * Sets the {@code terms} property.
   * @param terms the property value to set
   */
  public void setTerms(String terms) {
    this.terms = terms;
  }

  /**
   * Gets the {@code fob} property.
   * @return property value
   */
  public FreeOnBoard getFob() {
    return fob;
  }

  /**
   * Sets the {@code fob} property.
   * @param fob the property value to set
   */
  public void setFob(FreeOnBoard fob) {
    this.fob = fob;
  }

  /**
   * Gets the {@code items} property.
   * @return property value
   */
  public List<PurchaseItem> getItems() {
    return items;
  }

  /**
   * Sets the {@code items} property.
   * @param items the property value to set
   */
  public void setItems(List<PurchaseItem> items) {
    this.items = items;
  }

  /**
   * Gets the {@code approvers} property.
   * @return property value
   */
  public List<String> getApprovers() {
    return approvers;
  }

  /**
   * Sets the {@code approvers} property.
   * @param approvers the property value to set
   */
  public void setApprovers(List<String> approvers) {
    this.approvers = approvers;
  }

  public BigDecimal getItemTotal() {
    BigDecimal total = BigDecimal.ZERO;
    for (final PurchaseItem item : items) {
      BigDecimal ext = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
      total = total.add(ext);
    }
    return total;
  }

}
