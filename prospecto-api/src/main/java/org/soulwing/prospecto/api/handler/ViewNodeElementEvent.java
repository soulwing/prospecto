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
 * An event about an element of an array view node.
 *
 * @author Carl Harris
 */
public class ViewNodeElementEvent {

  private final ViewNode source;
  private final Object model;
  private final Object elementModel;
  private final ViewContext context;

  /**
   * Constructs a copy of an event, composed with a different element model.
   * @param source the source event
   * @param elementModel the new element model to compose in the event
   */
  public ViewNodeElementEvent(ViewNodeElementEvent source,
      Object elementModel) {
    this(source.getSource(), source.getModel(), elementModel,
        source.getContext());
  }

  /**
   * Constructs a new instance.
   * @param source view node source of the event
   * @param model model associated with source view node
   * @param elementModel model of the element to that will be used when visiting
   *    {@code elementNode}
   * @param context view context
   */
  public ViewNodeElementEvent(ViewNode source,
      Object model, Object elementModel, ViewContext context) {
    this.source = source;
    this.model = model;
    this.elementModel = elementModel;
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
   * Gets the model of the element to that will be visited.
   * @return element model
   */
  public Object getElementModel() {
    return elementModel;
  }

  /**
   * Gets the view context.
   * @return view context
   */
  public ViewContext getContext() {
    return context;
  }

}
