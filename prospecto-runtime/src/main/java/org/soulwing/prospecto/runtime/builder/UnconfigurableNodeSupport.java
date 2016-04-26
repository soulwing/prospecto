/*
 * File created on Mar 28, 2016
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
package org.soulwing.prospecto.runtime.builder;

import java.beans.Introspector;
import java.util.EnumSet;
import java.util.Map;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.converter.ValueTypeConverter;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.template.AbstractViewNode;

/**
 * Support for handling unsupported configuration of template builders.
 *
 * @author Carl Harris
 */
class UnconfigurableNodeSupport {

  private final String nodeType;

  UnconfigurableNodeSupport(AbstractViewNode node) {
    this.nodeType = nodeType(node);
  }

  private static String nodeType(AbstractViewNode node) {
    String nodeType = Introspector.decapitalize(node.getClass().getSimpleName());
    if (nodeType.endsWith("Node")) {
      nodeType = nodeType.substring(0, nodeType.length() - 4);
    }
    return nodeType;
  }

  public ViewTemplateBuilder discriminator() {
    return discriminator(null);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Object... configuration) {
    return discriminator(null);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder discriminator(
      Class<? extends DiscriminatorStrategy> discriminatorClass,
      Map configuration) {
    return discriminator(null);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder discriminator(DiscriminatorStrategy discriminator) {
    throw new ViewTemplateException("cannot set discriminator on " + nodeType);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder source(String name) {
    throw new ViewTemplateException("cannot set source on " + nodeType);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder accessType(AccessType accessType) {
    throw new ViewTemplateException("cannot set access type on " + nodeType);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder allow(AccessMode mode, AccessMode... modes) {
    return allow(EnumSet.of(mode, modes));
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder allow(EnumSet<AccessMode> modes) {
    throw new ViewTemplateException("cannot set access modes on " + nodeType);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Object... configuration) {
    return converter(null);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder converter(
      Class<? extends ValueTypeConverter> converterClass,
      Map configuration) {
    return converter(null);
  }

  @SuppressWarnings("unused")
  public ViewTemplateBuilder converter(ValueTypeConverter converter) {
    throw new ViewTemplateException("cannot set converter on " + nodeType);
  }

}
