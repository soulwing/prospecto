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
package org.soulwing.prospecto.demo.jaxrs.views;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.api.listener.ViewNodeEntityListener;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.demo.jaxrs.domain.AbstractEntity;

/**
 * TODO: DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
@ApplicationScoped
public class JpaViewNodeEntityListener implements ViewNodeEntityListener {

  private static final Logger logger =
      LoggerFactory.getLogger(JpaViewNodeEntityListener.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void entityCreated(ViewNodePropertyEvent event) {
    if (event.getValue() instanceof AbstractEntity) {
      logger.debug("persisting entity {}", event.getValue());
      entityManager.persist(event.getValue());
    }
  }

  @Override
  public void entityDiscarded(ViewNodePropertyEvent event) {
    if (event.getValue() instanceof AbstractEntity) {
      logger.debug("removing entity {}", event.getValue());
      entityManager.remove(event.getValue());
    }
  }

}
