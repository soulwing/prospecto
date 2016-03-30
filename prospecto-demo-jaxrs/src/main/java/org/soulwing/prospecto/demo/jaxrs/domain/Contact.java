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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * An entity that describes an (adult) contact person for a player.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "contact")
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "contact_type",
    discriminatorType = DiscriminatorType.STRING)
public class Contact extends Person {

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "mailing_address_id")
  private PhysicalAddress mailingAddress;

  @Column(name = "email_address")
  private EmailAddress emailAddress;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "contact_id")
  private Set<Phone> phones = new HashSet<>();

  /**
   * Gets the {@code mailingAddress} property.
   * @return property value
   */
  public PhysicalAddress getMailingAddress() {
    return mailingAddress;
  }

  /**
   * Sets the {@code mailingAddress} property.
   * @param mailingAddress the property value to set
   */
  public void setMailingAddress(PhysicalAddress mailingAddress) {
    this.mailingAddress = mailingAddress;
  }

  /**
   * Gets the {@code emailAddress} property.
   * @return property value
   */
  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  /**
   * Sets the {@code emailAddress} property.
   * @param emailAddress the property value to set
   */
  public void setEmailAddress(EmailAddress emailAddress) {
    this.emailAddress = emailAddress;
  }

  /**
   * Gets the {@code phones} property.
   * @return property value
   */
  public Set<Phone> getPhones() {
    return phones;
  }

  /**
   * Sets the {@code phones} property.
   * @param phones the property value to set
   */
  public void setPhones(Set<Phone> phones) {
    this.phones = phones;
  }

}
