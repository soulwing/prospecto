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
import org.soulwing.prospecto.demo.jaxrs.domain.Player;
import org.soulwing.prospecto.demo.jaxrs.views.PersonViews;

/**
 * A {@link PersonService} implemented as an injectable bean
 *
 * @author Carl Harris
 */
@Transactional
@ApplicationScoped
public class PersonServiceBean implements PersonService {

  private final EntityService contactService =
      new EntityServiceBase<>(Contact.class, PersonViews.CONTACT_DETAIL);

  private final EntityService playerService =
      new EntityServiceBase<>(Player.class, PersonViews.PLAYER_DETAIL);

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private ViewContext viewContext;

  @PostConstruct
  public void init() {
    contactService.setEntityManager(entityManager);
    contactService.setViewContext(viewContext);
    playerService.setEntityManager(entityManager);
    playerService.setViewContext(viewContext);
  }

  @Override
  public View findAllContacts() {
    return PersonViews.CONTACT_LIST.generateView(
        entityManager.createNamedQuery("findAllContacts").getResultList(),
        viewContext);
  }

  @Override
  public View findContactById(Long id) throws NoSuchEntityException {
    return contactService.findById(id);
  }

  @Override
  public Object createContact(View contactView) {
    return contactService.create(contactView);
  }

  @Override
  public View updateContact(Long id, View contactView)
      throws NoSuchEntityException, UpdateConflictException {
    return contactService.update(id, contactView);
  }

  @Override
  public View findPlayerById(Long id) throws NoSuchEntityException {
    return playerService.findById(id);
  }

  @Override
  public View updatePlayer(Long id, View playerView)
      throws NoSuchEntityException, UpdateConflictException {
    return playerService.update(id, playerView);
  }

}
