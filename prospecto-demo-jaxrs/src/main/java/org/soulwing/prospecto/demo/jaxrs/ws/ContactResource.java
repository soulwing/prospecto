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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
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
import org.soulwing.prospecto.demo.jaxrs.domain.Contact;
import org.soulwing.prospecto.demo.jaxrs.service.ContactService;
import org.soulwing.prospecto.demo.jaxrs.service.NoSuchEntityException;
import org.soulwing.prospecto.demo.jaxrs.service.UpdateConflictException;
import org.soulwing.prospecto.demo.jaxrs.views.RootView;
import org.soulwing.prospecto.jaxrs.api.ReferencedBy;
import org.soulwing.prospecto.jaxrs.api.TemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModelSequence;

/**
 * A JAX-RS resource used to access the {@link ContactService} to
 * access contacts.
 *
 * @author Carl Harris
 */
@ApplicationScoped
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ContactResource {

  @Inject
  private ContactService contactService;

  @GET
  @ReferencedBy({ RootView.Root.class, RootView.Contacts.class })
  @TemplateResolver(RootPathResolver.class)
  public View getContacts() {
    return contactService.findAllContacts();
  }

  @POST
  public Response postContact(View contactView,
      @Context UriInfo uriInfo) {
    Object id = contactService.createContact(contactView);
    URI location = uriInfo.getRequestUriBuilder().path("{id}").build(id);
    return Response.created(location).build();
  }

  @GET
  @Path("/{id}")
  @ReferencedBy({ AnyModelSequence.class, Contact.class })
  @TemplateResolver(EntityPathTemplateResolver.class)
  public View getContact(@PathParam("id") Long id) {
    try {
      return contactService.findContactById(id);
    }
    catch (NoSuchEntityException ex) {
      throw new NotFoundException(ex);
    }
  }

  @PUT
  @Path("/{id}")
  public View putContact(@PathParam("id") Long id, View contactView) {
    try {
      return contactService.updateContact(id, contactView);
    }
    catch (NoSuchEntityException ex) {
      throw new NotFoundException(ex);
    }
    catch (UpdateConflictException ex) {
      throw new ClientErrorException(Response.Status.CONFLICT, ex);
    }
  }

}
