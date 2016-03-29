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
package org.soulwing.prospecto.runtime.collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.collection.CollectionManager;

/**
 * Unit tests for {@link LinkedListCollectionManagerService}.
 *
 * @author Carl Harris
 */
public class LinkedListCollectionManagerServiceTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  private LinkedListCollectionManagerService managers =
      new LinkedListCollectionManagerService();

  @Test
  public void testAppendPrependAndRemove() throws Exception {
    final CollectionManager manager0 = context.mock(CollectionManager.class, "manager0");
    final CollectionManager manager1 = context.mock(CollectionManager.class, "manager1");
    final CollectionManager manager2 = context.mock(CollectionManager.class, "manager2");

    managers.append(manager1);
    managers.prepend(manager0);
    managers.append(manager2);
    assertThat(managers.toList(), contains(manager0, manager1, manager2));

    assertThat(managers.remove(manager2), is(true));
    assertThat(managers.toList(), contains(manager0, manager1));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindManager() throws Exception {
    final CollectionManager manager0 = context.mock(CollectionManager.class, "manager0");
    final CollectionManager manager1 = context.mock(CollectionManager.class, "manager1");
    final CollectionManager manager2 = context.mock(CollectionManager.class, "manager2");

    context.checking(new Expectations() {
      {
        oneOf(manager0).supports(MockOwner.class, MockElement.class);
        will(returnValue(false));
        oneOf(manager1).supports(MockOwner.class, MockElement.class);
        will(returnValue(true));
      }
    });

    managers.append(manager0);
    managers.append(manager1);
    managers.append(manager2);

    assertThat(managers.findManager(MockOwner.class, MockElement.class),
        is(sameInstance(manager1)));
  }

  interface MockOwner {
  }

  interface MockElement {
  }

}
