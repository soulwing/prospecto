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
package org.soulwing.prospecto.jaxrs.runtime.discovery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;
import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.jaxrs.api.AmbiguousPathResolutionException;
import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;
import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.ReflectionService;
import org.soulwing.prospecto.jaxrs.runtime.ResourceConfigurationException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;
import org.soulwing.prospecto.jaxrs.api.ReferencedBy;
import org.soulwing.prospecto.jaxrs.api.TemplateResolver;

/**
 * Unit tests for {@link ReflectionResourceTypeIntrospector}.
 *
 * @author Carl Harris
 */
public class ReflectionResourceTypeIntrospectorTest {

  private static final String PATH = "somePath";
  private static final ModelPath MODEL_PATH = ModelPath.with();


  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ReflectionService reflectionService;

  @Mock
  private ResourceDescriptorFactory descriptorFactory;

  @Mock
  private ResourceMethodIntrospector methodIntrospector;

  @Mock
  private ResourceDescriptor typeDescriptor;

  @Mock
  private ResourceDescriptor methodDescriptor;

  private TemplateResolver templateResolver =
      AnnotationUtils.templateResolverAnnotation(MockTemplateResolver.class);

  private ReflectionResourceTypeIntrospector introspector;

  @Before
  public void setUp() throws Exception {
    introspector = new ReflectionResourceTypeIntrospector(descriptorFactory,
        methodIntrospector);
  }

  @Test(expected = ResourceConfigurationException.class)
  public void testDescribeAbstractType() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(reflectionService).isAbstractType(with(MockResource.class));
        will(returnValue(true));
      }
    });

    assertThat(introspector.describe(MockResource.class, null, null,
        templateResolver, reflectionService), is(empty()));
  }

  @Test
  public void testDescribe()
      throws Exception {
    final boolean hasDescriptorFlag = true;
    context.checking(annotatedResourceTypeExpectations(hasDescriptorFlag));
    context.checking(typeDescriptorExpectations());
    context.checking(describeMethodsExpectations());

    assertThat(introspector.describe(MockResource.class, PATH, MODEL_PATH,
        templateResolver, reflectionService),
        containsInAnyOrder(typeDescriptor, methodDescriptor));
  }

  @Test
  public void testDescribeWhenReferencedByIndicatesNoDescriptor()
      throws Exception {
    final boolean hasDescriptorFlag = false;
    context.checking(annotatedResourceTypeExpectations(hasDescriptorFlag));
    context.checking(describeMethodsExpectations());

    assertThat(introspector.describe(MockResource.class, PATH, MODEL_PATH,
        templateResolver, reflectionService), contains(methodDescriptor));
  }

  private Expectations annotatedResourceTypeExpectations(
      final boolean hasDescriptorFlag) {
    return new Expectations() {
      {
        oneOf(reflectionService).isAbstractType(MockResource.class);
        will(returnValue(false));
        oneOf(reflectionService).getAnnotation(MockResource.class,
            ReferencedBy.class);
        will(returnValue(AnnotationUtils.referencedByAnnotation(
            hasDescriptorFlag, Object.class)));
      }
    };
  }

  private Expectations typeDescriptorExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(descriptorFactory).newDescriptor(with(MockResource.class),
            with(PATH), with(MODEL_PATH.concat(Object.class)),
            (PathTemplateResolver) with(instanceOf(MockTemplateResolver.class)));
        will(returnValue(typeDescriptor));
      }
    };
  }

  private Expectations describeMethodsExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(reflectionService).getMethods(MockResource.class);
        will(returnValue(new Method[]{
            MockResource.class.getMethod("someMethod")}));
        oneOf(reflectionService).getReturnType(
            MockResource.class.getMethod("someMethod"));
        will(returnValue(Object.class));
        oneOf(methodIntrospector).describe(
            MockResource.class.getMethod("someMethod"),
            PATH, MODEL_PATH.concat(Object.class),
            templateResolver, reflectionService, introspector);
        will(returnValue(Collections.singletonList(methodDescriptor)));
      }
    };
  }

  public static class MockResource {

    public Object someMethod() { return null; }

  }

  public static class MockTemplateResolver implements PathTemplateResolver {
    @Override
    public String resolve(String template, ViewContext context)
        throws AmbiguousPathResolutionException {
      return null;
    }
  }

}
