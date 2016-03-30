/*
 * File created on Mar 30, 2016
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
package org.soulwing.prospecto.runtime.collection;

import java.util.List;

import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.api.collection.CollectionManager;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * A static utility method for updating a collection during model update.
 *
 *
 * @author Carl Harris
 */
public class CollectionUpdater {

  private static final CollectionUpdateStrategy[] strategies = {
      OrderedCollectionUpdateStrategy.INSTANCE,
      UnorderedCollectionUpdateStrategy.INSTANCE
  };

  public static void update(ViewNode node, Object target,
      List<MutableViewEntity> entities, CollectionManager manager,
      ScopedViewContext context) throws Exception {
    findStrategy(manager).update(node, target, entities, manager, context);
  }

  private static CollectionUpdateStrategy findStrategy(
      CollectionManager manager) {
    for (final CollectionUpdateStrategy strategy : strategies) {
      if (strategy.supports(manager)) {
        return strategy;
      }
    }
    throw new AssertionError("no strategy");
  }

}
