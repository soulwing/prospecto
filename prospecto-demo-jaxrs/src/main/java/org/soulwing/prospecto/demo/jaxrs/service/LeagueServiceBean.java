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
package org.soulwing.prospecto.demo.jaxrs.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.domain.League;
import org.soulwing.prospecto.demo.jaxrs.views.LeagueViews;

/**
 * A {@link LeagueService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
@Transactional
public class LeagueServiceBean implements LeagueService {

  @Inject
  private ViewContext viewContext;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public View findAllLeagues() {
    return LeagueViews.LEAGUE_LIST.generateView(
        entityManager.createNamedQuery("findAllLeagues", League.class)
            .getResultList(),
        viewContext);
  }

  @Override
  public View findLeagueById(Long id) throws NoSuchEntityException {
    final League league = entityManager.find(League.class, id);
    if (league == null) {
      throw new NoSuchEntityException(League.class, id);
    }

    return LeagueViews.LEAGUE_DETAIL.generateView(league, viewContext);
  }

  @Override
  public View updateLeague(Long id, View leagueView)
      throws NoSuchEntityException, UpdateConflictException {
    League league = entityManager.find(League.class, id);
    if (league == null) {
      throw new NoSuchEntityException(League.class, id);
    }

    final ModelEditor editor = LeagueViews.LEAGUE_DETAIL.generateEditor(
        leagueView, viewContext);
    editor.update(league);
    league.setId(id);
    entityManager.clear();

    try {
      league = entityManager.merge(league);
      entityManager.flush();
      return LeagueViews.LEAGUE_DETAIL.generateView(league, viewContext);
    }
    catch (OptimisticLockException ex) {
      throw new UpdateConflictException(League.class, id, ex);
    }
  }

}