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
package org.soulwing.prospecto.jaxrs.runtime;

import java.util.Map;

import javax.servlet.ServletContext;

import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.jaxrs.runtime.discovery.ReflectionResourceDiscoveryService;
import org.soulwing.prospecto.jaxrs.runtime.discovery.ServletContextReflectionService;
import org.soulwing.prospecto.jaxrs.runtime.resolver.ConfigurableUrlResolver;
import org.soulwing.prospecto.jaxrs.runtime.resolver.ResourceDescriptorUrlResolver;
import org.soulwing.prospecto.spi.UrlResolverProvider;

/**
 * A {@link UrlResolverProvider} designed for use in a web application that
 * utilizes JAX-RS in a standard {@link ServletContext}.
 *
 * @author Carl Harris
 */
public class JaxRsUrlResolverProvider implements UrlResolverProvider {

  /**
   * Configuration property that provides the JAX-RS ServletContext
   */
  public static final String SERVLET_CONTEXT = "servletContext";

  /**
   * Configuration property that provides the JAX-RS application path
   */
  public static final String APPLICATION_PATH = "applicationPath";

  private final ResourceDiscoveryService resourceDiscoveryService;

  private UrlResolver resolver;

  public JaxRsUrlResolverProvider() {
    this(new ReflectionResourceDiscoveryService());
  }

  public JaxRsUrlResolverProvider(
      ResourceDiscoveryService resourceDiscoveryService) {
    this.resourceDiscoveryService = resourceDiscoveryService;
  }

  @Override
  public void init(Map<String, Object> properties) {
    final String applicationPath = getProperty(APPLICATION_PATH,
        String.class, properties);
    final ServletContext servletContext = getProperty(SERVLET_CONTEXT,
        ServletContext.class, properties);

    final ReflectionService reflectionService =
        new ServletContextReflectionService(servletContext);

    ConfigurableUrlResolver resolver =
        new ResourceDescriptorUrlResolver();

    resolver.addAll(resourceDiscoveryService.discoverResources(applicationPath,
        reflectionService));

    resolver.validate();

    this.resolver = resolver;
  }

  @SuppressWarnings("unchecked")
  private <T> T getProperty(String name, Class<T> type,
      Map<String, ?> properties) {
    final Object value = properties.get(name);
    if (!type.isInstance(value)) {
      throw new IllegalArgumentException("properties must contain a '"
          + name + "' property of '" + type.getSimpleName() + "' type");
    }
    return (T) value;
  }

  @Override
  public void destroy() {

  }

  @Override
  public UrlResolver getResolver() {
    if (resolver == null) {
      throw new IllegalStateException("resolver not initialized; "
          + "did you call " + UrlResolverProvider.class.getSimpleName() + ".init?");
    }
    return resolver;
  }

}
