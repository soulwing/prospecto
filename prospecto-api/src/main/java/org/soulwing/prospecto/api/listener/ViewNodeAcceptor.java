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
 * A listener that is notified before nodes are visited during a view processing
 * lifecycle.
 * <p>
 * When generating a view or applying a view to update a model, the
 * {@link ViewNodeAcceptor} instances registered with the view context are
 * consulted for each node in the view template's node tree.  In the order
 * in which they are registered, each acceptor is given an opportunity to allow
 * or deny the visitation of a given node. If all acceptors return {@code true}
 * from the {@link #shouldVisitNode(ViewNodeEvent)} method, the node is visited.
 * The first registered acceptor to return {@code false} effectively vetoes the
 * visitation of the node, and no other acceptors are consulted.
 * <p>
 * A listener of this type can be used to filter subtrees from a generated
 * view, or to ignore subtrees of a view provided as input when a view is
 * applied to update a model. If an acceptor vetoes the visitation of a given
 * node, none of the descendants of that node will be visited during view
 * processing.
 * <p>
 * The event object passed to the listener methods identifies the subject
 * view template node for the event. During view generation, the
 * {@link ViewNodeEvent#getModel() model} property of the event is the model
 * object that corresponds to the parent of the subject node.
 * During view application, the model property of the event is a
 * {@link org.soulwing.prospecto.api.ViewEntity} that represents an instance
 * of the object type that corresponds to the parent of the subject node.
 *
 * @author Carl Harris
 */
public interface ViewNodeAcceptor extends ViewListener {

  /**
   * Notifies the recipient that a view node will be visited.
   *
   * @param event event describing the node
   * @return {@code true} if this filter wishes to allow the node
   *    to be visited
   */
  boolean shouldVisitNode(ViewNodeEvent event);

}
