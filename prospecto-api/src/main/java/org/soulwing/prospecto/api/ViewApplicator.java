/*
 * File created on Mar 17, 2016
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
package org.soulwing.prospecto.api;

import java.util.Collection;
import java.util.List;

import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.association.ToManyIndexedAssociationManager;

/**
 * An applicator for a view.
 * <p>
 * An applicator applies the properties in the source view to a model.
 *
 * @author Carl Harris
 */
public interface ViewApplicator {

  /**
   * Creates a new instance of the model type represented by the source view,
   * and injects it with the properties represented in the view.
   * @return model instance
   * @throws ViewApplicatorException
   */
  Object create() throws ViewApplicatorException;

  /**
   * Updates the given model by injecting it with the properties represented
   * in the source view.
   * @param model model (which must be an instance of the type associated with
   *    the root of the target view)
   * @throws ViewApplicatorException
   */
  void update(Object model) throws ViewApplicatorException;

  /**
   * Updates the given collection of model objects with the state represented in
   * a source view whose root is an array of objects.
   * @param model collection to update
   * @param associationManager association manager that will be used to update
   *   the collection; the {@link ToManyAssociationManager#supports(AssociationDescriptor) supports}
   *   method of the manager will not be invoked
   * @throws ViewApplicatorException
   */
  void update(Collection<?> model,
      ToManyAssociationManager<?, ?> associationManager)
      throws ViewApplicatorException;

  /**
   * Updates the given array of model objects with the state represented in
   * a source view whose root is an array of objects.
   * @param model array to update
   * @param associationManager association manager that will be used to update
   *   the array; the {@link ToManyAssociationManager#supports(AssociationDescriptor) supports}
   *   method of the manager will not be invoked
   * @return updated array; will not be the same array as {@code model}, if it
   *   was necessary to resize the array (e.g. to add or remove objects from it)
   * @throws ViewApplicatorException
   */
  void update(Object[] model,
      ToManyIndexedAssociationManager<?, ?> associationManager)
      throws ViewApplicatorException;

  /**
   * Updates the given list of model objects with the state represented in
   * a source view whose root is an array of objects.
   * @param model list to update
   * @param associationManager association manager that will be used to update
   *   the list; the {@link ToManyAssociationManager#supports(AssociationDescriptor) supports}
   *   method of the manager will not be invoked
   * @throws ViewApplicatorException
   */
  void update(List<?> model,
      ToManyIndexedAssociationManager<?, ?> associationManager)
      throws ViewApplicatorException;

  /**
   * Resolves the reference represented by a source view whose root is
   * of reference type.
   * @return resolved reference
   * @throws ViewApplicatorException
   */
  Object resolve() throws ViewApplicatorException;

  /**
   * Resolves all of the references represented by a source view whose root is
   * an array of references.
   * @return list of resolved references
   * @throws ViewApplicatorException
   */
  List<?> resolveAll() throws ViewApplicatorException;

  /**
   * Produces an view entity representation for this applicator.
   * @return view entity
   * @throws ViewApplicatorException
   */
  ViewEntity toViewEntity() throws ViewApplicatorException;

}
