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

import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.node.ContainerViewNode;

/**
 * A {@link DiscriminatorStrategyLocator} implementation.
 *
 * @author Carl Harris
 */
class ConcreteDiscriminatorStrategyLocator
    implements DiscriminatorStrategyLocator {

  @Override
  public DiscriminatorStrategy findStrategy(ContainerViewNode node,
      ScopedViewContext context) {

    DiscriminatorStrategy strategy = node.get(DiscriminatorStrategy.class);
    if (strategy == null) {
      strategy = context.get(DiscriminatorStrategy.class);
    }
    if (strategy == null) {
      throw new AssertionError("discriminator strategy is required");
    }

    return strategy;
  }

}
