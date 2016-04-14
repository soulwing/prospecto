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

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OrderColumn;
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
public class Contact extends AbstractPerson implements ContactInfo {

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "mailing_address_id")
  private PhysicalAddress mailingAddress;

  @Column(name = "email_address")
  private EmailAddress emailAddress;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "contact_id")
  @OrderColumn(name = "list_index")
  private List<Phone> phones = new ArrayList<>();

  @Override
  public PhysicalAddress getMailingAddress() {
    return mailingAddress;
  }

  @Override
  public void setMailingAddress(PhysicalAddress mailingAddress) {
    this.mailingAddress = mailingAddress;
  }

  @Override
  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  @Override
  public void setEmailAddress(EmailAddress emailAddress) {
    this.emailAddress = emailAddress;
  }

  @Override
  public List<Phone> getPhones() {
    return phones;
  }

  @Override
  public void setPhones(List<Phone> phones) {
    this.phones = phones;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof Contact && super.equals(obj);
  }


}
