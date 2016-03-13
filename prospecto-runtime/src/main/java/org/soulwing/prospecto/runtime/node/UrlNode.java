/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.runtime.node;

import org.soulwing.prospecto.UrlResolverProducer;
import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A view node that represents a value that is resolved as a URL.
 *
 * @author Carl Harris
 */
public class UrlNode extends ValueViewNode {

  public static final String DEFAULT_NAME = "href";

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   */
  public UrlNode(String name, String namespace) {
    super(name, namespace);
  }

  /**
   * Constructs a copy of a node, composed with a new name.
   * @param source source node that will be copied
   * @param name name to compose in the new node
   */
  private UrlNode(UrlNode source, String name) {
    super(name, source.getNamespace());
  }

  @Override
  protected View.Event.Type getEventType() {
    return View.Event.Type.URL;
  }

  @Override
  protected Object getModelValue(Object source, ScopedViewContext context)
      throws Exception {
    return context.get(UrlResolver.class).resolve(this, context);
  }

  @Override
  protected Object toViewValue(Object model, ViewContext context)
      throws Exception {
    return model;
  }

  @Override
  public UrlNode copy(String name) {
    return new UrlNode(this, name);
  }

}
