/*
 * File created on Apr 8, 2016
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

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.demo.jaxrs.domain.AbstractEntity;
import org.soulwing.prospecto.demo.jaxrs.domain.Contact;

/**
 * A base for entity service implementations.
 * @author Carl Harris
 */
public class EntityServiceBase<T extends AbstractEntity>
    implements EntityService<T> {

  private final Class<T> entityClass;
  private final ViewTemplate detailView;

  private ViewContext viewContext;
  private EntityManager entityManager;

  EntityServiceBase(Class<T> entityClass, ViewTemplate detailView) {
    this.entityClass = entityClass;
    this.detailView = detailView;
  }

  @Override
  public View findById(Long id) throws NoSuchEntityException {
    Object entity = entityManager.find(entityClass, id);
    if (entity == null) {
      throw new NoSuchEntityException(entityClass, id);
    }
    return detailView.generateView(entity, viewContext);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T newEntity(View view) {
    return (T) detailView.createApplicator(view, viewContext).create();
  }

  @Override
  public Object create(View view) {
    final T entity = newEntity(view);
    entityManager.persist(entity);
    entityManager.flush();
    return entity.getId();
  }

  @Override
  public View update(Long id, View view) throws UpdateConflictException,
      NoSuchEntityException {

    Object entity = entityManager.find(entityClass, id);
    if (entity == null) {
      throw new NoSuchEntityException(entityClass, id);
    }

    try {
      ViewApplicator applicator = detailView.createApplicator(view, viewContext);
      applicator.update(entity);
      entityManager.detach(entity);
      entity = entityManager.merge(entity);
      entityManager.flush();
      return detailView.generateView(entity, viewContext);
    }
    catch (OptimisticLockException ex) {
      throw new UpdateConflictException(Contact.class, id, ex);
    }
  }

  @Override
  public void delete(Long id) {
    T entity = entityManager.find(entityClass, id);
    if (entity == null) return;
    onDelete(entity);
    entityManager.remove(entity);
    entityManager.flush();
  }

  protected void onDelete(T entity) {
  }

  public ViewContext getViewContext() {
    return viewContext;
  }

  public void setViewContext(ViewContext viewContext) {
    this.viewContext = viewContext;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

}
