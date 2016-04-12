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
import org.soulwing.prospecto.demo.jaxrs.domain.League;

/**
 * Views for the {@link org.soulwing.prospecto.demo.jaxrs.domain.League} type.
 * @author Carl Harris
 */
public interface LeagueViews {

  ViewTemplate LEAGUE_REFERENCE = ViewTemplateBuilderProducer
      .object(League.class)
      .url()
      .value("id")
      .value("name")
      .end()
      .build();

  ViewTemplate LEAGUE_LIST = ViewTemplateBuilderProducer
      .arrayOfObjects("leagues", "league", Namespace.URI, League.class)
          .url()
          .value("name")
          .arrayOfReferences("divisions", "division", DivisionViews.DIVISION_SUMMARY)
      .build();

  ViewTemplate LEAGUE_DETAIL = ViewTemplateBuilderProducer
      .object(League.class)
          .value("id")
          .value("version")
          .value("name")
          .arrayOfObjects("divisions", "division", DivisionViews.DIVISION_SUMMARY)
      .build();

}
