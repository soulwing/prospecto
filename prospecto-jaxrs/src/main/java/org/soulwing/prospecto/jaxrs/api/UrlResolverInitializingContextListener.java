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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.ws.rs.core.Application;

import org.soulwing.prospecto.UrlResolverProducer;

/**
 * A {@link ServletContextListener} that initializes the
 * {@link UrlResolverProducer}.
 *
 * @author Carl Harris
 */
public class UrlResolverInitializingContextListener
    implements ServletContextListener {

  static final String APPLICATION_SERVLET_NAME = "javax.ws.rs.core.Application";

  @Override
  public void contextInitialized(ServletContextEvent event) {
    final Map<String, Object> properties = new HashMap<>();

    final String path = findJaxrsApplicationPath(event.getServletContext());
    if (path == null) {
      throw new IllegalStateException("cannot determine JAX-RS application path");
    }

    properties.put("servletContext", event.getServletContext());
    properties.put("applicationPath", path);
    UrlResolverProducer.init(properties);
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    UrlResolverProducer.destroy();
  }

  String findJaxrsApplicationPath(ServletContext context) {
    final Map<String, ? extends ServletRegistration> registrations =
        context.getServletRegistrations();
    for (final ServletRegistration registration : registrations.values()) {
      if (registration.getName().equals(APPLICATION_SERVLET_NAME)
          || isJaxRsApplication(registration, context)) {
        return context.getContextPath() + servletPath(registration);
      }
    }

    return null;
  }

  private String servletPath(ServletRegistration registration) {
    final String path = registration.getMappings().iterator().next();
    if (path.endsWith(("/*"))) {
      return path.substring(0, path.length() - 2);
    }
    return path;
  }

  private boolean isJaxRsApplication(ServletRegistration registration,
      ServletContext context) {
    try {
      final Class<?> servletClass = context.getClassLoader().loadClass(
          registration.getClassName());
      return Application.class.isAssignableFrom(servletClass);
    }
    catch (ClassNotFoundException ex) {
      return false;
    }
  }

}
