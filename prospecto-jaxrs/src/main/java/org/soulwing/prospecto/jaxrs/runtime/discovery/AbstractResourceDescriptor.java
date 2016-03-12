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

import org.soulwing.prospecto.jaxrs.runtime.path.ModelPath;
import org.soulwing.prospecto.jaxrs.api.PathTemplateResolver;
import org.soulwing.prospecto.jaxrs.runtime.ResourceDescriptor;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModel;
import org.soulwing.prospecto.jaxrs.runtime.glob.AnyModelSequence;
import org.soulwing.prospecto.jaxrs.runtime.glob.GlobMatcher;

/**
 * An abstract base for {@link ResourceDescriptor} implementations
 *
 * @author Carl Harris
 */
abstract class AbstractResourceDescriptor implements ResourceDescriptor {

  private final String path;
  private final ModelPath referencedBy;
  private final GlobMatcher<Class<?>> matcher;
  private final PathTemplateResolver templateResolver;

  /**
   * Constructs a new instance.
   * @param path resource path template
   * @param referencedBy model path
   * @param templateResolver path template resolver
   */
  public AbstractResourceDescriptor(String path, ModelPath referencedBy,
      PathTemplateResolver templateResolver) {
    this.path = path;
    this.referencedBy = referencedBy;
    this.templateResolver = templateResolver;
    this.matcher = GlobMatcher.with(AnyModel.class, AnyModelSequence.class,
        referencedBy.asArray());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String path() {
    return path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelPath referencedBy() {
    return referencedBy;
  }

  @Override
  public boolean matches(Class<?>... modelPath) {
    return matcher.matches(modelPath);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean matches(ModelPath modelPath) {
    return matches(modelPath.asArray());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PathTemplateResolver templateResolver() {
    return templateResolver;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(referencedBy);
    sb.append(" => ");
    sb.append(path);
    sb.append(" [");
    sb.append(resourceType());
    sb.append("=");
    sb.append(resourceName());
    sb.append(", resolver=");
    sb.append(templateResolver.getClass().getSimpleName());
    sb.append("]");
    return sb.toString();
  }

  private String modelClassAsString(Class<?> modelClass) {
    if (AnyModelSequence.class.equals(modelClass)) {
      return "*";
    }
    else if (AnyModel.class.equals(modelClass)) {
      return "?";
    }
    else {
      return modelClass.getSimpleName();
    }
  }

  protected abstract String resourceType();

  protected abstract String resourceName();

}
