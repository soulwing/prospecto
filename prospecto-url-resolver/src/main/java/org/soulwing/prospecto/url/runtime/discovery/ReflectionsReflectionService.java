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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.reflections.Reflections;
import org.soulwing.prospecto.url.runtime.ReflectionService;

/**
 * A {@link ReflectionService} that delegates to the {@link Reflections}
 * utility and the Java Reflection API.
 *
 * @author Carl Harris
 */
public class ReflectionsReflectionService implements ReflectionService {

  private final Reflections reflections;
  
  /**
   * Constructs a new instance.
   * @param reflections
   */
  public ReflectionsReflectionService(Reflections reflections) {
    this.reflections = reflections;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
    return reflections.getSubTypesOf(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Class<?>> getTypesAnnotatedWith(
      Class<? extends Annotation> annotation) {
    return reflections.getTypesAnnotatedWith(annotation);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <A extends Annotation> A getAnnotation(Class<?> subjectType,
      Class<A> annotationType) {
    return subjectType.getAnnotation(annotationType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <A extends Annotation> A getAnnotation(Method subjectMethod,
      Class<A> annotationType) {
    return subjectMethod.getAnnotation(annotationType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAbstractType(Class<?> type) {
    return type.isInterface() || (type.getModifiers() & Modifier.ABSTRACT) != 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Method[] getMethods(Class<?> type) {
    return type.getMethods();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getReturnType(Method method) {
    return method.getReturnType();
  }

}
