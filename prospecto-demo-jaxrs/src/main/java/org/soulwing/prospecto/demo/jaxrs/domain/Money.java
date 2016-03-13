/*
 * File created on Mar 13, 2016
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * A value type representing an amount of money in some currency.
 *
 * @author Carl Harris
 */
@Embeddable
@Access(AccessType.FIELD)
public class Money implements Serializable {

  public static final Money ZERO = new Money(BigDecimal.ZERO, null);

  private BigDecimal amount = BigDecimal.ZERO;

  @Enumerated(EnumType.STRING)
  private Currency currency = Currency.USD;

  public Money() {
    this(BigDecimal.ZERO, Currency.USD);
  }

  public Money(Currency currency) {
    this(BigDecimal.ZERO, currency);
  }

  public Money(double amount, Currency currency) {
    this(BigDecimal.valueOf(amount), currency);
  }

  public Money(BigDecimal amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  /**
   * Gets the {@code amount} property.
   * @return property value
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * Sets the {@code amount} property.
   * @param amount the property value to set
   */
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  /**
   * Gets the {@code currency} property.
   * @return property value
   */
  public Currency getCurrency() {
    return currency;
  }

  /**
   * Sets the {@code currency} property.
   * @param currency the property value to set
   */
  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Money add(Money money) {
    if (BigDecimal.ZERO.equals(this.getAmount())) return money;
    if (BigDecimal.ZERO.equals(money.getAmount())) return money;
    if (!(money.getCurrency() == this.getCurrency())) {
      throw new IllegalArgumentException("currency mismatch");
    }
    final BigDecimal sum = this.amount.add(money.amount);
    return new Money(sum, this.currency);
  }

  public Money multiply(BigDecimal multiplier) {
    if (BigDecimal.ZERO.equals(multiplier)) return ZERO;
    if (BigDecimal.ZERO.equals(amount)) return ZERO;
    final BigDecimal product = this.amount.multiply(multiplier)
        .setScale(this.currency.getScale(), RoundingMode.HALF_EVEN);
    return new Money(product, this.getCurrency());
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Money)) return false;
    final Money that = (Money) obj;
    return Objects.equals(this.amount, that.amount)
        && Objects.equals(this.currency, that.currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (currency.isPrefix()) {
      sb.append(currency.getSymbol());
    }
    sb.append(amount);
    if (!currency.isPrefix()) {
      sb.append(currency.getSymbol());
    }
    return sb.toString();
  }

}
