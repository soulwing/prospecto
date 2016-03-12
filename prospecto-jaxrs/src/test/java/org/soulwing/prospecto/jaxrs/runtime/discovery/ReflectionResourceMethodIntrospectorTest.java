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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

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
 * Unit tests of {@link ReflectionResourceMethodIntrospector}.
 *
 * @author Carl Harris
 */
public class ReflectionResourceMethodIntrospectorTest {

  public static final String PARENT_PATH = "/parentPath";

  public static final String RESOURCE_PATH = "/resourcePath";

  public static final ModelPath MODEL_PATH = ModelPath.with();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ReflectionService reflectionService;

  @Mock
  private PathTemplateResolver pathTemplateResolver;

  @Mock
  private ResourceTypeIntrospector typeIntrospector;

  @Mock
  private ResourceDescriptorFactory descriptorFactory;

  @Mock
  private ResourceDescriptor typeDescriptor;

  @Mock
  private ResourceDescriptor methodDescriptor;

  private TemplateResolver inheritedTemplateResolver =
      AnnotationUtils.templateResolverAnnotation(MockTemplateResolver.class);

  private TemplateResolver typeTemplateResolver =
      AnnotationUtils.templateResolverAnnotation(MockTemplateResolver.class);

  private TemplateResolver methodTemplateResolver =
      AnnotationUtils.templateResolverAnnotation(MockTemplateResolver.class);

  private ReflectionResourceMethodIntrospector introspector;

  @Before
  public void setUp() throws Exception {
    introspector = new ReflectionResourceMethodIntrospector(descriptorFactory);
  }

