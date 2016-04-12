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

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.api.listener.ViewNodeEntityListener;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.demo.jaxrs.domain.AbstractEntity;

/**
 * A {@link ViewNodeEntityListener} that persists and removes JPA entities.
 *
 * @author Carl Harris
 */
class JpaViewNodeEntityListener implements ViewNodeEntityListener {

  private static final Logger logger =
      LoggerFactory.getLogger(JpaViewNodeEntityListener.class);

  private static final ThreadLocal<TransactionalViewNodeEntityListener> delegate =
      new ThreadLocal<>();

  static final JpaViewNodeEntityListener INSTANCE =
      new JpaViewNodeEntityListener();

  private JpaViewNodeEntityListener() {}

  @Override
  public void entityCreated(ViewNodePropertyEvent event) {
    if (delegate.get() == null) {
      delegate.set(new NullViewNodeEntityListener());
    }
    delegate.get().entityCreated(event);
  }

  @Override
  public void entityDiscarded(ViewNodePropertyEvent event) {
    if (delegate.get() == null) {
      delegate.set(new NullViewNodeEntityListener());
    }
    delegate.get().entityDiscarded(event);
  }

  void begin() {
    delegate.set(new InnerViewNodeEntityListener());
  }

  void apply(EntityManager entityManager) {
    delegate.get().apply(entityManager);
  }

  void end() {
    delegate.remove();
  }

  private interface TransactionalViewNodeEntityListener
      extends ViewNodeEntityListener {

    void apply(EntityManager entityManager);

  }

  private static class InnerViewNodeEntityListener
      implements TransactionalViewNodeEntityListener {

    private final List<AbstractEntity> entitiesToRemove = new LinkedList<>();
    private final List<AbstractEntity> entitiesToPersist = new LinkedList<>();

    @Override
    public void entityCreated(ViewNodePropertyEvent event) {
      final Object value = event.getValue();
      if (value instanceof AbstractEntity) {
        logger.debug("will persist {} entity associated with {} entity",
            event.getValue().getClass().getSimpleName(),
            event.getModel().getClass().getSimpleName());
        entitiesToPersist.add((AbstractEntity) value);
      }
    }

    @Override
    public void entityDiscarded(ViewNodePropertyEvent event) {
      final Object value = event.getValue();
      if (value instanceof AbstractEntity) {
        logger.debug("will remove {} entity associated with {} entity",
            event.getValue().getClass().getSimpleName(),
            event.getModel().getClass().getSimpleName());
        entitiesToRemove.add((AbstractEntity) value);
      }
    }

    @Override
    public void apply(EntityManager entityManager) {
      for (final AbstractEntity entity : entitiesToRemove) {
        entityManager.remove(entity);
      }
      for (final AbstractEntity entity : entitiesToPersist) {
        entityManager.persist(entity);
      }
    }

  }

  private static class NullViewNodeEntityListener
      implements TransactionalViewNodeEntityListener {

    @Override
    public void entityCreated(ViewNodePropertyEvent event) {
    }

    @Override
    public void entityDiscarded(ViewNodePropertyEvent event) {
    }

    @Override
    public void apply(EntityManager entityManager) {
    }

  }

}
