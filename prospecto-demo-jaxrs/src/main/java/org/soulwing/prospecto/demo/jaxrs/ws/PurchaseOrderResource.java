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
package org.soulwing.prospecto.demo.jaxrs.ws;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseOrder;
import org.soulwing.prospecto.demo.jaxrs.service.NoSuchEntityException;
import org.soulwing.prospecto.demo.jaxrs.service.PurchaseOrderService;
import org.soulwing.prospecto.jaxrs.api.ReferencedBy;
import org.soulwing.prospecto.jaxrs.api.TemplateResolver;

/**
 * A JAX-RS resource for {@link PurchaseOrder} entities.
 *
 * @author Carl Harris
 */
@Path("/orders")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class PurchaseOrderResource {

  @Inject
  private PurchaseOrderService purchaseOrderService;

  @GET
  public View getOrders() {
    return purchaseOrderService.findAllOrders();
  }

  @GET
  @Path("/{id}")
  @ReferencedBy(PurchaseOrder.class)
  @TemplateResolver(EntityPathTemplateResolver.class)
  public View getOrder(@PathParam("id") Long orderId) {
    try {
      return purchaseOrderService.findPurchaseOrder(orderId);
    }
    catch (NoSuchEntityException ex) {
      throw new NotFoundException();
    }
  }

}
