/*
 * File created on Mar 11, 2016
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
package org.soulwing.prospecto.jaxrs.runtime.resolver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.api.url.UrlDecorators;
import org.soulwing.prospecto.jaxrs.api.AmbiguousPathResolutionException;
import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.ReflectionService;
import org.soulwing.prospecto.jaxrs.runtime.ResourceConfigurationException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;
import org.soulwing.prospecto.jaxrs.runtime.ResourceNotFoundException;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModel;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModelSequence;
import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;

/**
 * Unit tests for {@link ResourceDescriptorUrlResolver}.
 *
 * @author Carl Harris
 */
public class ResourceDescriptorUrlResolverTest {
  
  private static final String APP_PATH = "/appPath";
  
  private static final String PATH = "pathTemplate";

  private static final String DECORATED_PATH = "decoratedPathTemplate";

  private static final String RESOLVED_PATH = "resolvedPath";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private ResourceDescriptor descriptor;
  
  @Mock
  private PathTemplateResolver templateResolver;
  
  @Mock
  private ViewContext viewContext;

  @Mock
  private ViewNode viewNode;
  
  @Mock
  private ReflectionService reflectionService;

  @Mock
  private UrlDecorators decorators;

  private ResourceDescriptorUrlResolver resolver = new ResourceDescriptorUrlResolver();

