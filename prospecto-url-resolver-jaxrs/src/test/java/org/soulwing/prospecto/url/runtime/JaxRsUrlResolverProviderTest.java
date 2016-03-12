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
package org.soulwing.prospecto.url.runtime;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for {@link JaxRsUrlResolverProvider}.
 * @author Carl Harris
 */
public class JaxRsUrlResolverProviderTest {

  private static final String PATH ="/some/application/path";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ServletContext servletContext;

  @Mock
  private ResourceDiscoveryService discoveryService;

  private JaxRsUrlResolverProvider provider;

  private Path contextRoot;
  private Path webInf;
  private Path webInfClasses;

  @Before
  public void setUp() throws Exception {
    provider = new JaxRsUrlResolverProvider(discoveryService);
    contextRoot = Files.createTempDirectory(getClass().getSimpleName());
    webInf = Paths.get(contextRoot.toString(), "WEB-INF");
    webInfClasses = Paths.get(webInf.toString(), "classes");
    Files.createDirectory(webInf);
    Files.createDirectory(webInfClasses);
  }

  @After
  public void tearDown() throws Exception {
    Files.delete(webInfClasses);
    Files.delete(webInf);
    Files.delete(contextRoot);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitWithNoServletContext() throws Exception {
    final Map<String, Object> properties = new HashMap<>();
    properties.put(JaxRsUrlResolverProvider.APPLICATION_PATH, PATH);
    provider.init(properties);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitWithNoApplicationPath() throws Exception {
    final Map<String, Object> properties = new HashMap<>();
    properties.put(JaxRsUrlResolverProvider.SERVLET_CONTEXT, servletContext);
    provider.init(properties);
  }

  @Test
  public void testInit() throws Exception {
    context.checking(new Expectations() {
      {
        allowing(servletContext).getRealPath("/WEB-INF/classes");
        will(returnValue(webInfClasses.toString()));
        allowing(servletContext).getResourcePaths("/WEB-INF/lib");
        will(returnValue(Collections.emptySet()));
        allowing(discoveryService).discoverResources(with(PATH),
            with(any(ReflectionService.class)));
        will(returnValue(Collections.emptyList()));
      }
    });

    final Map<String, Object> properties = new HashMap<>();
    properties.put(JaxRsUrlResolverProvider.APPLICATION_PATH, PATH);
    properties.put(JaxRsUrlResolverProvider.SERVLET_CONTEXT, servletContext);
    provider.init(properties);
  }

}
