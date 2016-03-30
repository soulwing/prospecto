/*
 * File created on Mar 29, 2016
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
package org.soulwing.prospecto.demo.jaxrs.service;

import org.soulwing.prospecto.api.View;

/**
 * A service that provides access to
 * {@link org.soulwing.prospecto.demo.jaxrs.domain.Division} entities.
 *
 * @author Carl Harris
 */
public interface DivisionService {

  /**
   * Finds a division using its persistent identifier.
   * @param id persistent identifier of the subject division
   * @return league view
   * @throws NoSuchEntityException if there exists no division with the given
   *    identifier
   */
  View findDivisionById(Long id) throws NoSuchEntityException;

  /**
   * Updates a league.
   * @param id persistent ID of the league to update
   * @param divisionView detail view representation of the division
   * @return updated division view
   * @throws NoSuchEntityException
   * @throws UpdateConflictException
   */
  View updateDivision(Long id, View divisionView)
      throws NoSuchEntityException, UpdateConflictException;

}
