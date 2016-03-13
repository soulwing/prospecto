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
package org.soulwing.prospecto.demo.jaxrs.startup;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.soulwing.prospecto.demo.jaxrs.domain.Account;
import org.soulwing.prospecto.demo.jaxrs.domain.Person;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseOrder;
import org.soulwing.prospecto.demo.jaxrs.domain.Vendor;

/**
 * A startup bean that populates the database with some demo data.
 * @author Carl Harris
 */
@Startup
@Singleton
public class DemoDataLoader {

  @PersistenceContext
  private EntityManager entityManager;

  @PostConstruct
  public void init() {
    Person nadine = Person.Builder.with()
        .type(Person.Type.EMPLOYEE)
        .surname("Bennett")
        .givenName("Nadine")
        .build();

    Person meggan = Person.Builder.with()
        .type(Person.Type.EMPLOYEE)
        .surname("Marshall")
        .givenName("Meggan")
        .build();

    Vendor officeMax = Vendor.Builder.with()
        .name("Office Max")
        .taxId("9018-82828")
        .build();

    Vendor staples = Vendor.Builder.with()
        .name("Staples")
        .taxId("7272-12381")
        .build();

    PurchaseOrder order1 = PurchaseOrder.Builder.with()
        .dueDate(new Date())
        .vendor(officeMax)
        .orderedBy(nadine)
        .item()
          .itemId("76382")
          .description("Stapler, manufacturer:Swingline, color:Red")
          .quantity(BigDecimal.valueOf(2))
          .unitPrice(BigDecimal.valueOf(15.99))
          .end()
        .item()
          .itemId("13138")
          .description("Staples, manufacturer:Bostich")
          .quantity(BigDecimal.valueOf(3))
          .unitPrice(BigDecimal.valueOf(7.99))
          .end()
        .build();

    PurchaseOrder order2 = PurchaseOrder.Builder.with()
        .dueDate(new Date())
        .vendor(staples)
        .orderedBy(meggan)
        .item()
          .itemId("9483")
          .description("Copy paper, case")
          .quantity(BigDecimal.valueOf(2))
          .unitPrice(BigDecimal.valueOf(42.50))
          .end()
        .item()
          .itemId("9483")
          .description("Ink Jet Printer Paper, Bright White, 1 case; Georgia Pacific")
          .quantity(BigDecimal.valueOf(1))
          .unitPrice(BigDecimal.valueOf(42.50))
          .end()
        .item()
          .itemId("855170")
          .description("Sharpie, Ultra; Black; box of 8")
          .quantity(BigDecimal.valueOf(4))
          .unitPrice(BigDecimal.valueOf(11.50))
          .end()
        .build();

    entityManager.persist(nadine);
    entityManager.persist(meggan);
    entityManager.persist(officeMax);
    entityManager.persist(staples);
    entityManager.persist(order1);
    entityManager.persist(order2);
    entityManager.flush();
  }

}
