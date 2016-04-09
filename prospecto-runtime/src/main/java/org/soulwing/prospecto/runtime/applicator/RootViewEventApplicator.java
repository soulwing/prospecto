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

import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * An applicator that can be applied to a target model.
 *
 * @author Carl Harris
 */
public interface RootViewEventApplicator extends ViewEventApplicator {

  /**
   * Applies an injector to update the target model.
   * @param injector the subject injector
   * @param target root of the target model
   * @param context view context
   * @throws Exception
   */
  void apply(Object injector, Object target, ScopedViewContext context)
      throws Exception;

}
