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
package org.soulwing.prospecto.runtime.applicator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Test;
import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.node.ContainerNode;
import org.soulwing.prospecto.api.node.ObjectNode;
import org.soulwing.prospecto.api.node.UpdatableNode;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.runtime.association.ToOneAssociationUpdater;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.entity.ViewEntityFactory;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.listener.TransformationService;

/**
 * An abstract base for tests of {@link AbstractObjectApplicator}
 * implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractObjectApplicatorTest<N extends ObjectNode>
    extends AbstractViewEventApplicatorTest<N> {

  static final Object MODEL = new Object();

  private static final Object VIEW_VALUE = new Object();
  private static final Object MODEL_VALUE = new Object();

  private static final View.Event TRIGGER_EVENT =
      new ConcreteViewEvent(View.Event.Type.BEGIN_OBJECT, null, null, null);

  private static final View.Event ANONYMOUS_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, null, null, null);

  private static final View.Event VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, VIEW_VALUE);

  private static final View.Event NULL_VALUE_EVENT =
      new ConcreteViewEvent(View.Event.Type.VALUE, NAME, null, null);

  private static final View.Event END_EVENT =
      new ConcreteViewEvent(View.Event.Type.END_OBJECT, null, null, null);

  @Mock
  ContainerNode childContainer;

  @Mock
  ViewEventApplicator child;

  @Mock
  UpdatableNode childNode;

  @Mock
  ViewEntityFactory entityFactory;

  @Mock
  MutableViewEntity entity;

  @Mock
  ToOneAssociationUpdater associationUpdater;

  @Mock
  TransformationService transformationService;

  @Mock
  ContainerApplicatorLocator applicatorLocator;

  @Mock
  Options options;

  @Test(expected = ModelEditorException.class)
  public void testToModelObjectWhenNoEndEvent() throws Exception {
    context.checking(baseExpectations(false));
    context.checking(contextScopeExpectations(false));
    context.checking(entityFactoryExpectations());
    applicator.toModelValue(parentEntity, TRIGGER_EVENT, events, viewContext);
  }

  @Test(expected = ModelEditorException.class)
  public void testToModelObjectWhenAnonymousEvent() throws Exception {
    context.checking(baseExpectations(false));
    context.checking(contextScopeExpectations(false));
    context.checking(entityFactoryExpectations());
    events.add(ANONYMOUS_EVENT);
    applicator.toModelValue(parentEntity, TRIGGER_EVENT, events, viewContext);
  }

  @Test(expected = ModelEditorException.class)
  public void testToModelObjectWhenChildNotFoundAndNotIgnored() throws Exception {
    context.checking(baseExpectations(false));
    context.checking(contextScopeExpectations(false));
    context.checking(entityFactoryExpectations());
    context.checking(findDescendantExpectations(null, null));
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getOptions();
        will(returnValue(options));
        oneOf(options).isEnabled(ViewKeys.IGNORE_UNKNOWN_PROPERTIES);
        will(returnValue(false));
      }
    });
    events.add(VALUE_EVENT);
    applicator.toModelValue(parentEntity, TRIGGER_EVENT, events, viewContext);
  }

  @Test
  public void testToModelObjectWhenChildNotFoundAndIgnored() throws Exception {
    context.checking(baseExpectations(true));
    context.checking(contextScopeExpectations(true));
    context.checking(entityFactoryExpectations());
    context.checking(findDescendantExpectations(null, null));
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getOptions();
        will(returnValue(options));
        oneOf(options).isEnabled(ViewKeys.IGNORE_UNKNOWN_PROPERTIES);
        will(returnValue(true));
      }
    });

    events.add(VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(applicator.toModelValue(
        parentEntity, TRIGGER_EVENT, events, viewContext),
        is(sameInstance((Object) entity)));
  }

  @Test
  public void testToModelObjectWhenNullUpdatableContainerNode()
      throws Exception {
    context.checking(baseExpectations(true));
    context.checking(contextScopeExpectations(true));
    context.checking(entityFactoryExpectations());
    context.checking(findDescendantExpectations(child, childContainer));
    context.checking(entityPutExpectations(MODEL_VALUE));
    context.checking(new Expectations() {
      {
        oneOf(node).getModelType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity, Object.class,
            null, node, viewContext);
        will(returnValue(MODEL_VALUE));
      }
    });

    events.add(NULL_VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(applicator.toModelValue(
        parentEntity, TRIGGER_EVENT, events, viewContext),
        is(sameInstance((Object) entity)));
  }

  @Test
  public void testToModelObjectWhenNullUpdatableContainerNodeUndefined()
      throws Exception {
    context.checking(baseExpectations(true));
    context.checking(contextScopeExpectations(true));
    context.checking(entityFactoryExpectations());
    context.checking(findDescendantExpectations(child, childContainer));
    context.checking(new Expectations() {
      {
        oneOf(node).getModelType();
        will(returnValue(Object.class));
        oneOf(transformationService).valueToInject(parentEntity, Object.class,
            null, node, viewContext);
        will(returnValue(UndefinedValue.INSTANCE));
      }
    });

    events.add(NULL_VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(applicator.toModelValue(
        parentEntity, TRIGGER_EVENT, events, viewContext),
        is(sameInstance((Object) entity)));
  }


  @Test
  public void testToModelObjectWhenUndefinedValue()
      throws Exception {
    context.checking(baseExpectations(true));
    context.checking(contextScopeExpectations(true));
    context.checking(entityFactoryExpectations());
    context.checking(findDescendantExpectations(child, childNode));
    context.checking(childModelValueExpectations(UndefinedValue.INSTANCE));

    events.add(VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(applicator.toModelValue(
        parentEntity, TRIGGER_EVENT, events, viewContext),
        is(sameInstance((Object) entity)));
  }

  @Test
  public void testToModelObject() throws Exception {
    context.checking(baseExpectations(true));
    context.checking(contextScopeExpectations(true));
    context.checking(entityFactoryExpectations());
    context.checking(findDescendantExpectations(child, childNode));
    context.checking(childModelValueExpectations(MODEL_VALUE));
    context.checking(entityPutExpectations(MODEL_VALUE));

    events.add(VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(applicator.toModelValue(
        parentEntity, TRIGGER_EVENT, events, viewContext),
        is(sameInstance((Object) entity)));
  }

  private Expectations entityFactoryExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(entityFactory).newEntity(node, events, viewContext);
        will(returnValue(entity));
      }
    };
  }

  private Expectations findDescendantExpectations(
      final ViewEventApplicator descendant, final UpdatableNode childNode)
      throws Exception {
    return new Expectations() {
      {
        oneOf(entity).getType();
        will(returnValue(MockModel.class));
        oneOf(applicatorLocator).findApplicator(NAME, MockModel.class,
            (ContainerApplicator) applicator);
        will(returnValue(descendant));
        allowing(child).getNode();
        will(returnValue(childNode));
      }
    };
  }

  private Expectations childModelValueExpectations(final Object modelValue)
      throws Exception {
    return new Expectations() {
      {
        oneOf(child).toModelValue(with(entity), with(any(View.Event.class)),
            with(events), with(viewContext));
        will(returnValue(modelValue));
      }
    };
  }

  private Expectations entityPutExpectations(final Object value) throws Exception {
    return new Expectations() {
      {
        oneOf(entity).put(NAME, value, child);
      }
    };
  }

  private interface MockModel {}

}
