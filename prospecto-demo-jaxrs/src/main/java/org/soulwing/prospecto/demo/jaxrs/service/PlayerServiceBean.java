/*
 * File created on Apr 11, 2016
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
package org.soulwing.prospecto.demo.jaxrs.service;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.domain.Division;
import org.soulwing.prospecto.demo.jaxrs.domain.Player;
import org.soulwing.prospecto.demo.jaxrs.views.PlayerViews;

/**
 * A {@link PlayerService} implemented as an injectable bean
 *
 * @author Carl Harris
 */
@Transactional
@ApplicationScoped
public class PlayerServiceBean extends EntityServiceBase<Player>
    implements PlayerService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private ViewContext viewContext;

  public PlayerServiceBean() {
    super(Player.class, PlayerViews.PLAYER_DETAIL);
  }

  @PostConstruct
  public void init() {
    setEntityManager(entityManager);
    setViewContext(viewContext);
  }

  @Override
  public View findPlayerById(Long id) throws NoSuchEntityException {
    return findById(id);
  }

  @Override
  public Object createPlayer(Long divisionId, View playerView) {
    final Player player = newEntity(playerView);
    player.setDivision(entityManager.getReference(Division.class, divisionId));
    entityManager.persist(player);
    entityManager.flush();
    return player.getId();
  }

  @Override
  public View updatePlayer(Long id, View playerView)
      throws NoSuchEntityException, UpdateConflictException {
    return update(id, playerView);
  }

}
