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

import javax.enterprise.util.AnnotationLiteral;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.soulwing.prospecto.jaxrs.api.ModelPathSpec;
import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;
import org.soulwing.prospecto.jaxrs.api.TemplateResolver;

/**
 * Static utility methods for creating annotation instances.
 *
 * @author Carl Harris
 */
public class AnnotationUtils {

  /**
   * Creates a new instance of {@link Path}.
   * @param value path for value attribute
   * @return annotation instance
   */
  public static Path pathAnnotation(final String value) {
    return new PathLiteral() {
      public String value() {
        return value;
      }
    };
  }

  /**
   * Creates a new instance of {@link ModelPathSpec}.
   * @param value class array for value attribute
   * @return annotation instance
   */
  public static ModelPathSpec referencedByAnnotation(final Class<?>... value) {
    return referencedByAnnotation(true, value);
  }

  /**
   * Creates a new instance of {@link ModelPathSpec}.
   * @param descriptor descriptor flag value
   * @param value class array for value attribute
   * @return annotation instance
   */
  public static ModelPathSpec referencedByAnnotation(final boolean descriptor,
      final Class<?>... value) {
    return new ReferencedByLiteral() {
      @Override
      public Class<?>[] value() {
        return value;
      }

      @Override
      public boolean descriptor() {
        return descriptor;
      }

      @Override
      public boolean inherit() {
        return true;
      }
    };
  }

  /**
   * Creates a new instance of {@link TemplateResolver}.
   * @param value class for value attribute
   * @return annotation instance
   */
  public static TemplateResolver templateResolverAnnotation(
      final Class<? extends PathTemplateResolver> value) {
    return new TemplateResolverLiteral() {
      @Override
      public Class<? extends PathTemplateResolver> value() {
        return value;
      }
    };
  }

  /**
   * Creates a new instance of {@link GET}.
   * @return annotation instance
   */
  public static GET httpGETAnnotation() {
    return new GETLiteral() {};
  }

  public static abstract class PathLiteral extends AnnotationLiteral<Path>
      implements Path {
  }

  public static abstract class ReferencedByLiteral
      extends AnnotationLiteral<ModelPathSpec> implements ModelPathSpec {
  }

  public static abstract class TemplateResolverLiteral
      extends AnnotationLiteral<TemplateResolver> implements TemplateResolver {
  }

  public static abstract class GETLiteral
      extends AnnotationLiteral<GET> implements GET {
  }

}
