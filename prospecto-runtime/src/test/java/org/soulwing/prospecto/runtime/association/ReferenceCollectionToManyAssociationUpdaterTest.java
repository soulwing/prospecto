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

import java.util.Arrays;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToManyAssociationManager;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.factory.ObjectFactoryService;
import org.soulwing.prospecto.runtime.reference.ReferenceResolverService;

/**
 * Unit tests for {@link ReferenceCollectionToManyAssociationUpdater}.
 *
 * @author Carl Harris
 */
public class ReferenceCollectionToManyAssociationUpdaterTest {

  private static final Object OWNER = new Object();

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private AssociationDescriptorFactory descriptorFactory;

  @Mock
  private AssociationManagerLocator managerLocator;

  @Mock
  private UpdatableNode node;

  @Mock
  private AssociationDescriptor descriptor;

  @Mock
  private ToManyAssociationManager defaultManager;

  @Mock
  private ToManyAssociationManager manager;

  @Mock
  private MockModel associate, newAssociate, missingAssociate;

  @Mock
  private InjectableViewEntity associateEntity, newAssociateEntity;

  @Mock
  private ReferenceResolverService resolvers;

  @Mock
  private ObjectFactoryService objectFactoryService;

  private ReferenceCollectionToManyAssociationUpdater updater;

  @Before
  public void setUp() throws Exception {
    updater = new ReferenceCollectionToManyAssociationUpdater(descriptorFactory,
        managerLocator);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUpdate() throws Exception {
    final Sequence sequence = context.sequence("updateSequence");
    context.checking(findManagerExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getObjectFactories();
        will(returnValue(objectFactoryService));
        oneOf(manager).begin(OWNER);
        inSequence(sequence);

        oneOf(manager).findAssociate(OWNER, associateEntity, objectFactoryService);
        will(returnValue(associate));
        oneOf(manager).findAssociate(OWNER, newAssociateEntity, objectFactoryService);
        will(returnValue(null));

        oneOf(newAssociateEntity).getType();
        will(returnValue(MockModel.class));
        oneOf(viewContext).getReferenceResolvers();
        will(returnValue(resolvers));
        oneOf(resolvers).resolve(MockModel.class, newAssociateEntity);
        will(returnValue(newAssociate));

        oneOf(manager).add(OWNER, newAssociate);
        inSequence(sequence);

        oneOf(manager).iterator(OWNER);
        will(returnValue(Arrays.asList(associate, missingAssociate, newAssociate).iterator()));

        oneOf(manager).remove(OWNER, missingAssociate);

        oneOf(manager).end(OWNER);
        inSequence(sequence);
      }
    });

    updater.findManagerAndUpdate(node, OWNER,
        Arrays.asList(associateEntity, newAssociateEntity),
        defaultManager, viewContext);
  }


  @SuppressWarnings("unchecked")
  private Expectations findManagerExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(descriptorFactory).newDescriptor(node);
        will(returnValue(descriptor));
        oneOf(managerLocator).findManager(ToManyAssociationManager.class,
            defaultManager, descriptor, node, viewContext);
        will(returnValue(manager));
      }
    };
  }

  private interface MockModel {}

}
