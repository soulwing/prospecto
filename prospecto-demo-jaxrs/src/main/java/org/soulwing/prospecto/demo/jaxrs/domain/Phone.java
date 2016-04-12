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
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * An entity that represents a phone number.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "phone")
@Access(AccessType.FIELD)
public class Phone extends AbstractEntity {

  private String label;

  @Column(name = "phone_number")
  private String number;

  @Column(name = "text_enabled")
  private boolean textEnabled;

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public boolean isTextEnabled() {
    return textEnabled;
  }

  public void setTextEnabled(boolean textEnabled) {
    this.textEnabled = textEnabled;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof Phone && super.equals(obj);
  }

}
