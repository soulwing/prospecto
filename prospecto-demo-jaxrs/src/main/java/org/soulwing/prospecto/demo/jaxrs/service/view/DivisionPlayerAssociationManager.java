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
import org.soulwing.prospecto.demo.jaxrs.domain.Player;

/**
 * A {@link ToManyAssociationManager} that manages the relationship between
 * a {@link Division} and its {@link Player} elements.
 *
 * @author Carl Harris
 */
class DivisionPlayerAssociationManager
    extends AbstractEntityCollectionAssociationManager<Division, Player> {

  static final DivisionPlayerAssociationManager INSTANCE =
      new DivisionPlayerAssociationManager();

  private DivisionPlayerAssociationManager() {}

  @Override
  public boolean supports(AssociationDescriptor descriptor) {
    return Division.class.isAssignableFrom(descriptor.getOwnerType())
        && Player.class.isAssignableFrom(descriptor.getAssociateType());
  }

  @Override
  public boolean add(Division division, Player player) throws Exception {
    return division.addPlayer(player);
  }

  @Override
  public boolean remove(Division division, Player player) throws Exception {
    return division.removePlayer(player);
  }

  @Override
  public void clear(Division division) throws Exception {
    for (final Player player : division.getPlayers()) {
      player.setDivision(null);
    }
    division.getPlayers().clear();
  }

  @Override
  protected Collection<Player> getAssociates(Division division)
      throws Exception {
    return division.getPlayers();
  }

}
