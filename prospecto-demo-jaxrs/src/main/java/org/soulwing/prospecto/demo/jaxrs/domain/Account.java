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
 * An entity that represents a financial account.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "account")
@Access(AccessType.FIELD)
public class Account extends AbstractEntity {

  @Column(name = "account_id")
  private String accountId;

  private String description;

  /**
   * Gets the {@code accountId} property.
   * @return property value
   */
  public String getAccountId() {
    return accountId;
  }

  /**
   * Sets the {@code accountId} property.
   * @param accountId the property value to set
   */
  public void setAccountId(String accountId) {
    this.accountId = accountId;
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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Account)) return false;
    return super.equals(obj);
  }

  /**
   * A builder that produces an {@link Account}.
   */
  public static class Builder {

    private final Account account = new Account();

    public static Builder with() {
      return new Builder();
    }

    private Builder() {
    }

    /**
     * Configures the {@code accountId} property
     * @param accountId the property value to set
     * @return this builder
     */
    public Builder accountId(String accountId) {
      account.setAccountId(accountId);
      return this;
    }

    /**
     * Configures the {@code description} property
     * @param description the property value to set
     * @return this builder
     */
    public Builder description(String description) {
      account.setDescription(description);
      return this;
    }

    /**
     * Creates the entity.
     * @return entity instance
     */
    public Account build() {
      return account;
    }

  }

}
