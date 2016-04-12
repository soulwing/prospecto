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

import java.util.Collections;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * An entity that represents a player's parent/guardian.
 *
 * @author Carl Harris
 */
@Entity
@Access(AccessType.FIELD)
public class Parent extends AbstractEntity implements Person, ContactInfo {

  public enum Relationship {
    MOTHER,
    FATHER,
    GUARDIAN,
    OTHER
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Contact contact;

  private Relationship relationship;

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  @Override
  public Token getSurname() {
    if (contact == null) return null;
    return contact.getSurname();
  }

  @Override
  public void setSurname(Token surname) {
    contact.setSurname(surname);
  }

  @Override
  public TokenList getGivenNames() {
    if (contact == null) return null;
    return contact.getGivenNames();
  }

  @Override
  public void setGivenNames(TokenList givenNames) {
    contact.setGivenNames(givenNames);
  }

  @Override
  public Token getPreferredName() {
    if (contact == null) return null;
    return contact.getPreferredName();
  }

  @Override
  public void setPreferredName(Token preferredName) {
    contact.setPreferredName(preferredName);
  }

  @Override
  public Gender getGender() {
    if (contact == null) return null;
    return contact.getGender();
  }

  @Override
  public void setGender(Gender gender) {
    contact.setGender(gender);
  }

  @Override
  public PhysicalAddress getMailingAddress() {
    if (contact == null) return null;
    return contact.getMailingAddress();
  }

  @Override
  public void setMailingAddress(PhysicalAddress mailingAddress) {
    contact.setMailingAddress(mailingAddress);
  }

  @Override
  public EmailAddress getEmailAddress() {
    if (contact == null) return null;
    return contact.getEmailAddress();
  }

  @Override
  public void setEmailAddress(EmailAddress emailAddress) {
    contact.setEmailAddress(emailAddress);
  }

  @Override
  public List<Phone> getPhones() {
    if (contact == null) return Collections.emptyList();
    return contact.getPhones();
  }

  @Override
  public void setPhones(List<Phone> phones) {
    contact.setPhones(phones);
  }

  public Relationship getRelationship() {
    return relationship;
  }

  public void setRelationship(Relationship relationship) {
    this.relationship = relationship;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof Parent && super.equals(obj);
  }

}
