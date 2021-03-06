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
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.InjectableViewEntity;
import org.soulwing.prospecto.runtime.factory.ObjectFactoryService;
import org.soulwing.prospecto.runtime.listener.NotifiableViewListeners;

/**
 * Unit tests for {@link OrderedToManyAssociationUpdateStrategy}.
 *
 * @author Carl Harris
 */
@SuppressWarnings({ "unused", "unchecked" })
public class OrderedToManyAssociationUpdateStrategyTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private UpdatableNode node;

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private ObjectFactoryService objectFactory;

  @Mock
  private NotifiableViewListeners listeners;

  @Mock
  private ToManyIndexedAssociationManager manager;

  @Mock
  private InjectableViewEntity a;

  @Mock
  private InjectableViewEntity b;

  @Mock
  private InjectableViewEntity c;

  @Mock
  private InjectableViewEntity d;

  @Mock
  private InjectableViewEntity e;

  @Mock
  private MockModel elementA;

  @Mock
  private MockModel elementB;

  @Mock
  private MockModel elementC;

  @Mock
  private MockModel elementD;

  @Mock
  private MockModel elementE;

  @Mock
  private MockModel owner;


  @Test
  public void testAddAll() throws Exception {
    /* view: [a, b]
     * model: []
     */

    context.checking(findElementExpectations(a, -1));
    context.checking(createElementExpectations(a, 0, elementA));
    context.checking(findElementExpectations(b, -1));
    context.checking(createElementExpectations(b, 1, elementB));
    context.checking(modelSizeExpectations(2));

    OrderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b), manager, viewContext);
  }

  @Test
  public void testRemoveAll() throws Exception {
    /* view: []
     * model: [a, b]
     */

    context.checking(modelSizeExpectations(2));
    context.checking(removeElementExpectations(0, elementA));
    context.checking(removeElementExpectations(0, elementB));

    OrderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Collections.<InjectableViewEntity>emptyList(),
            manager, viewContext);
  }

  @Test
  public void testUpdateExistingElementsInPlace() throws Exception {
    /* view: [a, b]
     * model: [a, b]
     */

    context.checking(findElementExpectations(a, 0));
    context.checking(updateElementExpectations(a, 0, elementA));
    context.checking(findElementExpectations(b, 1));
    context.checking(updateElementExpectations(b, 1, elementB));
    context.checking(modelSizeExpectations(2));

    OrderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b), manager, viewContext);
  }

  @Test
  public void testReorderElements() throws Exception {
    /* view: [a, b, c, d]
     * model: [c, d, a, b]
     */

    context.checking(findElementExpectations(a, 2));
    context.checking(updateElementExpectations(a, 2, elementA));
    context.checking(relocateElementExpectations(0, 2, elementA));

    /* model after relocate a: [a, c, d, b] */
    context.checking(findElementExpectations(b, 3));
    context.checking(updateElementExpectations(b, 3, elementB));
    context.checking(relocateElementExpectations(1, 3, elementB));

    /* model after relocate b: [a, b, c, d] */
    context.checking(findElementExpectations(c, 2));
    context.checking(updateElementExpectations(c, 2, elementC));
    context.checking(findElementExpectations(d, 3));
    context.checking(updateElementExpectations(d, 3, elementD));

    context.checking(modelSizeExpectations(4));

    OrderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b, c, d), manager, viewContext);

  }

  @Test
  public void testRemoveMissingElements() throws Exception {
    /* view: [a, b]
     * model: [a, b, c]
     */

    context.checking(findElementExpectations(a, 0));
    context.checking(updateElementExpectations(a, 0, elementA));
    context.checking(findElementExpectations(b, 1));
    context.checking(updateElementExpectations(b, 1, elementB));

    context.checking(modelSizeExpectations(3));
    context.checking(removeElementExpectations(2, elementA));

    OrderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b), manager, viewContext);
  }

  @Test
  public void testReorderAndRemoveMissingElements() throws Exception {
    /* view: [a, b, c]
     * model: [e, d, c, b, a]
     */

    context.checking(findElementExpectations(a, 4));
    context.checking(updateElementExpectations(a, 4, elementA));
    context.checking(relocateElementExpectations(0, 4, elementA));

    /* model after relocate a: [a, e, d, c, b] */
    context.checking(findElementExpectations(b, 4));
    context.checking(updateElementExpectations(b, 4, elementB));
    context.checking(relocateElementExpectations(1, 4, elementB));

    /* model after relocate b: [a, b, e, d, c] */
    context.checking(findElementExpectations(c, 4));
    context.checking(updateElementExpectations(c, 4, elementC));
    context.checking(relocateElementExpectations(2, 4, elementC));

    /* model after relocate c: [a, b, c, e, d] */
    context.checking(modelSizeExpectations(5));
    context.checking(removeElementExpectations(3, elementE));
    context.checking(removeElementExpectations(3, elementD));

    OrderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b, c), manager, viewContext);
  }

  @Test
  public void testAddUpdateReorderAndRemove() throws Exception {
    /* view: [a, b, c]
     * model: [e, d, b]
     */
    context.checking(findElementExpectations(a, -1));
    context.checking(createElementExpectations(a, 0, elementA));

    /* model after add a: [a, e, d, b] */
    context.checking(findElementExpectations(b, 3));
    context.checking(updateElementExpectations(b, 3, elementB));
    context.checking(relocateElementExpectations(1, 3, elementB));

    /* model after relocate b: [a, b, e, d] */
    context.checking(findElementExpectations(c, -1));
    context.checking(createElementExpectations(c, 2, elementC));

    /* model after add c: [a, b, c, e, d] */

    context.checking(modelSizeExpectations(5));
    context.checking(removeElementExpectations(3, elementE));
    context.checking(removeElementExpectations(3, elementD));

    OrderedToManyAssociationUpdateStrategy.INSTANCE
        .update(node, owner, Arrays.asList(a, b, c), manager, viewContext);
  }

  private Expectations findElementExpectations(final InjectableViewEntity entity,
      final int modelIndex) throws Exception {
    return new Expectations() {
      {
        allowing(viewContext).getObjectFactories();
        will(returnValue(objectFactory));
        oneOf(manager).indexOf(owner, entity, objectFactory);
        will(returnValue(modelIndex));
      }
    };
  }

  private Expectations createElementExpectations(final InjectableViewEntity entity,
      final int modelIndex, final Object element) throws Exception {
    return new Expectations() {
      {
        oneOf(manager).newAssociate(owner, entity, objectFactory);
        will(returnValue(element));
        oneOf(entity).inject(element, viewContext);
        oneOf(manager).add(owner, modelIndex, element);

        oneOf(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).entityCreated((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node),
                forModel(owner), propertyValue(element))));
      }
    };
  }

  private Expectations updateElementExpectations(final InjectableViewEntity entity,
      final int modelIndex, final Object element) throws Exception {
    return new Expectations() {
      {
        oneOf(manager).get(owner, modelIndex);
        will(returnValue(element));
        oneOf(entity).inject(element, viewContext);
      }
    };
  }

  private Expectations relocateElementExpectations(final int viewIndex,
      final int modelIndex, final Object element) throws Exception {
    return new Expectations() {
      {
        oneOf(manager).remove(owner, modelIndex);
        oneOf(manager).add(owner, viewIndex, element);
      }
    };
  }

  private Expectations removeElementExpectations(final int modelIndex,
      final Object element) throws Exception {
    return new Expectations() {
      {
        oneOf(manager).get(owner, modelIndex);
        will(returnValue(element));
        oneOf(manager).remove(owner, modelIndex);

        oneOf(viewContext).getListeners();
        will(returnValue(listeners));
        oneOf(listeners).entityDiscarded((ViewNodePropertyEvent) with(
            eventDescribing(sourceNode(node),
                forModel(owner), propertyValue(element))));
      }
    };
  }

  private Expectations modelSizeExpectations(final int size) throws Exception {
    return new Expectations() {
      {
        oneOf(manager).size(owner);
        will(returnValue(size));
      }
    };
  }


  private interface MockModel {}

}
