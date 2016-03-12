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
package org.soulwing.prospecto.url.runtime;

import org.soulwing.prospecto.url.runtime.path.ModelPath;
import org.soulwing.prospecto.url.api.PathTemplateResolver;
import org.soulwing.prospecto.url.api.ReferencedBy;
import org.soulwing.prospecto.url.api.TemplateResolver;


/**
 * A descriptor for a resource method.
 *
 * @author Carl Harris
 */
public interface ResourceDescriptor {

  /**
   * Gets the path to this resource method relative to the deployment context
   * path and JAX-RS application path.
   * @return resource path template
   */
  String path();

  /**
   * Gets the model path identified in a {@link ReferencedBy} annotation on
   * the described resource.
   * @return model path
   */
  ModelPath referencedBy();

  /**
   * Tests whether the given model path matches this method descriptor.
   * @param modelPath the model path to test
   * @return {@code true} if {@code modelPath} matches the path of model
   *    references associated with this method descriptor
   */
  boolean matches(Class<?>... modelPath);

  /**
   * Tests whether the given model path matches this method descriptor.
   * @param modelPath the model path to test
   * @return {@code true} if {@code modelPath} matches the path of model
   *    references associated with this method descriptor
   */
  boolean matches(ModelPath modelPath);

  /**
   * Gets a template resolver instance to use in resolving the path template
   * for the described resource method.
   * @return template resolver object of the type specified by the 
   *    {@link TemplateResolver} annotation on the described resource method
   */
  PathTemplateResolver templateResolver();
  
}
