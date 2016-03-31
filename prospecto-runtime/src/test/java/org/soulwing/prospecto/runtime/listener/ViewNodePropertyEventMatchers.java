/*
 * File created on Mar 31, 2016
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
package org.soulwing.prospecto.runtime.listener;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.sameInstance;

import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;

/**
 * Matchers for {@link ViewNodePropertyEvent} objects.
 *
 * @author Carl Harris
 */
public class ViewNodePropertyEventMatchers {

  @SafeVarargs
  public static Matcher<ViewNodePropertyEvent> eventDescribing(
      Matcher<ViewNodePropertyEvent>... matchers) {
    return allOf(matchers);
  }

  public static Matcher<ViewNodePropertyEvent> sourceNode(ViewNode node) {
    return hasProperty("source", sameInstance(node));
  }

  public static Matcher<ViewNodePropertyEvent> forModel(Object model) {
    return hasProperty("model", sameInstance(model));
  }

  public static Matcher<ViewNodePropertyEvent> propertyValue(Object value) {
    return hasProperty("value", sameInstance(value));
  }

  public static Matcher<ViewNodePropertyEvent> inContext(ViewContext context) {
    return hasProperty("context", sameInstance(context));
  }


}
