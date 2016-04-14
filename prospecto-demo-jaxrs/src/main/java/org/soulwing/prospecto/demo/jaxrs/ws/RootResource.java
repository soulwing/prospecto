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
package org.soulwing.prospecto.demo.jaxrs.ws;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.demo.jaxrs.service.RootService;

/**
 * A root JAX-RS resource.
 *
 * @author Carl Harris
 */
@Path("/")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class RootResource {

  @Inject
  private Instance<LeagueResource> leagueResources;

  @Inject
  private Instance<ContactResource> contactResources;

  @Inject
  private Instance<ExportResource> exportResources;

  @Inject
  private RootService rootService;

  @GET
  public View getRoot() {
    return rootService.getRootView();
  }

  @Path("/leagues")
  public LeagueResource getLeagues() {
    return leagueResources.get();
  }

  @Path("/contacts")
  public ContactResource getContacts() {
    return contactResources.get();
  }

  @Path("/export")
  public ExportResource getExport() {
    return exportResources.get();
  }

}
