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
 * discarded during view application.
 * <p>
 * When a to-one or to-many association between model types is encountered
 * as a view is applied to update a model, instances of the associate's
 * model type may be created or discarded.
 * <p>
 * An implementation of this interface can be used to inform a persistence
 * framework that entity instances have been added or removed. When using
 * JPA, it is generally easier to simply use the option to cascade operations
 * between associated entity types, but in some cases that is either undesirable
 * or unworkable. This listener type provides an alternative.
 * <p>
 * In a to-one association, the associate object represented in the view may be
 * a different object than model's current associate. In this case, a new
 * instance of the associate type will be created and injected with property
 * values from the input view, and then injected into the corresponding property
 * of the owning model object as the replacement associate. Every instance
 * of {@link ViewNodeEntityListener} registered with the view context, will
 * be notified, in order, that a new associate entity was created (by
 * invocation of the {@link #entityCreated(ViewNodePropertyEvent)} method).
 * If the previous associate of the owner was not null, every registered
 * entity listener will be notified that previous associate entity was
 * discarded from the model (by invocation of the
 * {@link #entityDiscarded(ViewNodePropertyEvent)} method).
 * <p>
 * In a to-many association, the owning model type has a collection whose
 * component type is the associate model type. The input view has an array
 * of object representations, each of which may represent either (1) an
 * existing associate in the collection or (2) a new associate not in the
 * collection. For each new associate, the registered entity listeners are
 * notified via {@link #entityCreated(ViewNodePropertyEvent)} that a new
 * entity was created. Additionally, any associates in the model's collection
 * that did not appear in the view may be removed from the collection. For
 * each associate that is removed, the registered entity listeners are notified
 * via {@link #entityDiscarded(ViewNodePropertyEvent)}.
 * <p>
 * The event object passed to the listener methods identifies the source
 * view template node for the event. The
 * {@link ViewNodePropertyEvent#getModel() model} property of the event
 * is the owning model object, and the
 * {@link ViewNodePropertyEvent#getValue() value} property is the associate
 * that was created or discarded.
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
