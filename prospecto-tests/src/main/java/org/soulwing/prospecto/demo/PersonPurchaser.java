/*
 * File created on Mar 16, 2016
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

/**
 * DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class PersonPurchaser implements Purchaser {

  private Long id;

  private String surname;

  private String givenName;

  /**
   * Gets the {@code id} property.
   * @return property value
   */
  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

}
