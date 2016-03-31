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

import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A service the locates a discriminator strategy.
 *
 * @author Carl Harris
 */
public interface DiscriminatorStrategyLocator {

  /**
   * Finds the appropriate discriminator strategy for the subject container node.
   * @param node subject node
   * @param context view context
   * @return discriminator strategy (never {@code null})
   */
  DiscriminatorStrategy findStrategy(ContainerViewNode node,
      ScopedViewContext context);

}