  @Test
  public void testMethodThatIsNotResourceOrLocator() throws Exception {
    final Method method = MockResource.class.getMethod("notResourceOrLocator");
    final boolean hasPath = false;
    final boolean hasHttpMethod = false;
    context.checking(resourceAnnotationExpectations(method, hasPath, hasHttpMethod));
    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH, null,
        reflectionService, typeIntrospector), is(empty()));
  }

  @Test
  public void testLocatorMethodWithConcreteReturn() throws Exception {
    final Method method = MockResource.class.getMethod("locator");
    final boolean hasPath = true;
    final boolean hasHttpMethod = false;
    final boolean hasAbstractReturnType = false;
    final boolean hasTemplateResolver = true;
    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedLocatorMethodExpectations(method,
        hasAbstractReturnType, hasTemplateResolver));
    context.checking(typeDescriptorExpectations(MockResource.class,
        MODEL_PATH.concat(Object.class), hasTemplateResolver));
    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH, null,
        reflectionService, typeIntrospector), contains(typeDescriptor));
  }

  @Test
  public void testLocatorMethodWithConcreteReturnAndTypeTemplateResolver()
      throws Exception {
    final Method method = MockResource.class.getMethod("locator");
    final boolean hasPath = true;
    final boolean hasHttpMethod = false;
    final boolean hasAbstractReturnType = false;
    final boolean hasMethodTemplateResolver = false;
    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedLocatorMethodExpectations(method,
        hasAbstractReturnType, hasMethodTemplateResolver));
    context.checking(typeDescriptorExpectations(MockResource.class,
        MODEL_PATH.concat(Object.class), hasMethodTemplateResolver));
    context.checking(new Expectations() {
      {
        oneOf(reflectionService).getAnnotation(MockResource.class,
            TemplateResolver.class);
        will(returnValue(typeTemplateResolver));
      }
    });
    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH,
        inheritedTemplateResolver,
        reflectionService, typeIntrospector), contains(typeDescriptor));
  }

  @Test
  public void testLocatorMethodWithAbstractReturn()
      throws Exception {
    final Method method = MockResource.class.getMethod("locator");
    final boolean hasPath = true;
    final boolean hasHttpMethod = false;
    final boolean hasAbstractReturnType = true;
    final boolean hasTemplateResolver = false;
    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedLocatorMethodExpectations(method,
        hasAbstractReturnType, hasTemplateResolver));
    context.checking(typeDescriptorExpectations(MockSubResource.class,
        MODEL_PATH, hasTemplateResolver));
    context.checking(new Expectations() {
      {
        oneOf(reflectionService).getSubTypesOf(MockResource.class);
        will(returnValue(Collections.singleton(MockSubResource.class)));
        oneOf(reflectionService).getAnnotation(MockSubResource.class,
            ReferencedBy.class);
        will(returnValue(AnnotationUtils.referencedByAnnotation(Object.class)));
        oneOf(reflectionService).getAnnotation(MockSubResource.class,
            TemplateResolver.class);
        will(returnValue(typeTemplateResolver));
      }
    });

    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH,
        typeTemplateResolver, reflectionService, typeIntrospector),
        contains(typeDescriptor));
  }

  @Test(expected = ResourceConfigurationException.class)
  public void testLocatorMethodWithAbstractReturnNoMatchingSubResourceType()
      throws Exception {
    final Method method = MockResource.class.getMethod("locator");
    final boolean hasPath = true;
    final boolean hasHttpMethod = false;
    final boolean hasAbstractReturnType = true;
    final boolean hasTemplateResolver = false;
    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedLocatorMethodExpectations(method,
        hasAbstractReturnType, hasTemplateResolver));
    context.checking(new Expectations() {
      {
        oneOf(reflectionService).getSubTypesOf(MockResource.class);
        will(returnValue(Collections.emptySet()));
      }
    });

    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH, null,
        reflectionService, typeIntrospector), is(empty()));
  }

  @Test(expected = ResourceConfigurationException.class)
  public void testLocatorMethodWithAbstractReturnMoreThanOneSubResourceType()
      throws Exception {
    final Method method = MockResource.class.getMethod("locator");
    final Set<Class<?>> subTypes = new HashSet<>();
    subTypes.add(MockSubResource.class);
    subTypes.add(MockOtherSubResource.class);

    final boolean hasPath = true;
    final boolean hasHttpMethod = false;
    final boolean hasAbstractReturnType = true;
    final boolean hasTemplateResolver = false;

    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedLocatorMethodExpectations(method,
        hasAbstractReturnType, hasTemplateResolver));
    context.checking(new Expectations() {
      {
        oneOf(reflectionService).getSubTypesOf(MockResource.class);
        will(returnValue(subTypes));
        oneOf(reflectionService).getAnnotation(MockSubResource.class,
            ReferencedBy.class);
        will(returnValue(AnnotationUtils.referencedByAnnotation(Object.class)));
        oneOf(reflectionService).getAnnotation(MockOtherSubResource.class,
            ReferencedBy.class);
        will(returnValue(AnnotationUtils.referencedByAnnotation(Object.class)));
      }
    });

    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH, null,
        reflectionService, typeIntrospector), is(empty()));
  }

  @Test
  public void testAnnotatedSubResource() throws Exception {
    final Method method = MockResource.class.getMethod("locator");
    final boolean hasPath = true;
    final boolean hasHttpMethod = false;
    final boolean hasTemplateResolver = false;
    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedSubresourceExpectations(method,
        hasTemplateResolver));
    context.checking(typeDescriptorExpectations(MockSubResource.class,
        MODEL_PATH, hasTemplateResolver));
    context.checking(new Expectations() {
      {
        oneOf(reflectionService).getAnnotation(MockSubResource.class,
            TemplateResolver.class);
        will(returnValue(typeTemplateResolver));
      }
    });

    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH,
        typeTemplateResolver, reflectionService, typeIntrospector),
        contains(typeDescriptor));
  }

  @Test
  public void testAnnotatedResourceMethod() throws Exception {
    final Method method = MockResource.class.getMethod("resource");
    final boolean hasPath = true;
    final boolean hasHttpMethod = true;
    final boolean hasTemplateResolver = true;
    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedResourceMethodExpectations(method,
        hasTemplateResolver));
    context.checking(methodDescriptorExpectations(method,
        hasTemplateResolver));
    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH, null,
        reflectionService, typeIntrospector), contains(methodDescriptor));
  }

  @Test
  public void testAnnotatedResourceMethodWithTypeTemplateResolver()
      throws Exception {
    final Method method = MockResource.class.getMethod("resource");
    final boolean hasPath = true;
    final boolean hasHttpMethod = true;
    final boolean hasTemplateResolver = false;
    context.checking(resourceAnnotationExpectations(method, hasPath,
        hasHttpMethod));
    context.checking(annotatedResourceMethodExpectations(method,
        hasTemplateResolver));
    context.checking(methodDescriptorExpectations(method,
        hasTemplateResolver));
    assertThat(introspector.describe(method, PARENT_PATH, MODEL_PATH,
        typeTemplateResolver, reflectionService, typeIntrospector),
        contains(methodDescriptor));
  }

  private Expectations annotatedLocatorMethodExpectations(
      final Method method, final boolean hasAbstractReturnType,
      final boolean hasTemplateResolver)
      throws Exception {
    return new Expectations() {
      {
        allowing(reflectionService).getReturnType(method);
        will(returnValue(MockResource.class));
        oneOf(reflectionService).isAbstractType(MockResource.class);
        will(returnValue(hasAbstractReturnType));
        oneOf(reflectionService).getAnnotation(method, ReferencedBy.class);
        will(returnValue(AnnotationUtils.referencedByAnnotation(Object.class)));
        oneOf(reflectionService).getAnnotation(method, TemplateResolver.class);
        will(returnValue(hasTemplateResolver ? methodTemplateResolver : null));
      }
    };
  }

  private Expectations typeDescriptorExpectations(final Class<?> returnType,
      final ModelPath modelPath, final boolean hasTemplateResolver)
      throws Exception {
    return new Expectations() {
      {
        oneOf(typeIntrospector).describe(
            with(returnType),
            with(PARENT_PATH + RESOURCE_PATH),
            with(modelPath),
            with(hasTemplateResolver ? methodTemplateResolver :
                typeTemplateResolver),
            with(reflectionService));
        will(returnValue(Collections.singletonList(typeDescriptor)));
      }
    };
  }

  private Expectations annotatedSubresourceExpectations(
      final Method method, final boolean hasTemplateResolver) throws Exception {
    return new Expectations() {
      {
        oneOf(reflectionService).getReturnType(method);
        will(returnValue(MockSubResource.class));
        oneOf(reflectionService).isAbstractType(MockSubResource.class);
        will(returnValue(false));
        oneOf(reflectionService).getAnnotation(method, ReferencedBy.class);
        will(returnValue(null));
        oneOf(reflectionService).getAnnotation(method, TemplateResolver.class);
        will(returnValue(hasTemplateResolver ? typeTemplateResolver : null));
      }
    };
  }

  private Expectations resourceAnnotationExpectations(
      final Method method, final boolean hasPath, final boolean hasHttpMethod)
      throws Exception {
    return new Expectations() {
      {
        oneOf(reflectionService).getAnnotation(method, Path.class);
        will(returnValue(hasPath ?
            AnnotationUtils.pathAnnotation(RESOURCE_PATH) : null));
        oneOf(reflectionService).getAnnotation(method, GET.class);
        will(returnValue(hasHttpMethod ?
            AnnotationUtils.httpGETAnnotation() : null));
        allowing(reflectionService).getAnnotation(method, POST.class);
        will(returnValue(null));
        allowing(reflectionService).getAnnotation(method, PUT.class);
        will(returnValue(null));
        allowing(reflectionService).getAnnotation(method, DELETE.class);
        will(returnValue(null));
        allowing(reflectionService).getAnnotation(method, HEAD.class);
        will(returnValue(null));
        allowing(reflectionService).getAnnotation(method, OPTIONS.class);
        will(returnValue(null));
      }
    };
  }

  private Expectations annotatedResourceMethodExpectations(
      final Method method, final boolean hasTemplateResolver) throws Exception {
    return new Expectations() {
      {
        oneOf(reflectionService).getReturnType(method);
        will(returnValue(MockSubResource.class));
        oneOf(reflectionService).getAnnotation(method, ReferencedBy.class);
        will(returnValue(AnnotationUtils.referencedByAnnotation(Object.class)));
        oneOf(reflectionService).getAnnotation(method, TemplateResolver.class);
        will(returnValue(hasTemplateResolver ? methodTemplateResolver : null));
      }
    };
  }

  private Expectations methodDescriptorExpectations(
      final Method method, final boolean hasTemplateResolver) throws Exception {
    return new Expectations() {
      {
        oneOf(descriptorFactory).newDescriptor(
            with(method),
            with(PARENT_PATH + RESOURCE_PATH),
            with(MODEL_PATH.concat(Object.class)),
            with(any(PathTemplateResolver.class)));
        will(returnValue(methodDescriptor));
      }
    };
  }

  public static class MockResource {

    public MockSuperResource abstractLocator() { return null; }

    public MockSubResource locator() { return null; }

    public Object resource() { return null; }

    public void notResourceOrLocator() { }

  }

  public static abstract class MockSuperResource {
  }

  public static class MockSubResource extends MockSuperResource {
  }

  public static class MockOtherSubResource extends MockSuperResource {
  }

  public static class MockTemplateResolver implements PathTemplateResolver {
    @Override
    public String resolve(String template, ViewContext context)
        throws AmbiguousPathResolutionException {
      return null;
    }
  }

}
