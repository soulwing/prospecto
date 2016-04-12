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
import org.soulwing.prospecto.demo.jaxrs.domain.Contact;
import org.soulwing.prospecto.demo.jaxrs.views.ContactViews;

/**
 * A {@link ContactService} implemented as an injectable bean
 *
 * @author Carl Harris
 */
@Transactional
@ApplicationScoped
public class ContactServiceBean extends EntityServiceBase<Contact>
    implements ContactService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private ViewContext viewContext;

  public ContactServiceBean() {
    super(Contact.class, ContactViews.CONTACT_DETAIL);
  }

  @PostConstruct
  public void init() {
    setEntityManager(entityManager);
    setViewContext(viewContext);
  }

  @Override
  public View findAllContacts() {
    return ContactViews.CONTACT_LIST.generateView(
        entityManager.createNamedQuery("findAllContacts").getResultList(),
        viewContext);
  }

  @Override
  public View findContactById(Long id) throws NoSuchEntityException {
    return findById(id);
  }

  @Override
  public Object createContact(View contactView) {
    return create(contactView);
  }

  @Override
  public View updateContact(Long id, View contactView)
      throws NoSuchEntityException, UpdateConflictException {
    return update(id, contactView);
  }

}
