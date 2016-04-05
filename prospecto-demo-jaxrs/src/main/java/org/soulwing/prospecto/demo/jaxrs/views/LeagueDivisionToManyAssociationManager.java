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
public class LeagueDivisionToManyAssociationManager
    extends AbstractEntityToManyAssociationManager<League, Division> {

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return League.class.isAssignableFrom(descriptor.getOwnerType())
        && Division.class.isAssignableFrom(descriptor.getAssociateType());
  }

  @Override
  public Iterator<Division> iterator(League league) {
    return league.getDivisions().iterator();
  }

  @Override
  public int size(League league) throws Exception {
    return league.getDivisions().size();
  }

  @Override
  public void add(League league, Division division) throws Exception {
    league.addDivision(division);
  }

  @Override
  public boolean remove(League league, Division division) throws Exception {
    return league.removeDivision(division);
  }

  @Override
  public void clear(League league) throws Exception {
    league.getDivisions().clear();
  }

}
