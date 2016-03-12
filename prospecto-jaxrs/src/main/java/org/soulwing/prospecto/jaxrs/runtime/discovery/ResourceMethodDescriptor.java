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

import java.lang.reflect.Method;

import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;
import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;

/**
 * A {@link ResourceDescriptor} for a JAX-RS resource method.
 *
 * @author Carl Harris
 */
class ResourceMethodDescriptor extends AbstractResourceDescriptor {

  private final Method method;

  /**
   * Constructs a new instance.
   * @param method resource method
   * @param path resource path template
   * @param referencedBy model path
   * @param templateResolver path template resolver
   */
  public ResourceMethodDescriptor(Method method,
      String path, ModelPath referencedBy,
      PathTemplateResolver templateResolver) {
    super(path, referencedBy, templateResolver);
    this.method = method;
  }

  @Override
  protected String resourceType() {
    return "method";
  }

  @Override
  protected String resourceName() {
    return method.getDeclaringClass().getSimpleName() + "." + method.getName();
  }

}
