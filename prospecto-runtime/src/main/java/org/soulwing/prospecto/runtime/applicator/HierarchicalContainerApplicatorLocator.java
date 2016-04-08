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

import java.util.Iterator;

/**
 * A hierarchical {@link ContainerApplicatorLocator}.
 *
 * This implementation uses the concrete type of target model object to
 * determine the applicator which is <em>best</em> -- i.e. the applicator
 * whose model type is the target type itself or its closest ancestor.
 *
 * @author Carl Harris
 */
class HierarchicalContainerApplicatorLocator
    implements ContainerApplicatorLocator {

  public static final HierarchicalContainerApplicatorLocator INSTANCE =
      new HierarchicalContainerApplicatorLocator();

  private HierarchicalContainerApplicatorLocator() {}

  @Override
  public ViewEventApplicator findApplicator(String name, Class<?> targetType,
      ContainerApplicator container) {
    Iterator<ViewEventApplicator> i = container.getChildren().iterator();
    while (i.hasNext()) {
      final ViewEventApplicator child = i.next();
      if (child instanceof SubtypeApplicator
          && child.getNode().getModelType().isAssignableFrom(targetType)) {
        final ViewEventApplicator descendant = findApplicator(name, targetType,
            (SubtypeApplicator) child);
        if (descendant != null) return descendant;
      }
    }

    i = container.getChildren().iterator();
    while (i.hasNext()) {
      final ViewEventApplicator child = i.next();
      if (name.equals(child.getNode().getName())) {
        return child;
      }
    }

    return null;
  }

}
