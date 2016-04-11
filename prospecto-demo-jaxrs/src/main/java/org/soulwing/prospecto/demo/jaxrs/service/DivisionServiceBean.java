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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.domain.Division;
import org.soulwing.prospecto.demo.jaxrs.views.DivisionViews;

/**
 * A {@link DivisionService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
@Transactional
public class DivisionServiceBean extends EntityServiceBase<Division>
    implements DivisionService {

  public DivisionServiceBean() {
    super(Division.class, DivisionViews.DIVISION_DETAIL);
  }

  @Inject
  private ViewContext viewContext;

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void init() {
    setViewContext(viewContext);
    setEntityManager(entityManager);
  }

  @Override
  public View findDivisionById(Long id) throws NoSuchEntityException {
    return findById(id);
  }

  @Override
  public Object createDivision(View divisionView) {
    return create(divisionView);
  }

  @Override
  public View updateDivision(Long id, View divisionView)
      throws NoSuchEntityException, UpdateConflictException {
    return update(id, divisionView);
  }

  @Override
  public void deleteDivision(Long id) {
    delete(id);
  }

  @Override
  protected void onDelete(Division division) {
    division.getLeague().removeDivision(division);
  }

}
