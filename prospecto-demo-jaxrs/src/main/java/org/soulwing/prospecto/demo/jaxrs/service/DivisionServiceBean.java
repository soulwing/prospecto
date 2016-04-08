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

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.domain.Division;
import org.soulwing.prospecto.demo.jaxrs.domain.League;
import org.soulwing.prospecto.demo.jaxrs.views.DivisionViews;

/**
 * A {@link DivisionService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
@Transactional
public class DivisionServiceBean implements DivisionService {

  @Inject
  private ViewContext viewContext;

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public View findDivisionById(Long id) throws NoSuchEntityException {
    final Division division = entityManager.find(Division.class, id);
    if (division == null) {
      throw new NoSuchEntityException(League.class, id);
    }

    return DivisionViews.DIVISION_DETAIL.generateView(division, viewContext);
  }

  @Override
  public View updateDivision(Long id, View divisionView)
      throws NoSuchEntityException, UpdateConflictException {
    Division division = entityManager.find(Division.class, id);
    if (division == null) {
      throw new NoSuchEntityException(League.class, id);
    }

    final ViewApplicator editor = DivisionViews.DIVISION_DETAIL.createApplicator(
        divisionView, viewContext);
    editor.update(division);
    division.setId(id);
    entityManager.clear();

    try {
      division = entityManager.merge(division);
      entityManager.flush();
      return DivisionViews.DIVISION_DETAIL.generateView(division, viewContext);
    }
    catch (OptimisticLockException ex) {
      throw new UpdateConflictException(League.class, id, ex);
    }
  }

}
