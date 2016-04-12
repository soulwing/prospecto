/*
 * File created on Apr 9, 2016
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
import org.soulwing.prospecto.demo.jaxrs.domain.Team;
import org.soulwing.prospecto.demo.jaxrs.views.TeamViews;

/**
 * A {@link TeamService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
@Transactional
public class TeamServiceBean extends EntityServiceBase<Team>
    implements TeamService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private ViewContext viewContext;

  public TeamServiceBean() {
    super(Team.class, TeamViews.TEAM_DETAIL);
  }

  @PostConstruct
  public void init() {
    setEntityManager(entityManager);
    setViewContext(viewContext);
  }

  @Override
  public Object createTeam(Long divisionId, View teamView) {
    entityListener.begin();
    try {
      final Team team = newEntity(teamView);
      team.setDivision(entityManager.getReference(Division.class, divisionId));
      entityListener.apply(entityManager);
      entityManager.persist(team);
      entityManager.flush();
      return team.getId();
    }
    finally {
      entityListener.end();
    }
  }

  @Override
  public View findTeamById(Long id)
      throws NoSuchEntityException {
    return findById(id);
  }

  @Override
  public View updateTeam(Long id, View teamView)
      throws NoSuchEntityException, UpdateConflictException {
    return update(id, teamView);
  }

  @Override
  public void deleteTeam(Long id) {
    delete(id);
  }

  @Override
  protected void onDelete(Team team) {
    team.getDivision().removeTeam(team);
  }

}
