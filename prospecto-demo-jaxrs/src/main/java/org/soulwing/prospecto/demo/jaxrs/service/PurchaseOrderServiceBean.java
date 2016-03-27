/*
 * File created on Mar 12, 2016
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

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseOrder;
import org.soulwing.prospecto.demo.jaxrs.views.EnvelopeKeys;
import org.soulwing.prospecto.demo.jaxrs.views.PurchaseOrderViews;

/**
 * An stateless application-scoped {@link PurchaseOrderService}.
 *
 * @author Carl Harris
 */
@Transactional
@ApplicationScoped
public class PurchaseOrderServiceBean implements PurchaseOrderService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private ViewContext viewContext;

  @Override
  public View findAllOrders() {
    List<PurchaseOrder> orders = entityManager
        .createNamedQuery("findAllPurchaseOrders", PurchaseOrder.class)
        .getResultList();
    final View events = PurchaseOrderViews.ORDERS_SUMMARY.generateView(orders,
        viewContext);
    events.getEnvelope().putProperty(EnvelopeKeys.OFFSET, 0);
    events.getEnvelope().putProperty(EnvelopeKeys.LIMIT, 10);
    events.getEnvelope().putProperty(EnvelopeKeys.COUNT, orders.size());
    return events;
  }

  @Override
  public View findPurchaseOrder(Long id) throws NoSuchEntityException {
    PurchaseOrder purchaseOrder = entityManager.find(PurchaseOrder.class, id);
    if (purchaseOrder == null) {
      throw new NoSuchEntityException(PurchaseOrder.class, id);
    }

    return PurchaseOrderViews.ORDER_DETAIL.generateView(purchaseOrder,
        viewContext);
  }

  @Override
  public View updatePurchaseOrder(Long id, View purchaseOrderView)
      throws NoSuchEntityException {
    PurchaseOrder purchaseOrder = entityManager.find(PurchaseOrder.class, id);
    if (purchaseOrder == null) {
      throw new NoSuchEntityException(PurchaseOrder.class, id);
    }

    final ModelEditor editor = PurchaseOrderViews.ORDER_DETAIL.generateEditor(
        purchaseOrderView, viewContext);
    editor.update(purchaseOrder);

    entityManager.flush();
    entityManager.refresh(purchaseOrder);

    return PurchaseOrderViews.ORDER_DETAIL.generateView(purchaseOrder,
        viewContext);
  }

}
