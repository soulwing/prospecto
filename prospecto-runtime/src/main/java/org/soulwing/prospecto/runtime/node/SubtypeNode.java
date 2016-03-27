/*
 * File created on Mar 16, 2016
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

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A node that acts as a container for properties of a subtype of its parent
 * node.
 * <p>
 * Unlike other container nodes, this node does not introduce any additional
 * object/array structure into a view. When evaluated with a model type that
 * is an instance of the type specified in its constructor, its children become
 * peers of the other children of its parent node.
 *
 * @author Carl Harris
 */
public class SubtypeNode extends ContainerViewNode {

  /**
   * Constructs a new instance
   * @param subtype type which must be a subtype of the parent node's type
   */
  public SubtypeNode(Class<?> subtype) {
    super(null, null, subtype);
  }

  @Override
  protected List<View.Event> onEvaluate(Object source,
      ScopedViewContext context) throws Exception {
    final List<View.Event> viewEvents = new LinkedList<>();
    if (getModelType().isInstance(source)) {
      viewEvents.addAll(evaluateChildren(source, context));
    }
    return viewEvents;
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
    throw new UnsupportedOperationException();
  }

}
