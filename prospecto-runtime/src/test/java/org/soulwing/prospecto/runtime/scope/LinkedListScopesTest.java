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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.scope.Scope;

/**
 * Unit tests for {@link LinkedListScopes}.
 *
 * @author Carl Harris
 */
public class LinkedListScopesTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  private LinkedListScopes scopes = new LinkedListScopes();

  @Test
  public void testAppendPrependAndRemove() throws Exception {
    final Scope scope0 = context.mock(Scope.class, "scope0");
    final Scope scope1 = context.mock(Scope.class, "scope1");
    final Scope scope2 = context.mock(Scope.class, "scope2");

    scopes.append(scope1);
    scopes.prepend(scope0);
    scopes.append(scope2);
    assertThat(scopes.toList(), contains(scope0, scope1, scope2));

    assertThat(scopes.remove(scope2), is(true));
    assertThat(scopes.toList(), contains(scope0, scope1));
  }

}
