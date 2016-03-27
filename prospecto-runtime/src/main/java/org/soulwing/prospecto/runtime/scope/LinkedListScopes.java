/*
 * File created on Mar 27, 2016
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
package org.soulwing.prospecto.runtime.scope;

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.scope.Scope;
import org.soulwing.prospecto.api.scope.Scopes;

/**
 * A {@link Scopes} implementation backed by a linked list.
 *
 * @author Carl Harris
 */
public class LinkedListScopes implements Scopes {

  private final List<Scope> scopes = new LinkedList<>();

  @Override
  public void append(Scope scope) {
    scopes.add(scope);
  }

  @Override
  public void prepend(Scope scope) {
    scopes.add(0, scope);
  }

  @Override
  public boolean remove(Scope scope) {
    return scopes.remove(scope);
  }

  @Override
  public List<Scope> toList() {
    return scopes;
  }

}
