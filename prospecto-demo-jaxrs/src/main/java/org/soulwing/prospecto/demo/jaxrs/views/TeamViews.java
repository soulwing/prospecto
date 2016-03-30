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
import org.soulwing.prospecto.demo.jaxrs.domain.Team;

/**
 * Views for the {@link org.soulwing.prospecto.demo.jaxrs.domain.Team} type.
 * @author Carl Harris
 */
public interface TeamViews {

  ViewTemplate TEAM_SUMMARY = ViewTemplateBuilderProducer
      .object(Team.class)
          .value("id")
          .value("version")
          .value("name")
          .reference("manager", PersonViews.CONTACT_REFERENCE)
      .build();

}
