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
import org.soulwing.prospecto.demo.jaxrs.domain.Contact;
import org.soulwing.prospecto.demo.jaxrs.views.PersonViews;

/**
 * A {@link PersonService} implemented as an injectable bean
 *
 * @author Carl Harris
 */
@Transactional
@ApplicationScoped
public class PersonServiceBean implements PersonService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private ViewContext viewContext;

  @Override
  public View createContact(View contactView) {
    final ModelEditor editor = PersonViews.CONTACT_DETAIL.generateEditor(
        contactView, viewContext);
    final Contact contact = (Contact) editor.create();
    entityManager.persist(contact);
    entityManager.flush();
    return PersonViews.CONTACT_DETAIL.generateView(contact, viewContext);
  }

  @Override
  public View findContactById(Long id) throws NoSuchEntityException {
    final Contact contact = entityManager.find(Contact.class, id);
    if (contact == null) {
      throw new NoSuchEntityException(Contact.class, id);
    }

    return PersonViews.CONTACT_DETAIL.generateView(contact, viewContext);
  }

  @Override
  public View updateContact(Long id, View contactView)
      throws NoSuchEntityException, UpdateConflictException {
    Contact contact = entityManager.find(Contact.class, id);
    if (contact == null) {
      throw new NoSuchEntityException(Contact.class, id);
    }

    final ModelEditor editor = PersonViews.CONTACT_DETAIL.generateEditor(
        contactView, viewContext);
    editor.update(contact);
    entityManager.clear();

    try {
      contact = entityManager.merge(contact);
      entityManager.flush();
      return PersonViews.CONTACT_DETAIL.generateView(contact, viewContext);
    }
    catch (OptimisticLockException ex) {
      throw new UpdateConflictException(Contact.class, id, ex);
    }
  }

}
