/*
 * File created on Mar 31, 2016
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

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.association.AbstractToManyAssociationManager;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.reference.ReferenceResolver;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Demo for custom association managers when using arrayOfReferences
 *
 * @author Michael Irwin
 */
public class AssociationManagerDemo {

  public static class Order {
    private Long id;
    private int total;
    private Set<Product> products = new HashSet<>();

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Set<Product> getProducts() {
      return products;
    }

    public void addProduct(Product product) {
      products.add(product);
      total += product.getCost();
    }

    public boolean removeProduct(Product product) {
      boolean removed = products.remove(product);
      if (removed)
        total -= product.getCost();
      return removed;
    }

    public int getTotal() {
      return total;
    }
  }

  public static class Product {
    private Long id;
    private String name;
    private int cost;

    public Product() {}
    public Product(Long id, String name, int cost) {
      this.id = id;
      this.name = name;
      this.cost = cost;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getCost() {
      return cost;
    }

    public void setCost(int cost) {
      this.cost = cost;
    }
  }

  public static class OrderToProductAssociationManager
      extends AbstractToManyAssociationManager<Order, Product>
      implements ToManyAssociationManager<Order, Product> {

    @Override
    public Iterator<Product> iterator(Order owner) throws Exception {
      return owner.getProducts().iterator();
    }

    @Override
    public int size(Order owner) throws Exception {
      return owner.getProducts().size();
    }

    @Override
    public Product findAssociate(Order owner, ViewEntity elementEntity) throws Exception {
      for (Product product : owner.getProducts())
        if (elementEntity.get("id").equals(product.getId()))
          return product;
      return null;
    }

    @Override
    public void add(Order owner, Product element) throws Exception {
      owner.addProduct(element);
    }

    @Override
    public boolean remove(Order owner, Product element) throws Exception {
      return owner.removeProduct(element);
    }

    @Override
    public void begin(Order owner) throws Exception {}

    @Override
    public void end(Order owner) throws Exception { }

    @Override
    public boolean supports(AssociationDescriptor descriptor) {
      return Order.class.isAssignableFrom(descriptor.getOwnerType()) &&
          Product.class.isAssignableFrom(descriptor.getAssociateType());
    }
  }

  static class ProductResolver implements ReferenceResolver {
    @Override
    public boolean supports(Class<?> type) {
      return Product.class.isAssignableFrom(type);
    }

    @Override
    public Object resolve(Class<?> type, ViewEntity reference) {
      return new Product((long) reference.get("id"), (String) reference.get("name"), (int) reference.get("cost"));
    }
  }

  static ViewTemplate TEMPLATE = ViewTemplateBuilderProducer
      .object(Order.class)
        .value("id")
        .arrayOfReferences("products", Product.class)
          .value("id")
          .value("cost")
          .value("name")
          .end()
        .build();

  public static void main(String[] args) throws Exception {
    final ViewContext context = ViewContextProducer.newContext();
    context.getReferenceResolvers().append(new ProductResolver());
    context.getAssociationManagers().append(new OrderToProductAssociationManager());

    final Options options = new OptionsMap();

    String product1 = "{\"id\":987,\"name\":\"Product 1\",\"cost\":1000}";
    String product2 = "{\"id\":876,\"name\":\"Product 2\",\"cost\":1500}";
    String json = "{\"id\":123,\"products\":[" + product1 + "," + product2 + "]}";

    ViewReaderFactory readerFactory = ViewReaderFactoryProducer
        .getFactory("JSON", options);
    View jsonProducedView = readerFactory
        .newReader(new ByteArrayInputStream(json.getBytes())).readView();

    Order order = (Order) TEMPLATE.generateEditor(jsonProducedView, context).create();
    assertThat(order.getId(), is(equalTo(123L)));
    assertThat(order.getProducts().size(), is(2));
    assertThat(order.getTotal(), is(2500));
  }

}
