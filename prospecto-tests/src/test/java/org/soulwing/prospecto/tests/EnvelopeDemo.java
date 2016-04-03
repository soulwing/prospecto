/*
 * File created on Mar 30, 2016
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
package org.soulwing.prospecto.tests;

import java.io.ByteArrayInputStream;
import java.util.Date;

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;

/**
 * TODO: DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class EnvelopeDemo {

  public static class TestObject {
    private Date startDate;
    private Date endDate;

    public Date getStartDate() {
      return startDate;
    }

    public void setStartDate(Date startDate) {
      this.startDate = startDate;
    }

    public Date getEndDate() {
      return endDate;
    }

    public void setEndDate(Date endDate) {
      this.endDate = endDate;
    }
  }

  static final ViewTemplate TEMPLATE = ViewTemplateBuilderProducer
      .object(TestObject.class)
      .envelope("stuffer")
        .envelope("dates")
          .value("start").source("startDate")
          .value("end").source("endDate")
        .end()
      .end()
      .build();

  public static void main(String[] args) throws Exception {
    TestObject test = new TestObject();
    test.endDate = new Date(20000L);
    test.startDate = new Date(50000L);

    final Options options = new OptionsMap();

    String json = "{\"stuffer\":{\"dates\":{\"start\":70000,\"end\":80000}}}";
    ViewReaderFactory readerFactory = ViewReaderFactoryProducer
        .getFactory("JSON", options);

    ViewReader reader = readerFactory.newReader(
        new ByteArrayInputStream(json.getBytes()));
    View updateView = reader.readView();


    final ViewContext context = ViewContextProducer.newContext();
    ModelEditor editor = TEMPLATE.generateEditor(updateView, context);

    ViewWriterFactory writerFactory = ViewWriterFactoryProducer
        .getFactory("JSON", options);

    editor.update(test);

    writerFactory.newWriter(TEMPLATE.generateView(test, context), System.out)
        .writeView();
  }


}
