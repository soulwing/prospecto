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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * An entity that describes a division in a league.
 * @author Carl Harris
 */
@Entity
@Table(name = "division")
@Access(AccessType.FIELD)
public class Division extends AbstractEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  private League league;

  @Column(nullable = false)
  private String name;

  @Column(name = "age_limit")
  private Integer ageLimit;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      mappedBy = "division")
  @OrderBy("name")
  private Set<Team> teams = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      mappedBy = "division")
  @OrderBy("surname, givenNames")
  private Set<Player> players = new HashSet<>();

  /**
   * Gets the {@code league} property.
   * @return property value
   */
  public League getLeague() {
    return league;
  }

  /**
   * Sets the {@code league} property.
   * @param league the property value to set
   */
  public void setLeague(League league) {
    this.league = league;
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
   * Gets the {@code ageLimit} property.
   * @return property value
   */
  public Integer getAgeLimit() {
    return ageLimit;
  }

  /**
   * Sets the {@code ageLimit} property.
   * @param ageLimit the property value to set
   */
  public void setAgeLimit(Integer ageLimit) {
    this.ageLimit = ageLimit;
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

  /**
   * Gets the {@code teams} property.
   * @return property value
   */
  public Set<Team> getTeams() {
    return teams;
  }

  public boolean addTeam(Team team) {
    team.setDivision(this);
    return teams.add(team);
  }

  public boolean removeTeam(Team team) {
    final boolean removed = teams.remove(team);
    if (removed) {
      team.setDivision(null);
    }
    return removed;
  }

  /**
   * Gets the {@code players} property.
   * @return property value
   */
  public Set<Player> getPlayers() {
    return players;
  }

  public int getPlayerCount() {
    return players.size();
  }

  public boolean addPlayer(Player player) {
    player.setDivision(this);
    return players.add(player);
  }

  public boolean removePlayer(Player player) {
    final boolean removed = players.remove(player);
    if (removed) {
      player.setDivision(null);
    }
    return removed;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof Division)) return false;
    return super.equals(obj);
  }

}
