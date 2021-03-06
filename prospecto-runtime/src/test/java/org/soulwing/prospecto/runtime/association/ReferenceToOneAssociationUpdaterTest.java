/*
 * File created on Apr 7, 2016
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

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToOneAssociationManager;
import org.soulwing.prospecto.api.template.ContainerNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.reference.ReferenceResolverService;

/**
 * Unit tests for {@link ValueCollectionToManyAssociationUpdater}.
 *
 * @author Carl Harris
 */
public class ReferenceToOneAssociationUpdaterTest {

  private static final Object OWNER = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  ContainerNode node;

  @Mock
  AssociationDescriptorFactory descriptorFactory;

  @Mock
  AssociationManagerLocator managerLocator;

  @Mock
  AssociationDescriptor descriptor;

  @Mock
  ToOneAssociationManager defaultManager;

  @Mock
  ToOneAssociationManager manager;

  @Mock
  MockModel associate;

  @Mock
  InjectableViewEntity associateEntity;

  @Mock
  ReferenceResolverService resolvers;


  ToOneAssociationUpdater updater;


  @Before
  public void setUp() throws Exception {
    updater = newUpdater();
  }

  protected ToOneAssociationUpdater newUpdater() {
    return new ReferenceToOneAssociationUpdater(
        descriptorFactory, managerLocator);
  }

  @Test
  public void testUpdate() throws Exception {
    context.checking(resolveExpectations(associate));
    context.checking(managerExpectations(associate));
    updater.update(node, OWNER, associateEntity, defaultManager, viewContext);
  }

  @Test
  public void testUpdateWhenEntityNull() throws Exception {
    context.checking(managerExpectations(null));
    updater.update(node, OWNER, null, defaultManager, viewContext);
  }

  @Test
  public void testUpdateWhenUndefinedAssociate() throws Exception {
    context.checking(resolveExpectations(UndefinedValue.INSTANCE));
    context.checking(managerExpectations(UndefinedValue.INSTANCE));
    updater.update(node, OWNER, associateEntity, defaultManager, viewContext);
  }

  protected Expectations resolveExpectations(final Object associate)
      throws Exception {
    return new Expectations() {
      {
        oneOf(associateEntity).getType();
        will(returnValue(MockModel.class));
        oneOf(viewContext).getReferenceResolvers();
        will(returnValue(resolvers));
        oneOf(resolvers).resolve(MockModel.class, associateEntity);
        will(returnValue(associate));
      }
    };
  }

  @SuppressWarnings("unchecked")
  private Expectations managerExpectations(final Object associate)
      throws Exception {
    return new Expectations() {
      {
        oneOf(descriptorFactory).newDescriptor(node);
        will(returnValue(descriptor));
        oneOf(managerLocator).findManager(ToOneAssociationManager.class,
            defaultManager, descriptor, node, viewContext);
        will(returnValue(manager));
        if (associate != UndefinedValue.INSTANCE) {
          oneOf(manager).set(OWNER, associate);
        }
      }
    };
  }

  private interface MockModel {}

}
