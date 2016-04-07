/*
 * File created on Apr 6, 2016
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

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.node.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * An object that applies the state in a sequence of view events to a model.
 *
 * @author Carl Harris
 */
public interface ViewEventApplicator extends MutableViewEntity.Injector {

  ViewNode getNode();

  Object toModelValue(ViewEntity parentEntity, View.Event triggerEvent,
      Deque<View.Event> events, ScopedViewContext context) throws Exception;

}
