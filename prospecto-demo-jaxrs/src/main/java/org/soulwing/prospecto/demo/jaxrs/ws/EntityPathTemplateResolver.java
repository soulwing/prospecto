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
package org.soulwing.prospecto.demo.jaxrs.ws;

import java.util.HashMap;
import java.util.Map;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.domain.AbstractEntity;
import org.soulwing.prospecto.jaxrs.api.MapPathTemplateResolver;

/**
 * A {@link PathTemplateResolver} that resolves a simple entity path
 * containing an ID.
 *
 * @author Carl Harris
 */
public class EntityPathTemplateResolver extends MapPathTemplateResolver {

  static final String ID_KEY = "id";

  /**
   * Creates a map of named values used to resolve placeholders in a
   * path template.
   * @param context template context
   * @return map
   */
  protected Map<String, Object> templateMap(ViewContext context) {
    Map<String, Object> map = new HashMap<>();
    map.put(ID_KEY, context.get(AbstractEntity.class).getId());
    return map;
  }

}
