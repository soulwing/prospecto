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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * An entity that represents a person.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "person")
@Access(AccessType.FIELD)
public class Person extends AbstractEntity {

  public enum Type {
    EMPLOYEE,
    CUSTOMER,
    OTHER
  }

  @Enumerated(EnumType.STRING)
  private Type type;

  private String surname;

  @Column(name = "given_name")
  private String givenName;

  /**
   * Gets the {@code type} property.
   * @return property value
   */
  public Type getType() {
    return type;
  }

  /**
   * Sets the {@code type} property.
   * @param type the property value to set
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * Gets the {@code surname} property.
   * @return property value
   */
  public String getSurname() {
    return surname;
  }

  /**
   * Sets the {@code surname} property.
   * @param surname the property value to set
   */
  public void setSurname(String surname) {
    this.surname = surname;
  }

  /**
   * Gets the {@code givenName} property.
   * @return property value
   */
  public String getGivenName() {
    return givenName;
  }

  /**
   * Sets the {@code givenName} property.
   * @param givenName the property value to set
   */
  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getDisplayName() {
    return getGivenName() + " " + getSurname();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Person)) return false;
    return super.equals(obj);
  }

  /**
   * A builder that produces an {@link Person}.
   */
  public static class Builder {
    final Person person = new Person();

    private Builder() {
    }

    public static Builder with() {
      return new Builder();
    }

    /**
     * Configures the {@code type} property
     * @param type the property value to set
     * @return this builder
     */
    public Builder type(Type type) {
      person.setType(type);
      return this;
    }

    /**
     * Configures the {@code surname} property
     * @param surname the property value to set
     * @return this builder
     */
    public Builder surname(String surname) {
      person.setSurname(surname);
      return this;
    }

    /**
     * Configures the {@code givenName} property
     * @param givenName the property value to set
     * @return this builder
     */
    public Builder givenName(String givenName) {
      person.setGivenName(givenName);
      return this;
    }

    /**
     * Creates the entity.
     * @return entity instance
     */
    public Person build() {
      return person;
    }

  }

}
