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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * An embeddable type that contains health insurance details.
 *
 * @author Carl Harris
 */
@Embeddable
public class InsuranceInfo implements Serializable {

  @Column(name = "insurance_provider")
  private String provider;

  @Column(name = "insurance_policy_number")
  private String policyNumber;

  /**
   * Gets the {@code provider} property.
   * @return property value
   */
  public String getProvider() {
    return provider;
  }

  /**
   * Sets the {@code provider} property.
   * @param provider the property value to set
   */
  public void setProvider(String provider) {
    this.provider = provider;
  }

  /**
   * Gets the {@code policyNumber} property.
   * @return property value
   */
  public String getPolicyNumber() {
    return policyNumber;
  }

  /**
   * Sets the {@code policyNumber} property.
   * @param policyNumber the property value to set
   */
  public void setPolicyNumber(String policyNumber) {
    this.policyNumber = policyNumber;
  }

}
