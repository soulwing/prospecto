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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * An entity that describes a player.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "player")
@Access(AccessType.FIELD)
public class Player extends Person {

  @ManyToOne(fetch = FetchType.LAZY)
  private Division division;

  @Temporal(TemporalType.DATE)
  @Column(name = "birth_date", nullable = false)
  private Date birthDate;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "residence_address_id")
  private PhysicalAddress residenceAddress = new PhysicalAddress();

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "player_parent",
      inverseJoinColumns = @JoinColumn(name = "parent_id"))
  private Set<Parent> parents = new HashSet<>();

  @Embedded
  private MedicalInfo medicalInfo = new MedicalInfo();

  @Embedded
  private UniformInfo uniformInfo = new UniformInfo();

  @Enumerated(EnumType.STRING)
  @Column(name = "batting_orientation")
  private Orientation battingOrientation;

  @Enumerated(EnumType.STRING)
  @Column(name = "throwing_orientation")
  private Orientation throwingOrientation;

  /**
   * Gets the {@code division} property.
   * @return property value
   */
  public Division getDivision() {
    return division;
  }

  /**
   * Sets the {@code division} property.
   * @param division the property value to set
   */
  public void setDivision(Division division) {
    this.division = division;
  }

  /**
   * Gets the {@code birthDate} property.
   * @return property value
   */
  public Date getBirthDate() {
    return birthDate;
  }

  /**
   * Sets the {@code birthDate} property.
   * @param birthDate the property value to set
   */
  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  /**
   * Gets the {@code residenceAddress} property.
   * @return property value
   */
  public PhysicalAddress getResidenceAddress() {
    return residenceAddress;
  }

  /**
   * Gets the {@code parents} property.
   * @return property value
   */
  public Set<Parent> getParents() {
    return parents;
  }

  public boolean addParent(Parent parent) {
    return parents.add(parent);
  }

  public boolean removeParent(Parent parent) {
    return parents.remove(parent);
  }

  /**
   * Gets the {@code battingOrientation} property.
   * @return property value
   */
  public Orientation getBattingOrientation() {
    return battingOrientation;
  }

  /**
   * Sets the {@code battingOrientation} property.
   * @param battingOrientation the property value to set
   */
  public void setBattingOrientation(Orientation battingOrientation) {
    this.battingOrientation = battingOrientation;
  }

  /**
   * Gets the {@code throwingOrientation} property.
   * @return property value
   */
  public Orientation getThrowingOrientation() {
    return throwingOrientation;
  }

  /**
   * Sets the {@code throwingOrientation} property.
   * @param throwingOrientation the property value to set
   */
  public void setThrowingOrientation(Orientation throwingOrientation) {
    this.throwingOrientation = throwingOrientation;
  }
}
