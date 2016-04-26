/*
 * File created on Apr 26, 2016
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
import org.soulwing.prospecto.api.ViewTemplate;

/**
 * An event that describes a view traversal.
 *
 * @author Carl Harris
 */
public class ViewTraversalEvent {

  private final ViewMode mode;
  private final ViewTemplate source;
  private final ViewContext context;

  /**
   * Constructs a new instance.
   * @param mode view processing mode
   * @param source source view template
   * @param context view context
   */
  public ViewTraversalEvent(ViewMode mode, ViewTemplate source,
      ViewContext context) {
    this.mode = mode;
    this.source = source;
    this.context = context;
  }

  /**
   * Gets the view processing mode.
   * @return view processing mode
   */
  public ViewMode getMode() {
    return mode;
  }

  /**
   * Gets the source template for the event.
   * @return source template
   */
  public ViewTemplate getSource() {
    return source;
  }

  /**
   * Gets the view context
   * @return view context
   */
  public ViewContext getContext() {
    return context;
  }

}
