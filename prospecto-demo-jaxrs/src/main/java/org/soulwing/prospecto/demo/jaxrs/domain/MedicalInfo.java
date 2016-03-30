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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * An embeddable containing medical information for a player.
 *
 * @author Carl Harris
 */
@Embeddable
public class MedicalInfo implements Serializable {

  @Embedded
  private InsuranceInfo insuranceInfo;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "emergency_contact_id")
  private Contact emergencyContact;

  @Column(name = "medical_note", length = 8192)
  private String note;

  /**
   * Gets the {@code insuranceInfo} property.
   * @return property value
   */
  public InsuranceInfo getInsuranceInfo() {
    return insuranceInfo;
  }

  /**
   * Sets the {@code insuranceInfo} property.
   * @param insuranceInfo the property value to set
   */
  public void setInsuranceInfo(InsuranceInfo insuranceInfo) {
    this.insuranceInfo = insuranceInfo;
  }

  /**
   * Gets the {@code emergencyContact} property.
   * @return property value
   */
  public Contact getEmergencyContact() {
    return emergencyContact;
  }

  /**
   * Sets the {@code emergencyContact} property.
   * @param emergencyContact the property value to set
   */
  public void setEmergencyContact(Contact emergencyContact) {
    this.emergencyContact = emergencyContact;
  }

  /**
   * Gets the {@code note} property.
   * @return property value
   */
  public String getNote() {
    return note;
  }

  /**
   * Sets the {@code note} property.
   * @param note the property value to set
   */
  public void setNote(String note) {
    this.note = note;
  }

}
