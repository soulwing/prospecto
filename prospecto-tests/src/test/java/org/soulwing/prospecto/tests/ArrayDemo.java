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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriterFactory;

/**
 * A simple demo of the arrayOfValues template construct illustrating
 * the nesting of maps and lists or arrays.
 *
 * @author Carl Harris
 */
public class ArrayDemo {

  public static void main(String[] args) throws Exception {
    ViewContext context = ViewContextProducer.newContext();
    ViewTemplate template = ViewTemplateBuilderProducer.arrayOfValues();

    final Map<Integer, Object> arrayMap = new LinkedHashMap<>();
    arrayMap.put(0, "red");
    arrayMap.put(1, "green");
    arrayMap.put(2, "blue");
    final Map<String, Object> subMap = new LinkedHashMap<>();
    subMap.put("foo", "bar");
    subMap.put("array", new Object[] { "foo", "bar", "baz", 42, arrayMap});
    final List<Object> array = new LinkedList<>();
    array.add("string");
    array.add(42);
    array.add(2.71828);
    array.add(true);
    array.add(null);
    array.add(subMap);
    array.add(new String[] { "red", "green", "blue"});
    final View view = template.generateView(array, context);
    final ViewWriterFactory writerFactory = ViewWriterFactoryProducer.getFactory("JSON");
    writerFactory.newWriter(view, System.out).writeView();
  }
}
