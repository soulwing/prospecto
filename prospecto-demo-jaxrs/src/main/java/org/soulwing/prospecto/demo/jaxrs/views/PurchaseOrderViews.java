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
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.converter.PropertyExtractingValueTypeConverter;
import org.soulwing.prospecto.demo.jaxrs.domain.Account;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseItem;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseOrder;

/**
 * Views of the {@link PurchaseOrder} entity.
 *
 * @author Carl Harris
 */
public interface PurchaseOrderViews {

  ViewTemplate ITEM_DETAIL = ViewTemplateBuilderProducer
      .object(PurchaseItem.class)
        .value("id")
        .value("version")
        .value("lineNumber")
        .value("description")
        .value("quantity")
        .value("unitPrice")
        .value("price")
      .build();

  ViewTemplate ORDER_DETAIL = ViewTemplateBuilderProducer
      .object("order", Namespace.URI, PurchaseOrder.class)
          .url()
          .value("id")
          .value("version")
          .value("creationDate")
          .value("dueDate")
          .reference("vendor", VendorViews.VENDOR_REFERENCE)
          .value("orderedBy").converter(PersonViews.PERSON_NAME_CONVERTER)
              .allow(AccessMode.READ)
          .value("comment")
          .value("currency")
          .value("fund")
              .allow(AccessMode.READ)
              .attribute("roleRequired", "MANAGER")
              .converter(PropertyExtractingValueTypeConverter.class,
                  "modelType", Account.class,
                  "propertyName", "accountId")
          .value("total")
              .allow(AccessMode.READ)
          .arrayOfObjects("items", "item", ITEM_DETAIL)
      .build();

  ViewTemplate ORDERS_SUMMARY = ViewTemplateBuilderProducer
      .arrayOfObjects("orders", "order", Namespace.URI, PurchaseOrder.class)
          .url()
          .value("dueDate")
          .object("vendor", VendorViews.VENDOR_REFERENCE)
          .value("orderedBy").converter(PersonViews.PERSON_NAME_CONVERTER)
          .value("total")
      .build();

}
