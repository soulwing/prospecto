/*
 * File created on Apr 11, 2016
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
import org.soulwing.prospecto.demo.jaxrs.domain.Player;
import org.soulwing.prospecto.demo.jaxrs.domain.UniformInfo;

/**
 * Views of {@link Player} entities.
 *
 * @author Carl Harris
 */
public interface PlayerViews {

  ViewTemplate INSURANCE_INFO = ViewTemplateBuilderProducer
      .object(InsuranceInfo.class)
          .value("provider")
          .value("policyNumber")
          .end()
      .build();

  ViewTemplate MEDICAL_INFO = ViewTemplateBuilderProducer
      .object(MedicalInfo.class)
          .reference("emergencyContact", ContactViews.CONTACT_REFERENCE)
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
              .attribute("roleRequired", "MANAGER")
          .object("residenceAddress", ContactViews.PHYSICAL_ADDRESS)
          .object("medicalInfo", MEDICAL_INFO)
          .object("uniformInfo", UNIFORM_INFO)
          .value("battingOrientation")
          .value("throwingOrientation")
          .arrayOfObjects("parents", Parent.class)
              .url()
              .value("id")
              .value("version")
              .reference("contact", Contact.class)
                  .allow(AccessMode.WRITE)
                  .value("id")
                  .end()
              .value("relationship")
              .value("surname")
                  .allow(AccessMode.READ)
              .value("givenNames")
                  .allow(AccessMode.READ)
              .value("emailAddress")
                  .allow(AccessMode.READ)
              .arrayOfReferences("phones", "phone", ContactViews.PHONE)
                  .allow(AccessMode.READ)
              .end()
          .end()
      .build();

  ViewTemplate PLAYER_SUMMARY = ViewTemplateBuilderProducer
      .object(Player.class)
          .url()
          .value("id")
          .value("version")
          .value("surname")
          .value("givenNames")
          .value("birthDate")
              .attribute("roleRequired", "MANAGER")
          .end()
      .build();

}
