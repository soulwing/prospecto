/*
 * File created on Mar 23, 2016
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
 * An event that describes a property of a view node.
 *
 * @author Carl Harris
 */
public class ViewNodePropertyEvent implements ViewEvent {

  private final ViewNode source;
  private final Object model;
  private final Object value;
  private final ViewContext context;

  /**
   * Constructs a copy of an event, composed with a different provoperty model.
   * @param source the source event
   * @param value the new property value to compose in the event
   */
  public ViewNodePropertyEvent(ViewNodePropertyEvent source, Object value) {
    this(source.getSource(), source.getModel(), value, source.getContext());
  }

  /**
   * Constructs a new instance.
   * @param source view node source of the event
   * @param model model associated with source view node
   * @param value property value associated with {@code model}
   * @param context view context
   */
  public ViewNodePropertyEvent(ViewNode source, Object model, Object value,
      ViewContext context) {
    this.source = source;
    this.model = model;
    this.value = value;
    this.context = context;
  }

  /**
   * Gets the event source.
   * @return node that is the source of the event
   */
  public ViewNode getSource() {
    return source;
  }

  /**
   * Gets the model associated with the event source node.
   * @return model
   */
  public Object getModel() {
    return model;
  }

  /**
   * Gets the {@code value} property.
   * @return property value
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
