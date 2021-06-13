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

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link ConcreteToManyAssociationUpdater}.
 *
 * @author Carl Harris
 */
public class ConcreteToManyAssociationUpdaterTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  NotifiableViewListeners listeners;

  @Mock
  AssociationDescriptorFactory descriptorFactory;

  @Mock
  AssociationManagerLocator managerLocator;

  @Mock
  AssociationDescriptor descriptor;

  @Mock
  ToManyAssociationUpdateStrategy strategy;

  @Mock
  ToManyAssociationManager defaultManager;

  @Mock
  ToManyAssociationManager manager;

  @Mock
  MockModel owner;

  MockContainerNode node = new MockContainerNode();

  ConcreteToManyAssociationUpdater updater;

  @Before
  public void setUp() throws Exception {
    updater = new ConcreteToManyAssociationUpdater(
        new ToManyAssociationUpdateStrategy[] { strategy },
        descriptorFactory, managerLocator);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUpdate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(manager).begin(owner);
        oneOf(descriptorFactory).newDescriptor(node);
        will(returnValue(descriptor));
        oneOf(managerLocator).findManager(ToManyAssociationManager.class,
            defaultManager, descriptor, node, viewContext);
        will(returnValue(manager));
        oneOf(strategy).supports(manager);
        will(returnValue(true));
        oneOf(strategy).update(node, owner,
            Collections.<InjectableViewEntity>emptyList(),
            manager, viewContext);
        oneOf(manager).end(owner);
      }
    });

    updater.findManagerAndUpdate(node, owner, Collections.<InjectableViewEntity>emptyList(),
        defaultManager, viewContext);
  }

  interface MockModel {}

}
