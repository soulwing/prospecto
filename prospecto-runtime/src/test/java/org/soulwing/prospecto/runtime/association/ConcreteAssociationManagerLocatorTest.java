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
package org.soulwing.prospecto.runtime.association;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.AssociationManager;
import org.soulwing.prospecto.api.node.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@link ConcreteAssociationManagerLocator}.
 *
 * @author Carl Harris
 */
public class ConcreteAssociationManagerLocatorTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  AssociationManagerService associationManagers;

  @Mock
  UpdatableNode node;

  @Mock
  AssociationDescriptor descriptor;

  @Mock
  AssociationManager defaultManager;

  @Mock
  AssociationManager manager;

  @Test
  public void testNodeSpecificManager() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(AssociationManager.class);
        will(returnValue(manager));
        oneOf(manager).supports(descriptor);
        will(returnValue(true));
      }
    });

    final AssociationManager result =
        ConcreteAssociationManagerLocator.INSTANCE.findManager(
            AssociationManager.class, defaultManager, descriptor, node,
            viewContext);

    assertThat(result, sameInstance(manager));
  }

  @Test(expected = ModelEditorException.class)
  public void testNodeSpecificManagerDoesNotSupportAssociation() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(AssociationManager.class);
        will(returnValue(manager));
        oneOf(manager).supports(descriptor);
        will(returnValue(false));
      }
    });

    ConcreteAssociationManagerLocator.INSTANCE.findManager(
        AssociationManager.class, defaultManager, descriptor, node,
        viewContext);
  }

  @Test
  public void testContextManager() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(AssociationManager.class);
        will(returnValue(null));
        oneOf(viewContext).getAssociationManagers();
        will(returnValue(associationManagers));
        oneOf(associationManagers).findManager(AssociationManager.class,
            descriptor);
        will(returnValue(manager));
      }
    });

    final AssociationManager result =
        ConcreteAssociationManagerLocator.INSTANCE.findManager(
            AssociationManager.class, defaultManager, descriptor, node,
            viewContext);

    assertThat(result, sameInstance(manager));
  }

  @Test
  public void testDefaultManager() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(node).get(AssociationManager.class);
        will(returnValue(null));
        oneOf(viewContext).getAssociationManagers();
        will(returnValue(associationManagers));
        oneOf(associationManagers).findManager(AssociationManager.class,
            descriptor);
        will(returnValue(null));
      }
    });

    final AssociationManager result =
        ConcreteAssociationManagerLocator.INSTANCE.findManager(
            AssociationManager.class, defaultManager, descriptor, node,
            viewContext);

    assertThat(result, sameInstance(defaultManager));
  }


}
