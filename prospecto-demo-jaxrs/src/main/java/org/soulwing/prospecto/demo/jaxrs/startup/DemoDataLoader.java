/*
 * File created on Mar 12, 2016
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
package org.soulwing.prospecto.demo.jaxrs.startup;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

import org.soulwing.jdbc.FluentJdbc;
import org.soulwing.jdbc.source.ResourceSQLSource;

/**
 * A startup bean that populates the database with some demo data.
 * @author Carl Harris
 */
@Startup
@Singleton
@TransactionAttribute(TransactionAttributeType.NEVER)
public class DemoDataLoader {

  @Resource
  private DataSource dataSource;

  @PostConstruct
  public void init() {
    final FluentJdbc jdbc = new FluentJdbc(dataSource);
    jdbc.setLogger(System.out);
    jdbc.executeScript(ResourceSQLSource.with("classpath:"
        + DemoDataLoader.class.getSimpleName() + ".sql"));
  }

}
