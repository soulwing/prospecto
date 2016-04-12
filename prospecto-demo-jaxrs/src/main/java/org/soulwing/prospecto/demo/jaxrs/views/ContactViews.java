/*
 * File created on Mar 29, 2016
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
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.demo.jaxrs.domain.Contact;
import org.soulwing.prospecto.demo.jaxrs.domain.Phone;
import org.soulwing.prospecto.demo.jaxrs.domain.PhysicalAddress;

/**
 * Views of {@link Person} entities.
 *
 * @author Carl Harris
 */
public interface ContactViews {

  ViewTemplate PHONE = ViewTemplateBuilderProducer
      .object(Phone.class)
        .value("id")
        .value("version")
        .value("label")
        .value("number")
        .value("textEnabled")
      .build();

  ViewTemplate CONTACT_REFERENCE = ViewTemplateBuilderProducer
      .object(Contact.class)
          .url()
          .value("id")
          .value("surname")
          .value("givenNames")
      .build();

  ViewTemplate PHYSICAL_ADDRESS = ViewTemplateBuilderProducer
      .object(PhysicalAddress.class)
          .value("id")
          .value("version")
          .value("streetAddress")
          .value("municipality")
          .value("state")
          .value("postalCode")
          .end()
      .build();

  ViewTemplate CONTACT_DETAIL = ViewTemplateBuilderProducer
      .object(Contact.class)
          .url()
          .value("id")
          .value("version")
          .value("surname")
          .value("givenNames")
          .value("preferredName")
          .value("gender")
          .value("emailAddress")
          .object("mailingAddress", PHYSICAL_ADDRESS)
          .arrayOfObjects("phones", PHONE)
          .end()
      .build();

  ViewTemplate CONTACT_LIST = ViewTemplateBuilderProducer
      .arrayOfObjects(CONTACT_REFERENCE);

}
