/*
 * File created on Apr 15, 2016
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
package org.soulwing.prospecto.jaxrs.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.soulwing.prospecto.jaxrs.api.UrlResolverInitializingContextListener.APPLICATION_SERVLET_NAME;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.ws.rs.core.Application;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for {@link UrlResolverInitializingContextListener}.
 * 
 * @author Carl Harris
 */
public class UrlResolverInitializingContextListenerTest {

  private static final String SERVLET_NAME = "mockServlet";

  private static final String CONTEXT_PATH = "/contextPath";

  private static final String SERVLET_PATH = "/servletPath";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private ServletContext servletContext;
  
  @Mock
  private ServletRegistration registration;

  private UrlResolverInitializingContextListener listener =
      new UrlResolverInitializingContextListener();

  @Test
  public void testFindPathWhenDefaultApplication() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(servletContext).getServletRegistrations();
        will(returnValue(Collections.singletonMap(APPLICATION_SERVLET_NAME,
            registration)));
        oneOf(registration).getName();
        will(returnValue(APPLICATION_SERVLET_NAME));
        oneOf(registration).getMappings();
        will(returnValue(Collections.singleton(SERVLET_PATH)));
        oneOf(servletContext).getContextPath();
        will(returnValue(CONTEXT_PATH));
      }
    });

    assertThat(listener.findJaxrsApplicationPath(servletContext),
        is(equalTo(CONTEXT_PATH + SERVLET_PATH)));
  }

  @Test
  public void testFindPathWhenApplicationSubtype() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(servletContext).getServletRegistrations();
        will(returnValue(Collections.singletonMap(SERVLET_NAME,
            registration)));
        oneOf(registration).getName();
        will(returnValue(SERVLET_NAME));
        oneOf(registration).getClassName();
        will(returnValue(MockApplication.class.getName()));
        oneOf(servletContext).getClassLoader();
        will(returnValue(MockApplication.class.getClassLoader()));
        oneOf(registration).getMappings();
        will(returnValue(Collections.singleton(SERVLET_PATH)));
        oneOf(servletContext).getContextPath();
        will(returnValue(CONTEXT_PATH));
      }
    });

    assertThat(listener.findJaxrsApplicationPath(servletContext),
        is(equalTo(CONTEXT_PATH + SERVLET_PATH)));
  }

  public static class MockApplication extends Application {
  }

}
