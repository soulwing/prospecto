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

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.domain.PurchaseOrder;
import org.soulwing.prospecto.demo.jaxrs.domain.Vendor;
import org.soulwing.prospecto.demo.jaxrs.views.PurchaseOrderViews;
import org.soulwing.prospecto.demo.jaxrs.views.VendorViews;

/**
 * An stateless application-scoped {@link VendorService}.
 *
 * @author Carl Harris
 */
@Transactional
@ApplicationScoped
public class VendorServiceBean implements VendorService {

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private ViewContext viewContext;

  @Override
  public View findAllVendors() {
    List<Vendor> vendors = entityManager
        .createNamedQuery("findAllVendors", Vendor.class)
        .getResultList();
    return VendorViews.VENDORS_SUMMARY.generateView(vendors, viewContext);
  }

  @Override
  public View findVendorById(Long id) throws NoSuchEntityException {
    Vendor vendor = entityManager.find(Vendor.class, id);
    if (vendor == null) {
      throw new NoSuchEntityException(Vendor.class, id);
    }

    return VendorViews.VENDOR_DETAIL.generateView(vendor, viewContext);
  }

}
