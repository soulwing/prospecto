/*
 * File created on Apr 12, 2016
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

/**
 * A view for the root resource.
 *
 * @author Carl Harris
 */
public interface RootView {

  interface Leagues {}

  interface Contacts {}

  interface Root {
    Leagues getLeagues();
    Contacts getContacts();
  }

  ViewTemplate INSTANCE = ViewTemplateBuilderProducer
      .object(Root.class)
          .object("leagues", Leagues.class)
              .url()
              .end()
          .object("contacts", Contacts.class)
              .url()
              .end()
          .end()
      .build();

}
