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
package org.soulwing.prospecto.demo.jaxrs.views;

import java.util.Iterator;

import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.collection.CollectionManager;
import org.soulwing.prospecto.demo.jaxrs.domain.Division;
import org.soulwing.prospecto.demo.jaxrs.domain.League;
import org.soulwing.prospecto.demo.jaxrs.domain.Team;

/**
 * A {@link CollectionManager} that manages the relationship between
 * a {@link League} and its {@link Division} elements.
 *
 * @author Carl Harris
 */
public class DivisionTeamCollectionManager
    extends AbstractEntityCollectionManager<Division, Team> {

  @Override
  public boolean supports(Class<?> ownerClass, Class<?> elementClass) {
    return Division.class.isAssignableFrom(ownerClass)
        && Team.class.isAssignableFrom(elementClass);
  }

  @Override
  protected Iterator<Team> elementIterator(Division owner) {
    return owner.getTeams().iterator();
  }

  @Override
  public Team newElement(Division owner, ViewEntity elementEntity)
      throws Exception {
    return null;
  }

  @Override
  public void add(Division division, Team team) throws Exception {
    division.addTeam(team);
  }

  @Override
  public void remove(Division division, Team team) throws Exception {
    division.removeTeam(team);
  }

}
