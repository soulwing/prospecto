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
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.demo.jaxrs.domain.Contact;
import org.soulwing.prospecto.demo.jaxrs.domain.InsuranceInfo;
import org.soulwing.prospecto.demo.jaxrs.domain.MedicalInfo;
import org.soulwing.prospecto.demo.jaxrs.domain.Parent;
import org.soulwing.prospecto.demo.jaxrs.domain.Person;
import org.soulwing.prospecto.demo.jaxrs.domain.Phone;
import org.soulwing.prospecto.demo.jaxrs.domain.PhysicalAddress;
import org.soulwing.prospecto.demo.jaxrs.domain.Player;
import org.soulwing.prospecto.demo.jaxrs.domain.UniformInfo;

/**
 * Views of {@link Person} entities.
 *
 * @author Carl Harris
 */
public interface PersonViews {

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
          .value("streetAddress")
          .value("municipality")
          .value("state")
          .value("postalCode")
          .end()
      .build();

  ViewTemplate INSURANCE_INFO = ViewTemplateBuilderProducer
      .object(InsuranceInfo.class)
          .value("provider")
          .value("policyNumber")
          .end()
      .build();

  ViewTemplate MEDICAL_INFO = ViewTemplateBuilderProducer
      .object(MedicalInfo.class)
          .reference("emergencyContact", CONTACT_REFERENCE)
          .object("insuranceInfo", INSURANCE_INFO)
          .value("note")
          .end()
      .build();

  ViewTemplate UNIFORM_INFO = ViewTemplateBuilderProducer
      .object(UniformInfo.class)
          .value("jerseySize")
          .value("preferredNumbers")
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

  ViewTemplate PLAYER_SUMMARY = ViewTemplateBuilderProducer
      .object(Player.class)
          .url()
          .value("id")
          .value("version")
          .value("surname")
          .value("givenNames")
          .value("birthDate")
              .allow(AccessMode.WRITE)
          .end()
      .build();

  ViewTemplate PLAYER_DETAIL = ViewTemplateBuilderProducer
      .object(Player.class)
          .url()
          .value("id")
          .value("version")
          .value("surname")
          .value("givenNames")
          .value("preferredName")
          .value("gender")
          .value("birthDate")
          .object("residenceAddress", PHYSICAL_ADDRESS)
          .object("medicalInfo", MEDICAL_INFO)
          .object("uniformInfo", UNIFORM_INFO)
          .value("battingOrientation")
          .value("throwingOrientation")
          .arrayOfObjects("parents", Parent.class)
              .url()
              .value("id")
              .value("version")
              .value("surname")
              .value("givenNames")
              .value("relationship")
              .end()
          .end()
      .build();


}
