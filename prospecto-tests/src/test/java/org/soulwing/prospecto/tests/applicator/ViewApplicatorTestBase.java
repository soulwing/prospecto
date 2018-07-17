/*
 * File created on Apr 4, 2016
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
package org.soulwing.prospecto.tests.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.soulwing.prospecto.testing.matcher.ViewMatchers.sameView;

import java.beans.Introspector;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.ViewOptionsRegistry;
import org.soulwing.prospecto.ViewReaderFactoryProducer;
import org.soulwing.prospecto.ViewWriterFactoryProducer;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewWriter;
import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.discriminator.UnqualifiedClassNameDiscriminatorStrategy;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.api.options.WriterKeys;
import org.soulwing.prospecto.api.scope.MutableScope;

/**
 * Base support for editor tests that create a model and then update it.
 *
 * @author Carl Harris
 */
public class ViewApplicatorTestBase {

  protected final ViewContext context = ViewContextProducer.newContext();

  @Before
  public void setUp() throws Exception {
    final MutableScope scope = context.appendScope();
    scope.put(new UnqualifiedClassNameDiscriminatorStrategy());
    ViewOptionsRegistry.getOptions().put(ViewKeys.DISCRIMINATOR_NAME,
        "objectType");
  }

  @After
  public void tearDown() throws Exception {
    ViewOptionsRegistry.getOptions().remove(ViewKeys.DISCRIMINATOR_NAME);
  }
  
  protected void validate(ViewTemplate template)
      throws IOException {
    validate(template, Representation.JSON);
    validate(template, Representation.XML);
  }

  protected void validate(ViewTemplate template, Representation representation)
      throws IOException {
    final Object model = validateCreate(template, representation);
    validateUpdate(template, representation, model);
  }

  protected Object validateCreate(ViewTemplate template,
      Representation representation) throws IOException {
    final View createView = readView("create", representation);
    Object model = template.createApplicator(createView, context).create();
    final View createResultView = regenerateView(
        template.generateView(model, context), "create", representation);
    assertThat(createResultView, is(sameView(createView)));
    return model;
  }

  protected void validateUpdate(ViewTemplate template,
      Representation representation, Object model) throws IOException {
    final View updateView = readView("update", representation);
    template.createApplicator(updateView, context).update(model);
    final View updateResultView = regenerateView(
        template.generateView(model, context), "update", representation);
    assertThat(updateResultView, is(sameView(updateView)));
  }

  private View regenerateView(View view, String resourceName,
      Representation representation) throws IOException {
    final String tempLocation =
        representation.temporaryResourceName(resourceName);
    try (final OutputStream outputStream = new FileOutputStream(
        tempLocation)) {
      representation.newWriter(view, outputStream).writeView();
    }
    try (final InputStream inputStream = new FileInputStream(tempLocation)) {
      return representation.newReader(inputStream).readView();
    }
    finally {
      Paths.get(tempLocation).toFile().delete();
    }
  }

  protected View readView(String resourceName, Representation representation)
      throws IOException {
    final String qualifiedResourceName =
        Introspector.decapitalize(getClass().getSimpleName())
            + "/" + representation.resourceName(resourceName);
    final InputStream inputStream = getClass()
        .getResourceAsStream(qualifiedResourceName);

    if (inputStream == null) {
      throw new FileNotFoundException(qualifiedResourceName);
    }
    return representation.newReader(inputStream).readView();
  }

  private enum Representation {
    JSON,
    XML;

    private final ViewReaderFactory readerFactory;

    private final ViewWriterFactory writerFactory;

    Representation() {
      final Options options = new OptionsMap();
      options.put(WriterKeys.INCLUDE_NULL_PROPERTIES, true);
      options.put(WriterKeys.INCLUDE_XML_XSI_TYPE, true);
      readerFactory = ViewReaderFactoryProducer.getFactory(
          this.name(), options);
      writerFactory = ViewWriterFactoryProducer.getFactory(
          this.name(), options);
    }

    public String resourceName(String name) {
      return name + '.' + this.name().toLowerCase();
    }

    public String temporaryResourceName(String name) throws IOException {
      return Files.createTempFile(name, '.'
          + this.name().toLowerCase()).toString();
    }

    public ViewReader newReader(InputStream inputStream) {
      return readerFactory.newReader(inputStream);
    }

    public ViewWriter newWriter(View view, OutputStream outputStream) {
      return writerFactory.newWriter(view, outputStream);
    }

  }


}
