/*
 * File created on Apr 5, 2016
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
import org.soulwing.prospecto.runtime.template.ConcreteContainerNode;
import org.soulwing.prospecto.runtime.testing.JUnitRuleClassImposterizingMockery;

/**
 * Unit tests for {@link ValueCollectionToManyAssociationUpdater}.
 *
 * @author Carl Harris
 */
public class ValueCollectionToManyAssociationUpdaterTest {

  private static final Object OWNER = new Object();
  private static final Object VALUE = new Object();

  @Rule
  public final JUnitRuleMockery context =
      new JUnitRuleClassImposterizingMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  ConcreteContainerNode node;

  @Mock
  AssociationDescriptorFactory descriptorFactory;

  @Mock
  AssociationManagerLocator managerLocator;

  @Mock
  AssociationDescriptor descriptor;

  @Mock
  ToManyAssociationManager defaultManager;

  @Mock
  ToManyAssociationManager manager;

  ToManyAssociationUpdater updater;

  @Before
  public void setUp() throws Exception {
    updater = newUpdater();
  }

  protected ToManyAssociationUpdater newUpdater() {
    return new ValueCollectionToManyAssociationUpdater(
        descriptorFactory, managerLocator);
  }

  @Test
  public void testUpdate() throws Exception {
    context.checking(resolveExpectations());
    context.checking(managerExpectations(resolvedValue()));
    updater.findManagerAndUpdate(node, OWNER, Collections.singletonList(value()),
        defaultManager, viewContext);
  }

  protected Object value() {
    return VALUE;
  }

  protected Object resolvedValue() {
    return VALUE;
  }

  protected Expectations resolveExpectations() throws Exception {
    return new Expectations() { {} };
  }

  @SuppressWarnings("unchecked")
  private Expectations managerExpectations(final Object value) throws Exception {
    return new Expectations() {
      {
        oneOf(descriptorFactory).newDescriptor(node);
        will(returnValue(descriptor));
        oneOf(managerLocator).findManager(ToManyAssociationManager.class,
            defaultManager, descriptor, node, viewContext);
        will(returnValue(manager));
        oneOf(manager).begin(OWNER);
        oneOf(manager).clear(OWNER);
        oneOf(manager).add(OWNER, value);
        oneOf(manager).end(OWNER);

      }
    };
  }

}