  @Test
  public void testResolve() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).currentModelPath();
        will(returnValue(Collections.singletonList(Object.class)));
        oneOf(descriptor).matches(ModelPath.with(Object.class));
        will(returnValue(true));
        oneOf(descriptor).path();
        will(returnValue(PATH));
        oneOf(viewContext).getUrlDecorators();
        will(returnValue(decorators));
        oneOf(decorators).decorate(PATH, viewNode, viewContext);
        will(returnValue(DECORATED_PATH));
        oneOf(descriptor).templateResolver();
        will(returnValue(templateResolver));
        oneOf(templateResolver).resolve(DECORATED_PATH, viewContext);
        will(returnValue(RESOLVED_PATH));
      }
    });

    resolver.addDescriptor(descriptor);
    assertThat(resolver.resolve(viewNode, viewContext),
        is(equalTo(RESOLVED_PATH)));
  }

  @Test(expected = AmbiguousPathResolutionException.class)
  public void testResolveWhenAmbiguous() throws Exception {
    final ResourceDescriptor descriptor1 =
        context.mock(ResourceDescriptor.class, "descriptor1");
    final ResourceDescriptor descriptor2 =
        context.mock(ResourceDescriptor.class, "descriptor2");

    final ModelPath modelPath = ModelPath.with(Object.class);
    context.checking(new Expectations() {
      {
        oneOf(viewContext).currentModelPath();
        will(returnValue(Arrays.asList(Object.class)));
        allowing(descriptor1).matches(modelPath);
        will(returnValue(true));
        allowing(descriptor2).matches(modelPath);
        will(returnValue(true));
        allowing(descriptor1).modelPath();
        will(returnValue(modelPath));
        allowing(descriptor2).modelPath();
        will(returnValue(modelPath));
      }
    });

    resolver.addDescriptor(descriptor1);
    resolver.addDescriptor(descriptor2);
    assertThat(resolver.resolve(viewNode, viewContext),
        is(equalTo(PATH)));
  }

  @Test
  public void testResolveBestMatchLongerPath() throws Exception {
    final ResourceDescriptor descriptor1 =
        context.mock(ResourceDescriptor.class, "descriptor1");
    final ResourceDescriptor descriptor2 =
        context.mock(ResourceDescriptor.class, "descriptor2");

    final ModelPath modelPath1 = ModelPath.with(Object.class, Object.class);
    final ModelPath modelPath2 = ModelPath.with(AnyModelSequence.class);

    context.checking(new Expectations() {
      {
        oneOf(viewContext).currentModelPath();
        will(returnValue(Arrays.asList(Object.class, Object.class)));

        allowing(descriptor1).modelPath();
        will(returnValue(modelPath1));
        allowing(descriptor2).modelPath();
        will(returnValue(modelPath2));

        allowing(descriptor2).matches(ModelPath.with(Object.class, Object.class));
        will(returnValue(true));
        allowing(descriptor1).matches(ModelPath.with(Object.class, Object.class));
        will(returnValue(true));

        oneOf(descriptor1).templateResolver();
        will(returnValue(templateResolver));

        oneOf(descriptor1).path();
        will(returnValue(PATH));

        oneOf(viewContext).getUrlDecorators();
        will(returnValue(decorators));
        oneOf(decorators).decorate(PATH, viewNode, viewContext);
        will(returnValue(DECORATED_PATH));

        oneOf(templateResolver).resolve(DECORATED_PATH, viewContext);
        will(returnValue(RESOLVED_PATH));
      }
    });

    resolver.addDescriptor(descriptor1);
    resolver.addDescriptor(descriptor2);
    assertThat(resolver.resolve(viewNode, viewContext),
        is(equalTo(RESOLVED_PATH)));
  }

  @Test
  public void testResolveBestMatchExactMatch() throws Exception {
    final ResourceDescriptor descriptor1 =
        context.mock(ResourceDescriptor.class, "descriptor1");
    final ResourceDescriptor descriptor2 =
        context.mock(ResourceDescriptor.class, "descriptor2");

    final ModelPath modelPath1 = ModelPath.with(Object.class);
    final ModelPath modelPath2 = ModelPath.with(Object.class,
        AnyModelSequence.class);

    context.checking(new Expectations() {
      {
        oneOf(viewContext).currentModelPath();
        will(returnValue(Collections.singletonList(Object.class)));

        allowing(descriptor1).modelPath();
        will(returnValue(modelPath1));
        allowing(descriptor2).modelPath();
        will(returnValue(modelPath2));

        allowing(descriptor2).matches(ModelPath.with(Object.class));
        will(returnValue(true));
        allowing(descriptor1).matches(ModelPath.with(Object.class));
        will(returnValue(true));

        oneOf(descriptor1).templateResolver();
        will(returnValue(templateResolver));

        oneOf(descriptor1).path();
        will(returnValue(PATH));

        oneOf(viewContext).getUrlDecorators();
        will(returnValue(decorators));
        oneOf(decorators).decorate(PATH, viewNode, viewContext);
        will(returnValue(DECORATED_PATH));


        oneOf(templateResolver).resolve(DECORATED_PATH, viewContext);
        will(returnValue(RESOLVED_PATH));
      }
    });

    resolver.addDescriptor(descriptor1);
    resolver.addDescriptor(descriptor2);
    assertThat(resolver.resolve(viewNode, viewContext),
        is(equalTo(RESOLVED_PATH)));
  }

  @Test
  public void testResolveBestMatchAnyWildcard() throws Exception {
    final ResourceDescriptor descriptor1 =
        context.mock(ResourceDescriptor.class, "descriptor1");
    final ResourceDescriptor descriptor2 =
        context.mock(ResourceDescriptor.class, "descriptor2");

    final ModelPath modelPath1 = ModelPath.with(Object.class);
    final ModelPath modelPath2 = ModelPath.with(AnyModel.class);

    context.checking(new Expectations() {
      {
        oneOf(viewContext).currentModelPath();
        will(returnValue(Collections.singletonList(Object.class)));

        allowing(descriptor1).modelPath();
        will(returnValue(modelPath1));
        allowing(descriptor2).modelPath();
        will(returnValue(modelPath2));

        allowing(descriptor2).matches(ModelPath.with(Object.class));
        will(returnValue(true));
        allowing(descriptor1).matches(ModelPath.with(Object.class));
        will(returnValue(true));

        oneOf(descriptor1).templateResolver();
        will(returnValue(templateResolver));

        oneOf(descriptor1).path();
        will(returnValue(PATH));

        oneOf(viewContext).getUrlDecorators();
        will(returnValue(decorators));
        oneOf(decorators).decorate(PATH, viewNode, viewContext);
        will(returnValue(DECORATED_PATH));

        oneOf(templateResolver).resolve(DECORATED_PATH, viewContext);
        will(returnValue(RESOLVED_PATH));
      }
    });

    resolver.addDescriptor(descriptor1);
    resolver.addDescriptor(descriptor2);
    assertThat(resolver.resolve(viewNode, viewContext),
        is(equalTo(RESOLVED_PATH)));
  }

  @Test
  public void testResolveBestMatchAnySequenceWildcard() throws Exception {
    final ResourceDescriptor descriptor1 =
        context.mock(ResourceDescriptor.class, "descriptor1");
    final ResourceDescriptor descriptor2 =
        context.mock(ResourceDescriptor.class, "descriptor2");

    final ModelPath modelPath1 = ModelPath.with(AnyModel.class);
    final ModelPath modelPath2 = ModelPath.with(AnyModelSequence.class);

    context.checking(new Expectations() {
      {
        oneOf(viewContext).currentModelPath();
        will(returnValue(Collections.singletonList(Object.class)));

        allowing(descriptor1).modelPath();
        will(returnValue(modelPath1));
        allowing(descriptor2).modelPath();
        will(returnValue(modelPath2));

        allowing(descriptor2).matches(ModelPath.with(Object.class));
        will(returnValue(true));
        allowing(descriptor1).matches(ModelPath.with(Object.class));
        will(returnValue(true));

        oneOf(descriptor1).templateResolver();
        will(returnValue(templateResolver));

        oneOf(descriptor1).path();
        will(returnValue(PATH));

        oneOf(viewContext).getUrlDecorators();
        will(returnValue(decorators));
        oneOf(decorators).decorate(PATH, viewNode, viewContext);
        will(returnValue(DECORATED_PATH));

        oneOf(templateResolver).resolve(DECORATED_PATH, viewContext);
        will(returnValue(RESOLVED_PATH));
      }
    });

    resolver.addDescriptor(descriptor1);
    resolver.addDescriptor(descriptor2);
    assertThat(resolver.resolve(viewNode, viewContext),
        is(equalTo(RESOLVED_PATH)));
  }

  @Test(expected = ResourceNotFoundException.class)
  public void testResolveWhenNotFound() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).currentModelPath();
        will(returnValue(Collections.emptyList()));
      }
    });
    resolver.resolve(viewNode, viewContext);
  }

  @Test(expected = ResourceConfigurationException.class)
  public void testValidateWithExactDuplicate() throws Exception {
    final ResourceDescriptor descriptor1 =
        context.mock(ResourceDescriptor.class, "descriptor1");
    final ResourceDescriptor descriptor2 =
        context.mock(ResourceDescriptor.class, "descriptor2");

    context.checking(new Expectations() {
      {
        allowing(descriptor1).modelPath();
        will(returnValue(ModelPath.with(Object.class)));
        allowing(descriptor2).modelPath();
        will(returnValue(ModelPath.with(Object.class)));
      }
    });

    resolver.addDescriptor(descriptor1);
    resolver.addDescriptor(descriptor2);
    resolver.validate();
  }

  @Test
  public void testValidateWithNoDuplicate() throws Exception {
    final ResourceDescriptor descriptor1 =
        context.mock(ResourceDescriptor.class, "descriptor1");
    final ResourceDescriptor descriptor2 =
        context.mock(ResourceDescriptor.class, "descriptor2");

    context.checking(new Expectations() {
      {
        allowing(descriptor1).modelPath();
        will(returnValue(ModelPath.with(Integer.class)));
        allowing(descriptor2).modelPath();
        will(returnValue(ModelPath.with(Long.class)));
      }
    });

    resolver.addDescriptor(descriptor1);
    resolver.addDescriptor(descriptor2);
    resolver.validate();
  }


}
