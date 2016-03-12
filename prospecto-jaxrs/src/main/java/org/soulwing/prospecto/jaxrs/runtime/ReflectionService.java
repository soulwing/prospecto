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
package org.soulwing.prospecto.jaxrs.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * A service that provides reflection (introspection) support.
 *
 * @author Carl Harris
 */
public interface ReflectionService {

  /**
   * Gets the set of classes that are subtypes of a given type.
   * @param type the base type
   * @return set of types that implement/extend {@code type}
   */
  <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type);

  /**
   * Gets the set of classes that have a given (type-level) annotation.
   * @param annotation the subject annotation
   * @return set of classes
   */
  Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation);

  /**
   * Gets an annotation on a type.
   * @param subjectType the subject type
   * @param annotationType class representating the annotation type
   * @param <A> annotation type
   * @return annotation instance or {@code null} if there is no annotation of
   *    type {@code A} on {@code subjectType}
   */
  <A extends Annotation> A getAnnotation(Class<?> subjectType,
      Class<A> annotationType);

  /**
   * Gets an annotation on a method.
   * @param subjectMethod the subject method
   * @param annotationType class representating the annotation type
   * @param <A> annotation type
   * @return annotation instance or {@code null} if there is no annotation of
   *    type {@code A} on {@code subjectType}
   */
  <A extends Annotation> A getAnnotation(Method subjectMethod,
      Class<A> annotationType);

  /**
   * Tests whether the given type is an interface or abstract class.
   * @param type the subject type
   * @return {@code true} if {@code type} is an interface or an abstract class
   */
  boolean isAbstractType(Class<?> type);

  /**
   * Gets the public methods of the given type.
   * @param type the subject type
   * @return array containing the public methods of {@code type}
   */
  Method[] getMethods(Class<?> type);

  /**
   * Gets the return type of the given method.
   * @param method the subject method
   * @return return type
   */
  Class<?> getReturnType(Method method);

}
