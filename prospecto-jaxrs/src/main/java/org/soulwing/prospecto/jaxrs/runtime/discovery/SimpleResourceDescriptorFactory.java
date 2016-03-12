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
 * A {@link ResourceDescriptorFactory} that creates descriptors using their
 * public constructors.
 *
 * @author Carl Harris
 */
public class SimpleResourceDescriptorFactory implements ResourceDescriptorFactory {

  @Override
  public ResourceDescriptor newDescriptor(Class<?> type,
      String resourcePath, ModelPath modelPath,
      PathTemplateResolver templateResolver) {
    return new ResourceTypeDescriptor(type, resourcePath, modelPath,
        templateResolver);
  }

  @Override
  public ResourceDescriptor newDescriptor(Method method,
      String resourcePath, ModelPath modelPath, PathTemplateResolver
      templateResolver) {
    return new ResourceMethodDescriptor(method, resourcePath, modelPath,
        templateResolver);
  }

}
