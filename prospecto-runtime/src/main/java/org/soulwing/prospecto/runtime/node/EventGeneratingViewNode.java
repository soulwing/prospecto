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

import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.runtime.accessor.Accessor;

/**
 * A view node that produces a sequence of {@link View.Event} instances.
 *
 * @author Carl Harris
 */
public interface EventGeneratingViewNode extends ViewNode {

  /**
   * Set the accessor to use to extract (or inject) model data needed by this
   * view node.
   * @param accessor the accessor to set
   */
  void setAccessor(Accessor accessor);

  /**
   * Creates a copy of this node.
   * @param name name for the new node.
   * @return node copy
   */
  EventGeneratingViewNode copy(String name);

  /**
   * Evaluates the node to produce an event sequence.
   * @param source model object associated with the node
   * @param context view context
   * @return sequence of events
   * @throws Exception
   */
  List<View.Event> evaluate(Object source, ViewContext context)
      throws Exception;

}
