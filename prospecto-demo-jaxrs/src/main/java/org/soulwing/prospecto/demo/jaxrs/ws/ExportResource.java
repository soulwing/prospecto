/*
 * File created on Apr 14, 2016
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

import java.io.IOException;
import java.io.OutputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.soulwing.prospecto.demo.jaxrs.service.ExportService;

/**
 * A resource for exporting the database contents.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class ExportResource {

  @Inject
  private ExportService exportService;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public StreamingOutput exportDatabase() {
    final ExportService.ExportResult result = exportService.exportDatabase();
    return new StreamingOutput() {
      @Override
      public void write(OutputStream outputStream) throws
          IOException, WebApplicationException {
        result.writeTo(outputStream);
      }
    };
  }

}
