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

import java.util.HashSet;
import java.util.Set;

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;

/**
 * TODO: DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class DiscriminatorTemplateDemo {

  public static class TestCollection {
    private Set<TestObject> objects = new HashSet<>();

    public Set<TestObject> getObjects() {
      return objects;
    }
  }

  public static class TestObject {
    private Long id;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }

  public static class SubObject extends TestObject {
    private String text;

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }

  static ViewTemplate OBJECT_TEMPLATE = ViewTemplateBuilderProducer
      .object(TestObject.class)
      .discriminator()
      .value("id")
      .subtype(SubObject.class)
      .value("text")
      .end()
      .build();

  static ViewTemplate OBJ_COLLECTION = ViewTemplateBuilderProducer
      .arrayOfObjects("objects", OBJECT_TEMPLATE);

  public static void main(String[] args) {
    SubObject o1 = new SubObject();
    o1.setId(123L);
    o1.setText("Text 1");

    SubObject o2 = new SubObject();
    o2.setId(234L);
    o2.setText("Text 2");

    TestCollection collection = new TestCollection();
    collection.getObjects().add(o1);
    collection.getObjects().add(o2);

    final ViewContext context = ViewContextProducer.newContext();

    final Options options = new OptionsMap();

    ViewWriterFactory writerFactory = ViewWriterFactoryProducer
        .getFactory("JSON", options);

    final View view = OBJ_COLLECTION.generateView(collection.getObjects(), context);
    writerFactory.newWriter(view, System.out)
        .writeView();
  }
}
