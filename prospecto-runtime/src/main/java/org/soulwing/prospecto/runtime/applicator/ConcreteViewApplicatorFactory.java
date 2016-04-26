/*
 * File created on Apr 8, 2016
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

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.listener.ViewTraversalEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * A {@link ViewApplicatorFactory} that produces {@link ConcreteViewApplicator}
 * objects.
 *
 * @author Carl Harris
 */
public class ConcreteViewApplicatorFactory implements ViewApplicatorFactory {

  public static final ConcreteViewApplicatorFactory INSTANCE =
      new ConcreteViewApplicatorFactory();

  private ConcreteViewApplicatorFactory() {}

  @Override
  public ViewApplicator newApplicator(Class<?> modelType,
      ViewEventApplicator applicator, View source, ScopedViewContext context,
      String dataKey, ViewTraversalEvent event) {
    if (!(applicator instanceof RootViewEventApplicator)) {
      throw new IllegalArgumentException("view template is not updatable");
    }
    return new ConcreteViewApplicator(modelType,
        (RootViewEventApplicator) applicator, source, context, dataKey, event);
  }

}
