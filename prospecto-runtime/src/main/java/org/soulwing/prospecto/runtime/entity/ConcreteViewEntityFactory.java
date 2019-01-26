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
package org.soulwing.prospecto.runtime.entity;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.discriminator.ConcreteDiscriminatorEventService;
import org.soulwing.prospecto.runtime.discriminator.DiscriminatorEventService;

/**
 * An {@link ViewEntityFactory} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewEntityFactory implements ViewEntityFactory {

  public static final ConcreteViewEntityFactory INSTANCE =
      new ConcreteViewEntityFactory();

  private final DiscriminatorEventService discriminatorEventService;

  private ConcreteViewEntityFactory() {
    this(ConcreteDiscriminatorEventService.INSTANCE);
  }

  ConcreteViewEntityFactory(DiscriminatorEventService
      discriminatorEventService) {
    this.discriminatorEventService = discriminatorEventService;
  }

  @Override
  public InjectableViewEntity newEntity(ViewNode node,
      View.Event triggerEvent, Iterable<View.Event> events,
      ScopedViewContext context) throws Exception {

    View.Event event = discriminatorEventService.findDiscriminatorEvent(
        triggerEvent, events.iterator());

    if (event == null) {
      return new ConcreteInjectableViewEntity(node.getModelType());
    }

    final Discriminator discriminator = new Discriminator(event.getValue());

    final DiscriminatorStrategy strategy =
        discriminatorEventService.findStrategy(node, context);

    return new ConcreteInjectableViewEntity(
        strategy.toSubtype(node.getModelType(), discriminator));
  }

}
