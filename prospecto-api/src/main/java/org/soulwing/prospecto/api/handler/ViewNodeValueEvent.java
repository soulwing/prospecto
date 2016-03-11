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
package org.soulwing.prospecto.api.handler;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;

/**
 * An event about the model value associated with a value source in a view.
 *
 * @author Carl Harris
 */
public class ViewNodeValueEvent {

  private final ViewNode source;
  private final Object value;
  private final ViewContext context;

  /**
   * Constructs a copy of an event, composed with a different value.
   * @param source the source event
   * @param value the new value to compose in the event
   */
  public ViewNodeValueEvent(ViewNodeValueEvent source, Object value) {
    this(source.getSource(), value, source.getContext());
  }

  /**
   * Constructs a new instance
   * @param source source node for the event
   * @param value model value associated with the node
   * @param context view context
   */
  public ViewNodeValueEvent(ViewNode source, Object value, ViewContext context) {
    this.source = source;
    this.value = value;
    this.context = context;
  }

  /**
   * Gets the event source.
   * @return node source that is the source of the event
   */
  public ViewNode getSource() {
    return source;
  }

  /**
   * Gets the subject value.
   * @return value
   */
  public Object getValue() {
    return value;
  }

  /**
   * Gets the view context.
   * @return view context
   */
  public ViewContext getContext() {
    return context;
  }

}
