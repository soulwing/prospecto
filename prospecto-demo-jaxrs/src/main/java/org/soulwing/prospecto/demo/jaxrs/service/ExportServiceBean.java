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
package org.soulwing.prospecto.demo.jaxrs.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import org.soulwing.jdbc.FluentJdbc;
import org.soulwing.jdbc.ResultSetHandler;

/**
 * A {@link ExportService} as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class ExportServiceBean implements ExportService {

  @Resource
  private DataSource dataSource;

  @Override
  public ExportResult exportDatabase() {
    final FluentJdbc jdbc = new FluentJdbc(dataSource);
    final ListExportResult result = new ListExportResult();
    jdbc.query()
        .using("SCRIPT SIMPLE NOSETTINGS DROP")
        .handlingResultWith(new ResultSetHandler<Void>() {
          @Override
          public Void handleResult(ResultSet rs) throws SQLException {
            while (rs.next()) {
              result.statements.add(rs.getString(1));
            }
            return null;
          }
        })
        .execute();
    return result;
  }

  private static class ListExportResult implements ExportResult {

    private final List<String> statements = new LinkedList<>();

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
      try (BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(outputStream, "UTF-8"))) {
        for (final String statement : statements) {
          writer.write(statement);
          writer.newLine();
        }
        writer.flush();
      }
    }

  }

}
