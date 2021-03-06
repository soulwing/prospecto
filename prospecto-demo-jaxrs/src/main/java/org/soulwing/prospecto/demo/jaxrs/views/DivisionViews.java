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
import org.soulwing.prospecto.demo.jaxrs.domain.Division;

/**
 * Views for the {@link org.soulwing.prospecto.demo.jaxrs.domain.Division} type.
 * @author Carl Harris
 */
public interface DivisionViews {

  ViewTemplate DIVISION_REFERENCE = ViewTemplateBuilderProducer
      .object(Division.class)
          .url()
          .value("id")
          .value("name")
          .end()
      .build();

  ViewTemplate DIVISION_SUMMARY = ViewTemplateBuilderProducer
      .object(Division.class)
          .url()
          .value("id")
          .value("version")
          .value("name")
          .value("ageLimit")
          .value("gender")
          .value("playerCount")
          .arrayOfReferences("teams", "team", TeamViews.TEAM_SUMMARY)
              .allow(AccessMode.READ)
          .end()
      .build();

  ViewTemplate DIVISION_DETAIL = ViewTemplateBuilderProducer
      .object(Division.class)
          .value("id")
          .reference("league", LeagueViews.LEAGUE_REFERENCE)
          .value("version")
          .value("name")
          .value("ageLimit")
          .value("gender")
          .envelope("teamInfo")
              .meta("href", "teams")
              .arrayOfObjects("teams", "team", TeamViews.TEAM_SUMMARY)
              .end()
          .envelope("playerInfo")
              .meta("href", "players")
              .arrayOfObjects("players", "player", PlayerViews.PLAYER_SUMMARY)
              .end()
          .end()
      .build();

}
