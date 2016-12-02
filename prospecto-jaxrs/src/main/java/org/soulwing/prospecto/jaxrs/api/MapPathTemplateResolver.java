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
package org.soulwing.prospecto.jaxrs.api;

import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;

/**
 * A template resolver that builds a URI from map of parameters.
 *
 * @author Carl Harris
 */
public abstract class MapPathTemplateResolver implements PathTemplateResolver {

  private static final Logger logger =
      LoggerFactory.getLogger(MapPathTemplateResolver.class);

  @Override
  public final String resolve(String template, ViewContext context) {
    Map<String, Object> templateMap = templateMap(context);
    try {
      return UriBuilder.fromPath(template)
          .buildFromMap(templateMap)
          .toString();
    } catch (IllegalArgumentException e) {
      logger.warn("Error resolving path for template {}", template);
      throw e;
    }
  }

  /**
   * Creates a map of named values used to resolve placeholders in a
   * path template.
   * @param context template context
   * @return map
   */
  protected abstract Map<String, Object> templateMap(ViewContext context);

}
