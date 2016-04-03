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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Set;

import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.reference.ReferenceResolver;

/**
 * Demo for an array of references
 * @author Michael Irwin
 */
public class ArrayOfReferencesDemo {

  public static class ParentObject {
    private Long id;
    private TestObject test;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public TestObject getTest() {
      return test;
    }

    public void setTest(TestObject test) {
      this.test = test;
    }
  }

  public static class ParentUsingCollection {
    private Long id;
    private Set<TestObject> tests = new HashSet<>();

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Set<TestObject> getTests() {
      return tests;
    }

    public void setTests(Set<TestObject> tests) {
      this.tests = tests;
    }
  }

  public static class TestObject {
    private Set<ReferencedObject> references = new HashSet<>();

    public Set<ReferencedObject> getReferences() {
      return references;
    }

  }

  public static class ReferencedObject {
    private Long id;

    public ReferencedObject(Long id) {
      this.id = id;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }

  public static class ReferencedObjectResolver implements ReferenceResolver {
    @Override
    public boolean supports(Class<?> type) {
      return ReferencedObject.class.isAssignableFrom(type);
    }

    @Override
    public Object resolve(Class<?> type, ViewEntity reference) {
      return new ReferencedObject((Long) reference.get("id"));
    }
  }

  static final ViewTemplate TEMPLATE = ViewTemplateBuilderProducer
      .object(TestObject.class)
        .arrayOfReferences("references", ReferencedObject.class)
          .value("id")
          .end()
        .build();

  static final ViewTemplate PARENT_TEMPLATE = ViewTemplateBuilderProducer
      .object(ParentObject.class)
        .value("id")
        .object("test", TestObject.class)
          .arrayOfReferences("references", ReferencedObject.class)
            .value("id")
            .end()
          .end()
        .build();

  static final ViewTemplate PARENT_COLLECTION_TEMPLATE = ViewTemplateBuilderProducer
      .object(ParentUsingCollection.class)
        .value("id")
        .arrayOfObjects("tests", TestObject.class)
          .arrayOfReferences("references", ReferencedObject.class)
            .value("id")
            .end()
          .end()
        .build();

  public static void main(String[] args) throws Exception {
    ReferencedObject reference1 = new ReferencedObject(123L);
    ReferencedObject reference2 = new ReferencedObject(456L);

    TestObject test = new TestObject();
    test.getReferences().add(reference1);
    test.getReferences().add(reference2);

    final ViewContext context = ViewContextProducer.newContext();
    context.getReferenceResolvers().append(new ReferencedObjectResolver());

    final Options options = new OptionsMap();
    ViewWriterFactory writerFactory = ViewWriterFactoryProducer
        .getFactory("JSON", options);

    final View view = TEMPLATE.generateView(test, context);
    writerFactory.newWriter(view, System.out).writeView();

    // Test reading
    String json = "{\"references\":[{\"id\":123},{\"id\":456}]}";
    ViewReaderFactory readerFactory = ViewReaderFactoryProducer
        .getFactory("JSON", options);
    View jsonProducedView = readerFactory
        .newReader(new ByteArrayInputStream(json.getBytes())).readView();

    test = (TestObject) TEMPLATE.generateEditor(jsonProducedView, context).create();
    assertThat(test.getReferences().size(), is(2));
    for (ReferencedObject r : test.getReferences()) {
      assertThat(r, is(notNullValue()));
      assertThat(r.getId(), is(notNullValue()));
    }

    // Test for other json
    String json2 = "{\"id\":321,\"test\":" + json + "}";
    jsonProducedView = readerFactory.newReader(new ByteArrayInputStream(json2.getBytes())).readView();
    ParentObject parent = (ParentObject) PARENT_TEMPLATE.generateEditor(jsonProducedView, context).create();
    assertThat(parent.getTest(), is(notNullValue()));
    assertThat(parent.getTest().getReferences().size(), is(2));

    // Test using json for parent using collection. Should be one item in TestObject
    // in top collection and it should have two referenced objects
    String json3 = "{\"id\":321,\"tests\":[" + json + "]}";
    jsonProducedView = readerFactory.newReader(new ByteArrayInputStream(json3.getBytes())).readView();
    ParentUsingCollection parentUsingCollection = (ParentUsingCollection)
        PARENT_COLLECTION_TEMPLATE.generateEditor(jsonProducedView, context).create();
    assertThat(parentUsingCollection.getTests().size(), is(1));
    assertThat(parentUsingCollection.getTests().iterator().next().getReferences().size(), is(2));
  }

}
