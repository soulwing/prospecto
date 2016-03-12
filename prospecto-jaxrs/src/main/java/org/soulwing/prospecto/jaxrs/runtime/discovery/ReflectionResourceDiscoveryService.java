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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;
import org.soulwing.prospecto.jaxrs.runtime.ReflectionService;
import org.soulwing.prospecto.jaxrs.runtime.ResourceConfigurationException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDiscoveryService;
import org.soulwing.prospecto.jaxrs.api.TemplateResolver;

/**
 * A concrete {@link ResourceDiscoveryService}.
 *
 * @author Carl Harris
 */
public class ReflectionResourceDiscoveryService
    implements ResourceDiscoveryService {

  private static final Logger logger = LoggerFactory.getLogger(
      ReflectionResourceDiscoveryService.class);

  private final ResourceTypeIntrospector typeIntrospector;

  /**
   * Constructs a new instance that will use the default type introspector.
   */
  public ReflectionResourceDiscoveryService() {
    this(new ReflectionResourceTypeIntrospector());
  }

  /**
   * Constructs a new instance that will use the given type introspector.
   * @param typeIntrospector type introspector
   */
  ReflectionResourceDiscoveryService(ResourceTypeIntrospector typeIntrospector) {
    this.typeIntrospector = typeIntrospector;
  }

  @Override
  public Collection<ResourceDescriptor> discoverResources(
      String applicationPath, ReflectionService reflectionService)
      throws ResourceConfigurationException {

    logger.debug("resource discovery started");
    final ModelPath modelPath = ModelPath.with();
    final Set<Class<?>> rootResourceTypes =
        reflectionService.getTypesAnnotatedWith(Path.class);
    final Collection<ResourceDescriptor> descriptors = new ArrayList<>();

    for (Class<?> rootResourceType : rootResourceTypes) {
      final Path path = reflectionService.getAnnotation(rootResourceType,
          Path.class);

      final String qualifiedPath = UriBuilder.fromPath(applicationPath)
          .path(path.value()).toTemplate();

      logger.trace("discovered root resource {}",
          rootResourceType.getSimpleName());

      TemplateResolver templateResolver = reflectionService.getAnnotation(
          rootResourceType, TemplateResolver.class);

      descriptors.addAll(typeIntrospector.describe(rootResourceType,
          qualifiedPath, modelPath,
          templateResolver, reflectionService));
    }
    logger.debug("resource discovery completed");
    return descriptors;
  }

}
