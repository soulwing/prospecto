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

import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.ResourceConfigurationException;

/**
 * Static utility method for creating a template resolver.
 *
 * @author Carl Harris
 */
public class TemplateResolverUtils {

  public static PathTemplateResolver newResolver(
      Class<? extends PathTemplateResolver> resolverClass)
      throws ResourceConfigurationException {
    try {
      return resolverClass.newInstance();
    }
    catch (InstantiationException | IllegalAccessException ex) {
      throw new ResourceConfigurationException(
          "failed to create resolver of type " + resolverClass.getSimpleName());
    }
  }

}
