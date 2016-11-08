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

import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.eventDescribing;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.forModel;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.inContext;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.propertyValue;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.association.AssociationDescriptor;
import org.soulwing.prospecto.api.association.ToOneAssociationManager;
import org.soulwing.prospecto.api.factory.ObjectFactory;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.factory.ObjectFactoryService;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;
import org.soulwing.prospecto.runtime.template.ConcreteContainerNode;
import org.soulwing.prospecto.runtime.testing.JUnitRuleClassImposterizingMockery;

/**
 * Unit tests for {@link ConcreteToOneAssociationUpdater}.
 *
 * @author Carl Harris
 */
public class ConcreteToOneAssociationUpdaterTest {

  @Rule
  public final JUnitRuleMockery context =
      new JUnitRuleClassImposterizingMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  NotifiableViewListeners listeners;

  @Mock
  ObjectFactoryService objectFactory;

  @Mock
  ConcreteContainerNode node;

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
  MockModel owner;

  @Mock
  MockModel currentAssociate;

  @Mock
  MockModel newAssociate;

  @Mock
  InjectableViewEntity associateEntity;

  ConcreteToOneAssociationUpdater updater;

  @Before
  public void setUp() throws Exception {
    updater = new ConcreteToOneAssociationUpdater(descriptorFactory,
        managerLocator);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUpdateNullToNull() throws Exception {
    context.checking(findManagerExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getObjectFactories();
        will(returnValue(objectFactory));
        oneOf(manager).get(owner);
        will(returnValue(null));
        oneOf(manager).isSameAssociate(owner, null, objectFactory);
        will(returnValue(true));
      }
    });

    updater.update(node, owner, null, defaultManager, viewContext);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUpdateNullToNonNull() throws Exception {
    context.checking(findManagerExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getObjectFactories();
        will(returnValue(objectFactory));
        oneOf(manager).get(owner);
        will(returnValue(null));
        oneOf(manager).isSameAssociate(owner, associateEntity, objectFactory);
        will(returnValue(false));
      }
    });

    context.checking(createAssociateExpectations(newAssociate));
    updater.update(node, owner, associateEntity, defaultManager, viewContext);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUpdateSameAssociate() throws Exception {
    context.checking(findManagerExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getObjectFactories();
        will(returnValue(objectFactory));
        oneOf(manager).get(owner);
        will(returnValue(currentAssociate));
        oneOf(manager).isSameAssociate(owner, associateEntity, objectFactory);
        will(returnValue(true));
        oneOf(associateEntity).inject(currentAssociate, viewContext);
      }
    });

    updater.update(node, owner, associateEntity, defaultManager, viewContext);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUpdateDifferentAssociate() throws Exception {
    context.checking(findManagerExpectations());
    context.checking(new Expectations() {
      {
        allowing(viewContext).getObjectFactories();
        will(returnValue(objectFactory));
        oneOf(manager).get(owner);
        will(returnValue(currentAssociate));
        oneOf(manager).isSameAssociate(owner, associateEntity, objectFactory);
        will(returnValue(false));
      }
    });
    context.checking(discardAssociateExpectations(currentAssociate));
    context.checking(createAssociateExpectations(newAssociate));
    updater.update(node, owner, associateEntity, defaultManager, viewContext);
  }

  @SuppressWarnings("unchecked")
  private Expectations findManagerExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(descriptorFactory).newDescriptor(node);
        will(returnValue(descriptor));
        oneOf(managerLocator).findManager(ToOneAssociationManager.class,
            defaultManager, descriptor, node, viewContext);
        will(returnValue(manager));
      }
    };
  }

  @SuppressWarnings("unchecked")
  private Expectations discardAssociateExpectations(final Object associate)
      throws Exception {
    return new Expectations() {
      {
        oneOf(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).entityDiscarded((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node), forModel(owner),
                propertyValue(associate), inContext(viewContext))));
      }
    };
  }

  @SuppressWarnings("unchecked")
  private Expectations createAssociateExpectations(final Object associate)
      throws Exception {
    return new Expectations() {
      {
        oneOf(manager).newAssociate(owner, associateEntity, objectFactory);
        will(returnValue(associate));
        oneOf(associateEntity).inject(associate, viewContext);
        oneOf(manager).set(owner, associate);
        oneOf(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).entityCreated((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node), forModel(owner),
                propertyValue(associate), inContext(viewContext))));
      }
    };
  }

  interface MockModel {}

}
