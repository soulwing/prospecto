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

import org.soulwing.prospecto.ViewOptionsRegistry;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.api.template.ContainerNode;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.template.ConcreteSubtypeNode;

/**
 * A {@link DiscriminatorEventService} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteDiscriminatorEventService
    implements DiscriminatorEventService {

  public static final ConcreteDiscriminatorEventService INSTANCE =
      new ConcreteDiscriminatorEventService();

  private final DiscriminatorStrategyLocator strategyLocator;

  private ConcreteDiscriminatorEventService() {
    this(ConcreteDiscriminatorStrategyLocator.INSTANCE);
  }

  ConcreteDiscriminatorEventService(
      DiscriminatorStrategyLocator strategyLocator) {
    this.strategyLocator = strategyLocator;
  }

  @Override
  public boolean isDiscriminatorNeeded(ContainerNode node) {
    final Boolean flag = node.get(DISCRIMINATOR_FLAG_KEY, Boolean.class);
    return flag != null && flag && !(node instanceof ConcreteSubtypeNode);
  }

  @Override
  public View.Event newDiscriminatorEvent(ViewNode node,
      Class<?> subtype, ScopedViewContext context) {

    final DiscriminatorStrategy strategy =
        strategyLocator.findStrategy(node, context);

    final Discriminator discriminator =
        strategy.toDiscriminator(node.getModelType(), subtype);

    return new ConcreteViewEvent(View.Event.Type.DISCRIMINATOR,
        ViewOptionsRegistry.getOptions().get(ViewKeys.DISCRIMINATOR_NAME,
            ViewDefaults.DISCRIMINATOR_NODE_NAME).toString(),
            null, discriminator.getValue());
  }

  @Override
  public View.Event findDiscriminatorEvent(View.Event triggerEvent,
      Iterator<View.Event> events) {
    View.Event event = null;
    while (events.hasNext()) {
      event = events.next();
      if (event.getType() == triggerEvent.getType().complement()) break;
      if (View.Event.Type.DISCRIMINATOR == event.getType()) break;
      skipEvent(event, events);
    }
    if (event == null) return null;
    if (event.getType() != View.Event.Type.DISCRIMINATOR) return null;
    return event;
  }

  static void skipEvent(View.Event event,
      Iterator<View.Event> events) {
    final View.Event.Type complementType = event.getType().complement();
    while (events.hasNext() && event.getType() != complementType) {
      event = events.next();
      if (event.getType() != complementType
          && event.getType() != event.getType().complement()) {
        skipEvent(event, events);
      }
    }
  }

  @Override
  public DiscriminatorStrategy findStrategy(ViewNode node,
      ScopedViewContext context) {
    return strategyLocator.findStrategy(node, context);
  }

}
