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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
public class Player extends AbstractPerson {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Division division;

  @Temporal(TemporalType.DATE)
  @Column(name = "birth_date", nullable = false)
  private Date birthDate;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "residence_address_id")
  private PhysicalAddress residenceAddress = new PhysicalAddress();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      orphanRemoval = true)
  @JoinColumn(name = "player_id")
  @OrderBy("relationship")
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

  public Division getDivision() {
    return division;
  }

  public void setDivision(Division division) {
    this.division = division;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public PhysicalAddress getResidenceAddress() {
    return residenceAddress;
  }

  public Set<Parent> getParents() {
    return parents;
  }

  public boolean addParent(Parent parent) {
    return parents.add(parent);
  }

  public boolean removeParent(Parent parent) {
    return parents.remove(parent);
  }

  public MedicalInfo getMedicalInfo() {
    return medicalInfo;
  }

  public void setMedicalInfo(MedicalInfo medicalInfo) {
    this.medicalInfo = medicalInfo;
  }

  public UniformInfo getUniformInfo() {
    return uniformInfo;
  }

  public void setUniformInfo(UniformInfo uniformInfo) {
    this.uniformInfo = uniformInfo;
  }

  public Orientation getBattingOrientation() {
    return battingOrientation;
  }

  public void setBattingOrientation(Orientation battingOrientation) {
    this.battingOrientation = battingOrientation;
  }

  public Orientation getThrowingOrientation() {
    return throwingOrientation;
  }

  public void setThrowingOrientation(Orientation throwingOrientation) {
    this.throwingOrientation = throwingOrientation;
  }

}
