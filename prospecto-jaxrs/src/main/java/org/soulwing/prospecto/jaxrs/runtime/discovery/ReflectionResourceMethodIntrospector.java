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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.jaxrs.api.ModelPathSpec;
import org.soulwing.prospecto.jaxrs.api.ModelPathSpecs;
import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;
import org.soulwing.prospecto.jaxrs.api.TemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.ReflectionService;
import org.soulwing.prospecto.jaxrs.runtime.ResourceConfigurationException;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;
import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;

/**
 * A {@link ResourceMethodIntrospector} that utilizes reflection.
 *
 * @author Carl Harris
 */
class ReflectionResourceMethodIntrospector
    implements ResourceMethodIntrospector {

  private static final Logger logger = LoggerFactory.getLogger(
      ReflectionResourceMethodIntrospector.class);


  private final ResourceDescriptorFactory descriptorFactory;

  /**
   * Constructs a new instance using the default resource descriptor factory.
   */
  ReflectionResourceMethodIntrospector() {
    this(new SimpleResourceDescriptorFactory());
  }

  /**
   * Constructs a new instance using the specified resource descriptor factory.
   * @param descriptorFactory descriptor factory
   */
  ReflectionResourceMethodIntrospector(
      ResourceDescriptorFactory descriptorFactory) {
    this.descriptorFactory = descriptorFactory;
  }

  @Override
  public Collection<ResourceDescriptor> describe(Method method,
      String resourcePath, ModelPath modelPath,
      TemplateResolver templateResolver,
      ReflectionService reflectionService,
      ResourceTypeIntrospector typeIntrospector)
      throws ResourceConfigurationException {

    final Path path = reflectionService.getAnnotation(method, Path.class);
    resourcePath = resourcePath(resourcePath, path);

    final boolean resourceMethod = isResourceMethod(method, reflectionService);

    if (path == null && !resourceMethod) {
      return Collections.emptyList();
    }

    TemplateResolver methodTemplateResolver = reflectionService.getAnnotation(
        method, TemplateResolver.class);

    Class<?> returnType = reflectionService.getReturnType(method);

    if (resourceMethod) {
      return describeResourceMethod(method, resourcePath, modelPath,
          templateResolver, reflectionService, methodTemplateResolver);
    }


    ModelPathSpec modelPathSpec = reflectionService.getAnnotation(method,
        ModelPathSpec.class);

    if (reflectionService.isAbstractType(returnType)) {
      if (modelPathSpec == null) return Collections.emptyList();

      returnType = findMatchingSubResourceType(modelPath.concat(modelPathSpec),
          reflectionService, method);
    }
    else if (modelPathSpec != null && modelPathSpec.inherit()) {
      modelPath = modelPath.concat(modelPathSpec);
    }

    if (methodTemplateResolver == null) {
      TemplateResolver typeTemplateResolver = reflectionService.getAnnotation(
          returnType, TemplateResolver.class);
      if (typeTemplateResolver != null) {
        templateResolver = typeTemplateResolver;
      }
    }
    else {
      templateResolver = methodTemplateResolver;
    }

    return typeIntrospector.describe(returnType, resourcePath, modelPath,
        templateResolver, reflectionService);
  }

  private Collection<ResourceDescriptor> describeResourceMethod(Method method,
      String resourcePath, ModelPath modelPath,
      TemplateResolver templateResolver, ReflectionService reflectionService,
      TemplateResolver methodTemplateResolver) {

    if (methodTemplateResolver != null) {
      templateResolver = methodTemplateResolver;
    }

    ModelPathSpec[] specs = getModelPathSpecs(method, reflectionService);

    if (specs.length == 0) {
      logger.trace("ignoring method {}", methodToString(method));
      return Collections.emptyList();
    }

    if (templateResolver == null) {
      throw new ResourceConfigurationException(
          "no template resolver for method " + methodToString(method));
    }

    final PathTemplateResolver pathTemplateResolver = TemplateResolverUtils
        .newResolver(templateResolver.value());

    final List<ResourceDescriptor> descriptors = new ArrayList<>();
    for (final ModelPathSpec spec : specs) {
      final ModelPath path = spec.inherit() ? modelPath.concat(spec) : modelPath;

      descriptors.add(descriptorFactory.newDescriptor(method,
          resourcePath, path, pathTemplateResolver));
    }
    return descriptors;
  }

  private ModelPathSpec[] getModelPathSpecs(Method method,
      ReflectionService reflectionService) {

    final ModelPathSpecs specs =
        reflectionService.getAnnotation(method, ModelPathSpecs.class);

    final ModelPathSpec spec = reflectionService.getAnnotation(method,
        ModelPathSpec.class);

    if (specs != null && spec == null) {
      return specs.value();
    }
    if (specs == null && spec != null) {
      return new ModelPathSpec[] { spec };
    }
    if (specs == null) {
      return new ModelPathSpec[0];
    }

    throw new ResourceConfigurationException("cannot use both @"
        + ModelPathSpecs.class.getSimpleName() + " and @"
        + ModelPathSpec.class.getSimpleName() + " on the same method");
  }

  /**
   * Determines whether the given method is annotated with an HTTP method
   * annotation.
   * @param method the method to examine
   * @param reflector reflection service
   * @return {@code true} if {@code method} has at least one HTTP method
   *    annotation
   */
  private boolean isResourceMethod(Method method,
      ReflectionService reflector) {
    return reflector.getAnnotation(method, GET.class) != null
        || reflector.getAnnotation(method, POST.class) != null
        || reflector.getAnnotation(method, PUT.class) != null
        || reflector.getAnnotation(method, DELETE.class) != null
        || reflector.getAnnotation(method, HEAD.class) != null
        || reflector.getAnnotation(method, OPTIONS.class) != null;
  }

  /**
   * Finds the unique concrete subtype of the return type of the given method
   * whose referenced-by annotation is equivalent to the referenced-by
   * annotation of the method.
   *
   * @param modelPath model path to match
   * @param reflectionService reflection service  @return matching sub type
   * @param method the subject method
   * @throws ResourceConfigurationException if the number of concrete types
   *    that satisfy the above criterion is not equal to 1
   */
  private Class<?> findMatchingSubResourceType(ModelPath modelPath,
      ReflectionService reflectionService, Method method) {

    final List<Class<?>> types = new ArrayList<>();
    final Class<?> returnType = reflectionService.getReturnType(method);
    final Class<?>[] methodReferences = modelPath.asArray();
    for (Class<?> type : reflectionService.getSubTypesOf(returnType)) {
      ModelPathSpec typeModelPathSpec = reflectionService.getAnnotation(type,
          ModelPathSpec.class);
      if (typeModelPathSpec == null) continue;
      final Class<?>[] typeReferences = typeModelPathSpec.value();
      if (!Arrays.equals(typeReferences, methodReferences)) continue;
      types.add(type);
    }

    final int numTypes = types.size();
    if (numTypes == 0) {
      throw new ResourceConfigurationException("there is no subtype of "
          + returnType.getSimpleName() + " with a @"
          + ModelPathSpec.class.getSimpleName() + " that matches "
          + modelPath + " at " + methodToString(method));
    }
    else if (numTypes > 1) {
      throw new ResourceConfigurationException(
          "there is more than one subtype of "
          + returnType.getSimpleName() + " with a @"
          + ModelPathSpec.class.getSimpleName() + " that matches "
          + modelPath + " at " + method);
    }

    return types.get(0);

  }

  /**
   * Creates a new resource path from a parent path and the path specified
   * by a {@link Path} annotation.
   * @param parent parent path
   * @param path path annotation whose value is to be appended
   * @return {@code parent} with the value of {@link Path} appended to it
   */
  private String resourcePath(String parent, Path path) {
    UriBuilder uriBuilder = UriBuilder.fromUri(parent);
    if (path != null) {
      uriBuilder.path(path.value());
    }
    return uriBuilder.toTemplate();
  }


  private String methodToString(Method method) {
    return method.getDeclaringClass().getSimpleName() + "." + method.getName();
  }

}