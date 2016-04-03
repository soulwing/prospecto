/*
 * File created on Mar 31, 2016
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

import java.util.Collections;

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.discriminator.SimpleClassNameDiscriminatorStrategy;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;

/**
 * TODO: DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class DiscriminatorDemo {

  public static class TestObject {

    private Long id;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

  }

  public static class TestSubObject extends TestObject {

    private String text;

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

  }

  static final ViewTemplate TEMPLATE = ViewTemplateBuilderProducer
      .arrayOfObjects(TestObject.class)
          .discriminator(SimpleClassNameDiscriminatorStrategy.class,
              "suffix", "Object", "decapitalize", true)
          .value("id")
          .subtype(TestSubObject.class)
              .value("text")
              .end()
      .build();

  public static void main(String[] args) throws Exception {
    TestSubObject model = new TestSubObject();
    Options options = new OptionsMap();
    model.setId(1L);
    model.setText("Some text");
    final ViewContext context = ViewContextProducer.newContext();
    ViewWriterFactory writerFactory = ViewWriterFactoryProducer
        .getFactory("JSON", options);

    final View view = TEMPLATE.generateView(Collections.singleton(model), context);
    writerFactory.newWriter(view, System.out)
        .writeView();
  }

}
