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
package org.soulwing.prospecto.url.runtime.discovery;

import org.soulwing.prospecto.url.runtime.path.ModelPath;
import org.soulwing.prospecto.url.api.PathTemplateResolver;
import org.soulwing.prospecto.url.runtime.ResourceDescriptor;

/**
 * A {@link ResourceDescriptor} for a JAX-RS resource type.
 *
 * @author Carl Harris
 */
class ResourceTypeDescriptor extends AbstractResourceDescriptor {

  private final Class<?> type;

  /**
   * Constructs a new instance.
   * @param type resource type
   * @param path resource path template
   * @param referencedBy model path
   * @param templateResolver path template resolver
   */
  public ResourceTypeDescriptor(Class<?> type,
      String path, ModelPath referencedBy,
      PathTemplateResolver templateResolver) {
    super(path, referencedBy, templateResolver);
    this.type = type;
  }

  @Override
  protected String resourceType() {
    return "type";
  }

  @Override
  protected String resourceName() {
    return type.getSimpleName();
  }
  
}
