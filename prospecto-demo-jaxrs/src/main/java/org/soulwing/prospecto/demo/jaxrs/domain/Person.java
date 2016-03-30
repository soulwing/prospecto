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

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * An entity that describes a person.
 *
 * @author Carl Harris
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public class Person extends AbstractEntity {

  @Column(nullable = false)
  private Token surname;

  @Column(name = "given_names", nullable = false)
  private TokenList givenNames;

  @Column(name = "preferred_name")
  private Token preferredName;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  /**
   * Gets the {@code surname} property.
   * @return property value
   */
  public Token getSurname() {
    return surname;
  }

  /**
   * Sets the {@code surname} property.
   * @param surname the property value to set
   */
  public void setSurname(Token surname) {
    this.surname = surname;
  }

  /**
   * Gets the {@code givenNames} property.
   * @return property value
   */
  public TokenList getGivenNames() {
    return givenNames;
  }

  /**
   * Sets the {@code givenNames} property.
   * @param givenNames the property value to set
   */
  public void setGivenNames(TokenList givenNames) {
    this.givenNames = givenNames;
  }

  /**
   * Gets the {@code preferredName} property.
   * @return property value
   */
  public Token getPreferredName() {
    return preferredName;
  }

  /**
   * Sets the {@code preferredName} property.
   * @param preferredName the property value to set
   */
  public void setPreferredName(Token preferredName) {
    this.preferredName = preferredName;
  }

  /**
   * Gets the {@code gender} property.
   * @return property value
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * Sets the {@code gender} property.
   * @param gender the property value to set
   */
  public void setGender(Gender gender) {
    this.gender = gender;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Person)) return false;
    return super.equals(obj);
  }

}
