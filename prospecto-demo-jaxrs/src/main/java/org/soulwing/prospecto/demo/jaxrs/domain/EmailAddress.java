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

import java.io.Serializable;
import java.util.Objects;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * A value type that represents an RFC-822 email address.
 *
 * @author Carl Harris
 */
public final class EmailAddress implements Serializable, Cloneable,
    Comparable<EmailAddress> {

  private String value;

  private EmailAddress(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof EmailAddress)) return false;
    return Objects.equals(this.value, ((EmailAddress) obj).value);
  }

  @Override
  public EmailAddress clone() {
    try {
      return (EmailAddress) super.clone();
    }
    catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public int compareTo(EmailAddress other) {
    return this.value.compareTo(other.value);
  }

  @Override
  public String toString() {
    return value;
  }

  /**
   * Constructs a new instance from the given string representation.
   * @param value RFC-822 email address string
   * @return email address instance
   */
  public static EmailAddress valueOf(String value) {
    if (value == null) {
      throw new NullPointerException("value is required");
    }
    if (value.trim().isEmpty()) {
      throw new IllegalArgumentException("an empty string is not allowed");
    }
    try {
      InternetAddress address = new InternetAddress(value);
      return new EmailAddress(address.toString());
    }
    catch (AddressException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

}
