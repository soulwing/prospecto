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
package org.soulwing.prospecto.demo.jaxrs.ws;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.demo.jaxrs.domain.Division;
import org.soulwing.prospecto.demo.jaxrs.service.DivisionService;
import org.soulwing.prospecto.demo.jaxrs.service.NoSuchEntityException;
import org.soulwing.prospecto.demo.jaxrs.service.PlayerService;
import org.soulwing.prospecto.demo.jaxrs.service.TeamService;
import org.soulwing.prospecto.demo.jaxrs.service.UpdateConflictException;
import org.soulwing.prospecto.jaxrs.api.ModelPathSpec;
import org.soulwing.prospecto.jaxrs.api.TemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModelSequence;

/**
 * A JAX-RS resource used to access the {@link DivisionService}.
 *
 * @author Carl Harris
 */
@Path("/divisions")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class DivisionResource {

  @Inject
  private DivisionService divisionService;

  @Inject
  private TeamService teamService;

  @Inject
  private PlayerService playerService;

  @GET
  @Path("{id}")
  @ModelPathSpec({ AnyModelSequence.class, Division.class })
  @TemplateResolver(EntityPathTemplateResolver.class)
  public View getDivision(@PathParam("id") Long id) {
    try {
      return divisionService.findDivisionById(id);
    }
    catch (NoSuchEntityException ex) {
      throw new NotFoundException(ex);
    }
  }

  @PUT
  @Path("{id}")
  public View putDivision(@PathParam("id") Long id, View divisionView) {
    try {
      return divisionService.updateDivision(id, divisionView);
    }
    catch (NoSuchEntityException ex) {
      throw new NotFoundException(ex);
    }
    catch (UpdateConflictException ex) {
      throw new ClientErrorException(Response.Status.CONFLICT, ex);
    }
  }

  @DELETE
  @Path("{id}")
  public void deleteDivision(@PathParam("id") Long id) {
    divisionService.deleteDivision(id);
  }

  @POST
  @Path("{id}/teams")
  public Response postTeam(View teamView, @PathParam("id") Long divisionId,
      @Context UriInfo uriInfo) {
    Object id = teamService.createTeam(divisionId, teamView);
    URI location = uriInfo.getBaseUriBuilder().path("teams/{id}").build(id);
    return Response.created(location).build();
  }

  @POST
  @Path("{id}/players")
  public Response postPlayer(View teamView, @PathParam("id") Long divisionId,
      @Context UriInfo uriInfo) {
    Object id = playerService.createPlayer(divisionId, teamView);
    URI location = uriInfo.getBaseUriBuilder().path("players/{id}").build(id);
    return Response.created(location).build();
  }

}
