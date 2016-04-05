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
package org.soulwing.prospecto.api.listener;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.node.ViewNode;

/**
 * An event about a view node.
 *
 * @author Carl Harris
 */
public class ViewNodeEvent implements ViewListener {

  public enum Mode {
    VIEW_GENERATION,
    MODEL_UPDATE
  }

  private final Mode mode;
  private final ViewNode source;
  private final Object model;
  private final ViewContext context;

  /**
   * Constructs a new instance.
   * @param mode mode for which the source node is being considered
   * @param source view node source of the event
   * @param model model associated with {@code source}
   * @param context view context
   */
  public ViewNodeEvent(Mode mode, ViewNode source, Object model,
      ViewContext context) {
    this.mode = mode;
    this.source = source;
    this.model = model;
    this.context = context;
  }

  /**
   * Gets the mode for which the source node is being considered.
   * @return property value
   */
  public Mode getMode() {
    return mode;
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
   * Gets the view context.
   * @return view context
   */
  public ViewContext getContext() {
    return context;
  }


}
