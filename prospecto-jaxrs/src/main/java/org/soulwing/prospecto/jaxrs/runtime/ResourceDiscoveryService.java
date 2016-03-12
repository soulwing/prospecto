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

import java.util.Collection;

/**
 * A service that discovers JAX-RS resources association model path references
 * with resource path templates.
 * s
 * @author Carl Harris
 */
public interface ResourceDiscoveryService {

  /**
   * Discovers JAX-RS resources, adding appropriate descriptors to the given
   * configurable resolver.
   *
   * @param applicationPath the context-qualified path to the JAX-RS application
   * @param reflectionService reflection service to use for discovery
   * @return resource descriptors
   * @throws ResourceConfigurationException if a configuration error is
   *    discovered
   */
  Collection<ResourceDescriptor> discoverResources(String applicationPath,
      ReflectionService reflectionService)
      throws ResourceConfigurationException;

}
