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
package org.soulwing.prospecto.api.association;

/**
 * A descriptor for an association between model objects.
 *
 * @author Carl Harris
 */
public interface AssociationDescriptor {

  /**
   * Gets the class for the model type that owns the association.
   * @return owner type
   */
  Class<?> getOwnerType();

  /**
   * Gets the class for the model type for the associate.
   * @return associate type
   */
  Class<?> getAssociateType();

  /**
   * Gets the property name of the association in the owner type.
   * @return association property name
   */
  String getPropertyName();

}
