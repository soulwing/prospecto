/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.tests;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.scope.MutableScope;
import org.soulwing.prospecto.demo.DepartmentPurchaser;
import org.soulwing.prospecto.demo.MockUrlResolverProvider;
import org.soulwing.prospecto.demo.PurchaseItem;
import org.soulwing.prospecto.demo.PurchaseOrder;
import org.soulwing.prospecto.demo.Vendor;
import org.soulwing.prospecto.demo.Views;

/**
 * Just a simple demo.
 *
 * @author Carl Harris
 */
public class Demo {

  @Test
  public void test() throws Exception {

    final DepartmentPurchaser purchaser = new DepartmentPurchaser();
    purchaser.setId(5L);
    purchaser.setDepartmentId("072500");

    final Vendor vendor = new Vendor();
    vendor.setId(1L);
    vendor.setName("Office Stuff");
    vendor.setTaxId("905087028");

    final PurchaseItem item1 = new PurchaseItem();
    item1.setId(3L);
    item1.setDescription("Stapler, Swingline, Red");
    item1.setQuantity(2);
    item1.setUnitPrice(BigDecimal.valueOf(15.99));

    final PurchaseItem item2 = new PurchaseItem();
    item2.setId(4L);
    item2.setDescription("Common staples, Bostich");
    item2.setQuantity(6);
    item2.setUnitPrice(BigDecimal.valueOf(4.99));

    final PurchaseOrder order = new PurchaseOrder();
    order.setId(2L);
    order.setFob(PurchaseOrder.FreeOnBoard.ORIGIN);
    order.setCreationDate(new Date());
    order.setDueDate(new Date());
    order.setVendor(vendor);
    order.setPurchaser(purchaser);
    order.getItems().add(item1);
    order.getItems().add(item2);
    order.getApprovers().add("Machelle Hall");
    order.getApprovers().add("Megan Degnan");

    final List<Object> orders = Collections.<Object>singletonList(order);

    /*
     * If using JPA entities, all of what happens next needs to happen inside
     * of a transaction context, while the entity manager is still open on the
     * thread.
     */
    final ViewContext context = ViewContextProducer.newContext();
    MutableScope scope = context.appendScope();

    scope.put(new MockUrlResolverProvider().getResolver());

//    context.getValueTypeConverters().add(
//      new DateTypeConverter(DateTypeConverter.Format.ISO8601_WITH_TIME_ZONE));

    final View summaryView = Views.PO_SUMMARY_TEMPLATE.generateView(orders, context);
    summaryView.getEnvelope().putProperty("offset", 0);
    summaryView.getEnvelope().putProperty("limit", 10);

    final View detailView = Views.PO_DETAILS_TEMPLATE.generateView(order, context);

    /*
     * Probably want to do this once before a MessageBodyWriter is needed.
     */

    final ViewWriterFactory jsonWriterFactory = ViewWriterFactoryProducer.getFactory("JSON");
    final ViewWriterFactory xmlWriterFactory = ViewWriterFactoryProducer.getFactory("XML");

    /*
     * This sort of thing would go in a message body writer.
     */
    System.out.println();
    jsonWriterFactory.newWriter(summaryView, System.out).writeView();
    System.out.println();
    jsonWriterFactory.newWriter(detailView, System.out).writeView();

    System.out.println();
    xmlWriterFactory.newWriter(summaryView, System.out).writeView();
    System.out.println();
    xmlWriterFactory.newWriter(detailView, System.out).writeView();

  }

}
