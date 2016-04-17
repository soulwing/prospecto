/*
 * File created on Apr 14, 2016
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
package org.soulwing.prospecto.demo.jaxrs.service.view;

import java.util.Collection;

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.demo.jaxrs.domain.Division;
import org.soulwing.prospecto.demo.jaxrs.domain.League;

/**
 * A {@link ToManyAssociationManager} that manages the relationship between
 * a {@link League} and its {@link Division} elements.
 *
 * @author Carl Harris
 */
class LeagueDivisionAssociationManager
    extends AbstractEntityCollectionAssociationManager<League, Division> {

  static final LeagueDivisionAssociationManager INSTANCE =
      new LeagueDivisionAssociationManager();

  private LeagueDivisionAssociationManager() {}

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return League.class.isAssignableFrom(descriptor.getOwnerType())
        && Division.class.isAssignableFrom(descriptor.getAssociateType());
  }

  @Override
  public boolean add(League league, Division division) throws Exception {
    return league.addDivision(division);
  }

  @Override
  public boolean remove(League league, Division division) throws Exception {
    return league.removeDivision(division);
  }

  @Override
  public void clear(League league) throws Exception {
    for (final Division division : league.getDivisions()) {
      division.setLeague(null);
    }
    league.getDivisions().clear();
  }

  @Override
  protected Collection<Division> getAssociates(League league) throws Exception {
    return league.getDivisions();
  }

}
