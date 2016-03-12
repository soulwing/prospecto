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
package org.soulwing.prospecto.url.runtime.discovery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.Collections;

import javax.ws.rs.Path;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.url.api.AmbiguousPathResolutionException;
import org.soulwing.prospecto.url.runtime.path.ModelPath;
import org.soulwing.prospecto.url.api.PathTemplateResolver;
import org.soulwing.prospecto.url.runtime.ReflectionService;
import org.soulwing.prospecto.url.runtime.ResourceDescriptor;
import org.soulwing.prospecto.url.api.TemplateResolver;
import org.soulwing.prospecto.url.runtime.resolver.ConfigurableUrlResolver;

/**
 * Unit tests for {@link ReflectionResourceDiscoveryService}.
 *
 * @author Carl Harris
 */
public class ReflectionResourceDiscoveryServiceTest {

  public static final String APPLICATION_PATH = "applicationPath";
  public static final String RESOURCE_PATH = "resourcePath";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ReflectionService reflectionService;

  @Mock
  private ResourceTypeIntrospector typeIntrospector;

  @Mock
  private ResourceDescriptor descriptor;

  @Mock
  private ConfigurableUrlResolver resolver;

  private ReflectionResourceDiscoveryService service;

  private TemplateResolver templateResolver =
      AnnotationUtils.templateResolverAnnotation(MockTemplateResolver.class);

  @Before
  public void setUp() throws Exception {
    service = new ReflectionResourceDiscoveryService(typeIntrospector);
  }

  @Test
  public void testDiscoverResources() throws Exception {

    context.checking(new Expectations() {
      {
        oneOf(reflectionService).getTypesAnnotatedWith(Path.class);
        will(returnValue(Collections.singleton(MockResource.class)));

        oneOf(reflectionService).getAnnotation(MockResource.class, Path.class);
        will(returnValue(AnnotationUtils.pathAnnotation(RESOURCE_PATH)));

        oneOf(reflectionService).getAnnotation(MockResource.class,
            TemplateResolver.class);
        will(returnValue(templateResolver));

        oneOf(typeIntrospector).describe(MockResource.class,
            APPLICATION_PATH + "/" + RESOURCE_PATH, ModelPath.with(),
            templateResolver, reflectionService);
        will(returnValue(Collections.singletonList(descriptor)));
      }
    });

    assertThat(service.discoverResources(APPLICATION_PATH, reflectionService),
        contains(descriptor));
  }

  public static class MockResource {
  }

  public static class MockTemplateResolver implements PathTemplateResolver {
    @Override
    public String resolve(String template, ViewContext context)
        throws AmbiguousPathResolutionException {
      return null;
    }
  }

}
