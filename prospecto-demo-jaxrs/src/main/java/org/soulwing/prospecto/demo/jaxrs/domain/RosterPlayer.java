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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * An entity that describes a player in a team roster.
 *
 * @author Carl Harris
 */
@Entity
@Table(name = "roster_player")
@Access(AccessType.FIELD)
public class RosterPlayer extends AbstractEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Player player;

  @Column(name = "jersey_number")
  private Integer jerseyNumber;

  @Enumerated(EnumType.STRING)
  private Position position;

  /**
   * Gets the {@code player} property.
   * @return property value
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Sets the {@code player} property.
   * @param player the property value to set
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Gets the {@code jerseyNumber} property.
   * @return property value
   */
  public Integer getJerseyNumber() {
    return jerseyNumber;
  }

  /**
   * Sets the {@code jerseyNumber} property.
   * @param jerseyNumber the property value to set
   */
  public void setJerseyNumber(Integer jerseyNumber) {
    this.jerseyNumber = jerseyNumber;
  }

  /**
   * Gets the {@code position} property.
   * @return property value
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Sets the {@code position} property.
   * @param position the property value to set
   */
  public void setPosition(Position position) {
    this.position = position;
  }

}
