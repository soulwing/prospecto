/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.runtime.accessor;

import java.util.EnumSet;

import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;
import org.soulwing.prospecto.api.association.ToOneAssociationManager;

/**
 * An accessor for a property of a model.
 *
 * @author Carl Harris
 */
public interface Accessor extends ToOneAssociationManager<Object, Object> {

  /**
   * Gets the model type in which the property is located
   * @return model type
   */
  Class<?> getModelType();

  /**
   * Gets the name of the property.
   * @return property name
   */
  String getName();

  /**
   * Gets the access type.
   * @return access type
   */
  AccessType getAccessType();

  /**
   * Gets the supported access modes of this accessor.
   * @return set of supported access modes
   */
  EnumSet<AccessMode> getSupportedModes();

  /**
   * Tests whether this accessor can be called to read (get) a value.
   * <p>
   * This is basically shorthand for {@code getAccessModes().contains(AccessMode.READ)}
   * @return {@code true} if this accessor supports {@link AccessMode#READ}.
   */
  boolean canRead();

  /**
   * Tests whether this accessor can be called to write (set) a value.
   * <p>
   * This is basically shorthand for {@code getAccessModes().contains(AccessMode.WRITE)}
   * @return {@code true} if this accessor supports {@link AccessMode#WRITE}.
   */
  boolean canWrite();

  /**
   * Gets the declared data type of the property.
   * @return data type
   */
  Class<?> getDataType();

  /**
   * Creates a copy of this accessor for a particular subtype of the base
   * type.
   * @param subtype the subject subtype
   * @return accessor with same configuration, but with {!code subtype} as
   *    the new base type
   * @throws Exception
   */
  Accessor forSubtype(Class<?> subtype) throws Exception;

  /**
   * Gets the property value.
   * @param source model object from which the property value is to be extracted
   * @return property value
   */
  Object get(Object source) throws Exception;

  /**
   * Sets the property value.
   * @param target model object into which the property value is to be injected
   * @param value property value to inject
   */
  void set(Object target, Object value) throws Exception;


}
