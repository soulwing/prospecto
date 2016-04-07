/*
 * File created on Apr 7, 2016
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
package org.soulwing.prospecto.runtime.applicator;

import java.util.Deque;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A {@link ViewEventApplicator} that applies nothing.
 *
 * @param <N> view node type
 * @author Carl Harris
 */
class NoOpApplicator<N extends ViewNode> implements ViewEventApplicator {

  private final N node;

  NoOpApplicator(N node) {
    this.node = node;
  }

  @Override
  public N getNode() {
    return null;
  }

  @Override
  public Object toModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception {
    return UndefinedValue.INSTANCE;
  }

  @Override
  public void inject(Object target, Object value) throws Exception {
  }

  @Override
  public void inject(Object target, Object value, ScopedViewContext context)
      throws Exception {
  }

}
