/*
 * File created on Nov 6, 2022
 *
 * Copyright (c) 2022 Carl Harris, Jr
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

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.json.JsonPSource;
import org.soulwing.prospecto.api.json.JsonPTarget;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.options.WriterKeys;

/**
 * A simple demo of writing and reading and object using JSON-P.
 *
 * @author Carl Harris
 */
public class JsonPObjectDemo {

  public static class Model {
    public String string = "foobar";
    public Date datetime = new Date();
    public Map<Object, Object> map = new LinkedHashMap<>();
    public List<Object> list = new LinkedList<>();
    public Map<String, Model> nestedModels = new LinkedHashMap<>();
    public JsonObject jsonObject;
    public JsonArray jsonArray;
  }

  public static void main(String[] args) throws Exception {
    ViewContext context = ViewContextProducer.newContext();
    ViewTemplate template = ViewTemplateBuilderProducer
        .object(Model.class)
        .accessType(AccessType.FIELD)
        .value("string")
        .value("datetime")
        .value("map")
        .value("list")
        .mapOfObjects("nestedModels", String.class, Model.class)
          .accessType(AccessType.FIELD)
          .value("string")
          .value("map")
          .value("list")
          .end()
        .value("jsonObject")
        .value("jsonArray")
        .end()
        .build();

    final Model model = new Model();
    model.map.put("string", "another string");
    model.map.put("int", 42);
    model.list.add("cyan");
    model.list.add("magenta");
    model.list.add("yellow");
    model.list.add("black");

    final Model nestedModel = new Model();
    nestedModel.map.put("string", "cool string");
    nestedModel.map.put("float", 3.14159);
    nestedModel.list.add("manny");
    nestedModel.list.add("moe");
    nestedModel.list.add("jack");

    model.nestedModels.put("pep", nestedModel);

    model.jsonObject = Json.createObjectBuilder()
        .add("symbol", "H")
        .add("name", "Hydrogen")
        .add("number", 1)
        .add("common", true)
        .build();

    model.jsonArray = Json.createArrayBuilder()
        .add("Download")
        .add("Print")
        .add("Sign")
        .addNull()
        .build();

    View view = template.generateView(model, context);
    OptionsMap options = new OptionsMap();
    options.put(WriterKeys.USE_ISO_DATETIME, true);
    final ViewWriterFactory writerFactory = ViewWriterFactoryProducer.getFactory("JSON-P", options);
    final JsonPTarget target = new JsonPTarget();
    writerFactory.newWriter(view).writeView(target);
    System.out.println(target.toJson());
    final ViewReaderFactory readerFactory = ViewReaderFactoryProducer.getFactory("JSON-P");
    final JsonPSource source = new JsonPSource(target.toJson());
    view = readerFactory.newReader(source).readView();
    template.createApplicator(view, context).update(model);
    System.out.println(model);
  }
}
