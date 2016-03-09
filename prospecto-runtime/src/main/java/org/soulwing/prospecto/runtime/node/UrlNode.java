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

import java.util.Collections;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A view node that represents a value that is resolved as a URL.
 *
 * @author Carl Harris
 */
public class UrlNode implements EventGeneratingViewNode {

  public static final String DEFAULT_NAME = "href";

  private final String name;
  private final String namespace;

  private UrlNode(UrlNode source, String name) {
    this(name, source.namespace);
  }

  public UrlNode(String name, String namespace) {
    this.name = name;
    this.namespace = namespace;
  }

  @Override
  public void setAccessor(Accessor accessor) {
  }

  @Override
  public List<View.Event> evaluate(Object source, ViewContext context)
      throws Exception {
    return Collections.<View.Event>singletonList(new ConcreteViewEvent(
        View.Event.Type.URL, name, namespace, "URL GOES HERE"));
  }

  @Override
  public EventGeneratingViewNode copy(String name) {
    return new UrlNode(this, name);
  }


}
