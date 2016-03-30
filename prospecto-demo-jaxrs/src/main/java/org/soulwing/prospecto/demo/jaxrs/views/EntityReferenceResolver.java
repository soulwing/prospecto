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
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.reference.ReferenceResolver;
import org.soulwing.prospecto.demo.jaxrs.domain.AbstractEntity;

/**
 * A {@link ReferenceResolver} for JPA entities.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class EntityReferenceResolver implements ReferenceResolver {

  private static final Logger logger =
      LoggerFactory.getLogger(EntityReferenceResolver.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public boolean supports(Class<?> type) {
    logger.debug("request to resolve for type {}", type.getSimpleName());
    return AbstractEntity.class.isAssignableFrom(type);
  }

  @Override
  public Object resolve(Class<?> type, ViewEntity reference) {
    Object resolvedValue = entityManager.find(type, reference.get("id"));
    if (resolvedValue != null) {
      logger.debug("resolved entity {}", resolvedValue);
    }
    else {
      logger.debug("failed to resolve value for {} with ID {}",
          type.getSimpleName(), reference.get("id"));
    }
    return resolvedValue;
  }

}
