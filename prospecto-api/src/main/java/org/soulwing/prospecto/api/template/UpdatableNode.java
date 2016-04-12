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
package org.soulwing.prospecto.api.template;

import java.util.Set;

import org.soulwing.prospecto.api.AccessMode;

/**
 * A {@link ViewNode} that can be used to update a corresponding model value.
 *
 * @author Carl Harris
 */
public interface UpdatableNode extends ViewNode {

  /**
   * Gets the set of access modes that are allowed by the configuration of
   * this node.
   * @return allowed access modes
   */
  Set<AccessMode> getAllowedModes();

  /**
   * Gets the set of access modes that are supported by the model property
   * associated with this node.
   * @return supported access modes
   */
  Set<AccessMode> getSupportedModes();

  /**
   * Gets the name of the updatable property.
   * @return property name
   */
  String getPropertyName();

}
