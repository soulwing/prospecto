/*
 * File created on Mar 23, 2016
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
package org.soulwing.prospecto.api.listener;

/**
 * A listener that is notified as instances of model types are created or
 * discarded during model update.
 *
 * @author Carl Harris
 */
public interface ViewNodeEntityListener extends ViewListener {

  /**
   * Notifies the recipient that a model entity was created and added to the
   * model.
   * @param event event describing the entity that was created
   */
  void entityCreated(ViewNodePropertyEvent event);

  /**
   * Notifies the recipient that a model entity was removed from the model
   * and is to be discarded.
   * @param event event describing the entity to be discarded
   */
  void entityDiscarded(ViewNodePropertyEvent event);

}
