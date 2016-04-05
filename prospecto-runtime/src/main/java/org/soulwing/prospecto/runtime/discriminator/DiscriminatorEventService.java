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
package org.soulwing.prospecto.runtime.discriminator;

import java.util.Iterator;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.node.ConcreteContainerNode;

/**
 * A service that supports creating and finding discriminator events.
 *
 * @author Carl Harris
 */
public interface DiscriminatorEventService {

  String DISCRIMINATOR_FLAG_KEY = "hasDiscriminator";

  /**
   * Tests whether a node requires a discriminator.
   * @param node the subject node
   * @return {@code true} if {@code node} requires a discriminator
   */
  boolean isDiscriminatorNeeded(ConcreteContainerNode node);

  /**
   * Creates a new discriminator event for the given node and subtype.
   * @param node the subject node
   * @param subtype subtype represented by the node
   * @param context view context
   * @return discriminator event
   */
  View.Event newDiscriminatorEvent(ConcreteContainerNode node, Class<?> subtype,
      ScopedViewContext context);

  /**
   * Finds the first discriminator event in the given event stream, skipping
   * any structures encountered while searching.
   * @param events stream of events to search
   * @return discriminator or {@code null} if not found
   */
  View.Event findDiscriminatorEvent(Iterator<View.Event> events);

  /**
   * Finds the appropriate discriminator strategy for the subject container node.
   * @param node subject node
   * @param context view context
   * @return discriminator strategy (never {@code null})
   */
  DiscriminatorStrategy findStrategy(ConcreteContainerNode node,
      ScopedViewContext context);

}
