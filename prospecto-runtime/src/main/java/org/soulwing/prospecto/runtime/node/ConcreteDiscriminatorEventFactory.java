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
package org.soulwing.prospecto.runtime.node;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.discriminator.Discriminator;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;

/**
 * A {@link DiscriminatorEventFactory} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteDiscriminatorEventFactory
    implements DiscriminatorEventFactory {

  private final DiscriminatorStrategyLocator strategyLocator;

  ConcreteDiscriminatorEventFactory() {
    this(new ConcreteDiscriminatorStrategyLocator());
  }

  ConcreteDiscriminatorEventFactory(
      DiscriminatorStrategyLocator strategyLocator) {
    this.strategyLocator = strategyLocator;
  }

  @Override
  public DiscriminatorStrategyLocator getStrategyLocator() {
    return strategyLocator;
  }

  @Override
  public boolean isDiscriminatorNeeded(ContainerViewNode node) {
    final Boolean flag = node.get(DISCRIMINATOR_FLAG_KEY, Boolean.class);
    return flag != null && flag && !(node instanceof SubtypeNode);
  }

  @Override
  public View.Event newDiscriminatorEvent(ContainerViewNode node,
      Class<?> subtype, ScopedViewContext context) {

    final DiscriminatorStrategy strategy =
        strategyLocator.findStrategy(node, context);

    final Discriminator discriminator =
        strategy.toDiscriminator(node.getModelType(), subtype);

    return new ConcreteViewEvent(View.Event.Type.DISCRIMINATOR,
        discriminator.getName(), null, discriminator.getValue());
  }

}
