/*
 * File created on Mar 21, 2016
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

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.converter.DateTypeConverter;
import org.soulwing.prospecto.api.discriminator.SimpleClassNameDiscriminatorStrategy;
import org.soulwing.prospecto.api.handler.ViewNodeEntityListener;
import org.soulwing.prospecto.api.handler.ViewNodeEvent;
import org.soulwing.prospecto.api.handler.ViewNodeListener;
import org.soulwing.prospecto.api.handler.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.handler.ViewNodePropertyListener;
import org.soulwing.prospecto.api.reference.ReferenceResolver;
import org.soulwing.prospecto.demo.DepartmentPurchaser;
import org.soulwing.prospecto.demo.PersonPurchaser;
import org.soulwing.prospecto.demo.PurchaseItem;
import org.soulwing.prospecto.demo.PurchaseOrder;
import org.soulwing.prospecto.demo.Purchaser;
import org.soulwing.prospecto.demo.Vendor;

/**
 * TODO: DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class EditorDemo {

  public static final ViewTemplate PO_TEMPLATE = ViewTemplateBuilderProducer
      .object("order", PurchaseOrder.class)
        .value("id")
        .value("fob")
        .value("creationDate")
        .value("dueDate")
          .converter(DateTypeConverter.class,
              "format", DateTypeConverter.Format.ISO8601_DATE)
        .value("total")
          .source("itemTotal")
          .allow(AccessMode.READ)
        .reference("vendor", Vendor.class)
          .value("id")
          .value("name")
          .end()
        .object("purchaser", Purchaser.class)
          .discriminator(SimpleClassNameDiscriminatorStrategy.class,
              "suffix", "Purchaser",
              "decapitalize", true)
          .value("id")
          .subtype(DepartmentPurchaser.class)
            .value("name")
            .value("departmentId")
          .end()
          .subtype(PersonPurchaser.class)
            .value("surname")
            .value("givenName")
          .end()
        .end()
        .arrayOfObjects("items", "item", PurchaseItem.class)
          .value("id")
          .value("description")
          .value("quantity")
          .value("unitPrice")
        .end()
      .end()
      .build();

  public static void main(String[] args) throws Exception {

    final Vendor vendor = new Vendor();
    vendor.setId(1L);
    vendor.setName("Office Stuff");
    vendor.setTaxId("905087028");

    final DepartmentPurchaser purchaser = new DepartmentPurchaser();
    purchaser.setId(5L);
    purchaser.setDepartmentId("072500");

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

    ViewReaderFactory readerFactory = ViewReaderFactoryProducer
        .getFactory("JSON", Collections.<String, Object>emptyMap());

    ViewReader reader = readerFactory.newReader(
        EditorDemo.class.getClassLoader().getResourceAsStream("po.json"));

    View view = reader.readView();

    ViewContext context = ViewContextProducer.newContext();

    context.getListeners().append(new ViewNodePropertyListener() {
      @Override
      public void propertyVisited(ViewNodePropertyEvent event) {
        System.out.format("will inject '%s' at %s\n",
            event.getValue(), event.getContext().currentViewPathAsString());
      }
    });

    context.getListeners().append(new ViewNodeListener() {
      @Override
      public void nodeVisited(ViewNodeEvent event) {
        System.out.format("%s visited\n",
            event.getContext().currentViewPathAsString());
      }
    });

    context.getListeners().append(new ViewNodeEntityListener() {
      @Override
      public void entityCreated(ViewNodePropertyEvent event) {
        System.out.format("created entity: %s\n", event.getValue());
      }

      @Override
      public void entityDiscarded(ViewNodePropertyEvent event) {
        System.out.format("discarded entity: %s\n", event.getValue());
      }
    });

    context.getReferenceResolvers().append(new ReferenceResolver() {
      @Override
      public boolean supports(Class<?> type) {
        return Vendor.class.isAssignableFrom(type);
      }

      @Override
      public Object resolve(Class<?> type, ViewEntity reference) {
        return vendor;
      }
    });

    ModelEditor editor =
        PO_TEMPLATE.generateEditor(view, context);

    editor.update(order);

    ViewWriterFactory writerFactory = ViewWriterFactoryProducer
        .getFactory("JSON");

    View updatedView = PO_TEMPLATE.generateView(order, context);

    ViewWriter writer = writerFactory.newWriter(updatedView, System.out);
    writer.writeView();
  }
}
