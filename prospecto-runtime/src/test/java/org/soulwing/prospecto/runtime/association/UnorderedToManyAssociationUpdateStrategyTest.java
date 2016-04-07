/*
 * File created on Mar 30, 2016
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
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.propertyValue;
import static org.soulwing.prospecto.runtime.listener.ViewNodeEventMatchers.sourceNode;

import java.util.Arrays;
import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.association.ToManyIndexedAssociationManager;
import org.soulwing.prospecto.api.listener.ViewNodePropertyEvent;
import org.soulwing.prospecto.api.node.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link OrderedToManyAssociationUpdateStrategy}.
 *
 * @author Carl Harris
 */
@SuppressWarnings({ "unused", "unchecked" })
public class UnorderedToManyAssociationUpdateStrategyTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private UpdatableNode node;

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private NotifiableViewListeners listeners;

  @Mock
  private ToManyIndexedAssociationManager manager;

  @Mock
  private MutableViewEntity a;

  @Mock
  private MutableViewEntity b;

  @Mock
  private MutableViewEntity c;

  @Mock
  private MockModel elementA;

  @Mock
  private MockModel elementB;

  @Mock
  private MockModel elementC;

  @Mock
  private MockModel owner;


  @Test
  public void testAddAll() throws Exception {
    /* view: [a, b]
     * model: []
     */

    context.checking(findElementExpectations(a, null));
    context.checking(createElementExpectations(a, elementA));
    context.checking(findElementExpectations(b, null));
    context.checking(createElementExpectations(b, elementB));
    context.checking(copyElementsExpectations());

    UnorderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b), manager, viewContext);
  }

  @Test
  public void testRemoveAll() throws Exception {
    /* view: []
     * model: [a, b]
     */

    context.checking(removeElementExpectations(elementA));
    context.checking(removeElementExpectations(elementB));
    context.checking(copyElementsExpectations(elementA, elementB));

    UnorderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Collections.<MutableViewEntity>emptyList(),
            manager, viewContext);
  }

  @Test
  public void testUpdateExistingElementsInPlace() throws Exception {
    /* view: [a, b]
     * model: [a, b]
     */

    context.checking(findElementExpectations(a, elementA));
    context.checking(updateElementExpectations(a, elementA));
    context.checking(findElementExpectations(b, elementB));
    context.checking(updateElementExpectations(b, elementB));
    context.checking(copyElementsExpectations());

    UnorderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b), manager, viewContext);
  }

  @Test
  public void testRemoveMissingElements() throws Exception {
    /* view: [a, b]
     * model: [a, b, c]
     */

    context.checking(findElementExpectations(a, elementA));
    context.checking(updateElementExpectations(a, elementA));
    context.checking(findElementExpectations(b, elementB));
    context.checking(updateElementExpectations(b, elementB));
    context.checking(copyElementsExpectations(elementA, elementB, elementC));
    context.checking(removeElementExpectations(elementC));

    UnorderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b), manager, viewContext);
  }

  private Expectations findElementExpectations(final MutableViewEntity entity,
      final Object element) throws Exception {
    return new Expectations() {
      {
        oneOf(manager).findAssociate(owner, entity);
        will(returnValue(element));
      }
    };
  }

  private Expectations createElementExpectations(final MutableViewEntity entity,
      final Object element) throws Exception {
    return new Expectations() {
      {
        oneOf(manager).newAssociate(owner, entity);
        will(returnValue(element));
        oneOf(entity).inject(element, viewContext);

        oneOf(manager).add(owner, element);

        oneOf(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).entityCreated((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node),
                forModel(owner), propertyValue(element))));
      }
    };
  }

  private Expectations updateElementExpectations(final MutableViewEntity entity,
      final Object element) throws Exception {
    return new Expectations() {
      {
        oneOf(entity).inject(element, viewContext);
      }
    };
  }

  private Expectations copyElementsExpectations(final Object... elements)
      throws Exception {
    return new Expectations() {
      {
        allowing(manager).iterator(owner);
        will(returnValue(Arrays.asList(elements).iterator()));
      }
    };
  }

  private Expectations removeElementExpectations(final Object element)
      throws Exception {
    return new Expectations() {
      {
        oneOf(manager).remove(owner, element);

        oneOf(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).entityDiscarded((ViewNodePropertyEvent)
            with(eventDescribing(sourceNode(node),
                forModel(owner), propertyValue(element))));
      }
    };
  }


  private interface MockModel {}

}
