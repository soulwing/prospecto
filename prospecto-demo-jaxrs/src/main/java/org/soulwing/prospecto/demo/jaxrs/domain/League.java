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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * An entity that describes a league.
 * @author Carl Harris
 */
@Entity
@Table(name = "league")
@Access(AccessType.FIELD)
public class League extends AbstractEntity {

  @Column(nullable = false)
  private String name;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
      mappedBy = "league")
  @OrderBy("ageLimit, name")
  private Set<Division> divisions = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Division> getDivisions() {
    return divisions;
  }

  public boolean addDivision(Division division) {
    division.setLeague(this);
    return divisions.add(division);
  }

  public boolean removeDivision(Division division) {
    final boolean removed = divisions.remove(division);
    if (removed) {
      division.setLeague(null);
    }
    return removed;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this || obj instanceof League && super.equals(obj);
  }

}
