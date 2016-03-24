/*
 * File created on Mar 15, 2016
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
package org.soulwing.prospecto.runtime.handler;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.sameInstance;

import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.handler.ViewNodeEvent;
import org.soulwing.prospecto.api.handler.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Matchers for view node event types.
 *
 * @author Carl Harris
 */
public class ViewNodeEventMatchers {

  public static Matcher<ViewNodePropertyEvent> viewNodePropertyEvent(
      ViewNode node, Object model, Object value, ViewContext viewContext) {
    return allOf(
        hasProperty("source", sameInstance(node)),
        hasProperty("model", sameInstance(model)),
        hasProperty("value", sameInstance(value)),
        hasProperty("context", sameInstance(viewContext)));
  }

  public static Matcher<ViewNodeEvent> viewNodeEvent(ViewNode node,
      Object model, ScopedViewContext viewContext) {
    return allOf(
        hasProperty("source", sameInstance(node)),
        hasProperty("model", sameInstance(model)),
        hasProperty("context", sameInstance(viewContext)));
  }


}


