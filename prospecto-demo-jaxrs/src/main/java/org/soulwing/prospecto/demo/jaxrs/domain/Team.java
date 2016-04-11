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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * An entity that describes a team in a division.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "team")
@Access(AccessType.FIELD)
public class Team extends AbstractEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Division division;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  private Contact manager;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "team_coach",
      inverseJoinColumns = @JoinColumn(name = "coach_id"))
  private Set<Contact> coaches = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "team_id")
  @OrderBy("position")
  private Set<RosterPlayer> roster = new HashSet<>();

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
   * Gets the {@code name} property.
   * @return property value
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the {@code name} property.
   * @param name the property value to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the {@code manager} property.
   * @return property value
   */
  public Contact getManager() {
    return manager;
  }

  /**
   * Sets the {@code manager} property.
   * @param manager the property value to set
   */
  public void setManager(Contact manager) {
    this.manager = manager;
  }

  /**
   * Gets the {@code coaches} property.
   * @return property value
   */
  public Set<Contact> getCoaches() {
    return coaches;
  }

  /**
   * Gets the {@code roster} property.
   * @return property value
   */
  public Set<RosterPlayer> getRoster() {
    return roster;
  }

  public boolean addPlayer(RosterPlayer player) {
    return roster.add(player);
  }

  public boolean removePlayer(RosterPlayer player) {
    return roster.remove(player);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Team)) return false;
    return super.equals(obj);
  }

}
