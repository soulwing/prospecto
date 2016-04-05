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
package org.soulwing.prospecto.runtime.listener;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.sameInstance;

import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.api.node.ViewNode;

/**
 * Matchers for view node event types.
 *
 * @author Carl Harris
 */
public class ViewNodeEventMatchers {


  @SafeVarargs
  public static <E extends ViewNodeEvent> Matcher<E> eventDescribing(
      Matcher<E>... matchers) {
    return allOf(matchers);
  }

  public static Matcher<ViewNodeEvent> sourceNode(ViewNode node) {
    return hasProperty("source", sameInstance(node));
  }

  public static Matcher<ViewNodeEvent> forModel(Object model) {
    return hasProperty("model", sameInstance(model));
  }

  public static Matcher<ViewNodeEvent> propertyValue(Object value) {
    return hasProperty("value", equalTo(value));
  }

  public static Matcher<ViewNodeEvent> inContext(ViewContext context) {
    return hasProperty("context", sameInstance(context));
  }

  public static Matcher<ViewNodeEvent> mode(ViewNodeEvent.Mode mode) {
    return hasProperty("mode", equalTo(mode));
  }

}


