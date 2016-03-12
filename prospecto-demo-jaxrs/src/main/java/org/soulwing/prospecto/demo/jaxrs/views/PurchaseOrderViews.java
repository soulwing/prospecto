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
package org.soulwing.prospecto.demo.jaxrs.views;

import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseItem;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseOrder;

/**
 * Views of the {@link PurchaseOrder} entity.
 *
 * @author Carl Harris
 */
public interface PurchaseOrderViews {

  ViewTemplate ORDER_DETAIL = ViewTemplateBuilderProducer
      .object("order", Namespace.URI, PurchaseOrder.class)
          .url()
          .value("id")
          .value("version")
          .value("creationDate")
          .value("dueDate")
          .subview("vendor", VendorViews.VENDOR_REFERENCE)
          .subview("orderedBy", PersonViews.PERSON_REFERENCE)
          .value("comment")
          .value("total")
          .arrayOfObjects("items", "item", PurchaseItem.class)
              .value("id")
              .value("version")
              .value("lineNumber")
              .value("description")
              .value("quantity")
              .value("unitPrice")
              .value("price")
          .end()
      .build();

  ViewTemplate ORDERS_SUMMARY = ViewTemplateBuilderProducer
      .arrayOfObjects("orders", "order", Namespace.URI, PurchaseOrder.class)
          .url()
          .value("dueDate")
          .subview("vendor", VendorViews.VENDOR_REFERENCE)
          .subview("orderedBy", PersonViews.PERSON_REFERENCE)
          .value("total")
      .build();

}
