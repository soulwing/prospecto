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
package org.soulwing.prospecto.jaxrs.runtime.resolver;

import java.util.Collection;

import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.jaxrs.runtime.ResourceConfigurationException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;

/**
 * A configurable {@link UrlResolver}.
 *
 * @author Carl Harris
 */
public interface ConfigurableUrlResolver extends UrlResolver {

  /**
   * Adds resource descriptors to the resolver.
   * @param descriptors the descriptor to add
   */
  void addAll(Collection<ResourceDescriptor> descriptors);

  /**
   * Validates the configuration of this resolver.
   * @throws ResourceConfigurationException if the resource configuration is
   *    invalid
   */
  void validate() throws ResourceConfigurationException;

}
