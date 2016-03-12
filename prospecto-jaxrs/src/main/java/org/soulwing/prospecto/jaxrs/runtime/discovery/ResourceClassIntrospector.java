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

import java.util.Set;

import org.soulwing.prospecto.jaxrs.runtime.ReflectionService;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;

/**
 * An object that examines the JAX-RS annotations on a resource class to
 * discover all resource methods and their corresponding paths.
 *
 * @author Carl Harris
 */
interface ResourceClassIntrospector {
  
  /**
   * Initializes the recipient introspector.
   * @param reflectionService reflection service
   */
  void init(ReflectionService reflectionService);
  
  /**
   * Produces a set of resource method descriptors for the given resource
   * class.
   * <p>
   * An implementation of this method must traverse classes identified by
   * subresource locators to produce the complete set of resource methods 
   * reachable from the given resource class.
   * 
   * @param path path of the resource specified by {@code resourceClass}
   * @param resourceClass the subject resource class
   * @return set of resource method descriptors
   */
  Set<ResourceDescriptor> describe(String path, Class<?> resourceClass);
  
}
