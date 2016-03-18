/*
 * File created on Mar 18, 2016
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

import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.ViewTemplateBuilderProducer;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;

/**
 * Tests for various errors in templates.
 *
 * @author Carl Harris
 */
public class TemplateErrorsTest {

  private static final String NAME = "name";
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ValueTypeConverter<?> converter;

  private static final String SOURCE = "source";
  private static final AccessType ACCESS_TYPE = AccessType.PROPERTY;

  @Test(expected = ViewTemplateException.class)
  public void testSourceAtRootNode() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class).source(SOURCE);
  }

  @Test(expected = ViewTemplateException.class)
  public void testAccessTypeAtRootNode() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class).accessType(ACCESS_TYPE);
  }

  @Test(expected = ViewTemplateException.class)
  public void testBuildOnChildBuilder() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class)
        .object(NAME, Object.class)
        .build();
  }

  @Test(expected = ViewTemplateException.class)
  public void testConverterAtRootNode() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class).converter(converter);
  }

  @Test(expected = ViewTemplateException.class)
  public void testConverterOnContainerNode() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class)
        .object(NAME, Object.class).converter(converter);
  }

  @Test(expected = ViewTemplateException.class)
  public void testDiscriminatorNotFirstChild() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class)
        .url()
        .discriminator();
  }

  @Test(expected = ViewTemplateException.class)
  public void testMultipleDiscriminators() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class)
        .discriminator()
        .discriminator();
  }

  @Test(expected = ViewTemplateException.class)
  public void testSubtypeWithoutDiscriminator() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class)
        .subtype(String.class);
  }

  @Test(expected = ViewTemplateException.class)
  public void testSubtypeEqualsType() throws Exception {
    ViewTemplateBuilderProducer.object(Object.class)
        .discriminator()
        .subtype(Object.class);
  }

  @Test(expected = ViewTemplateException.class)
  public void testSubtypeIsNotASubtype() throws Exception {
    ViewTemplateBuilderProducer.object(String.class)
        .discriminator()
        .subtype(Object.class);
  }

}
