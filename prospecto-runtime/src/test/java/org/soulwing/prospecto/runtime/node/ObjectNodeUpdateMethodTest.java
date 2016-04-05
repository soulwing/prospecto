/*
 * File created on Apr 2, 2016
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
package org.soulwing.prospecto.runtime.node;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewEntity;
import org.soulwing.prospecto.api.node.ViewNodeVisitor;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;
import org.soulwing.prospecto.runtime.event.ConcreteViewEvent;
import org.soulwing.prospecto.runtime.testing.JUnitRuleClassImposterizingMockery;

/**
 * Unit tests for {@link ObjectNodeUpdateMethod}.
 *
 * @author Carl Harris
 */
public class ObjectNodeUpdateMethodTest {

  private static final String NAME = "name";
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

  @Rule
  public final JUnitRuleMockery context =
      new JUnitRuleClassImposterizingMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  Options options;

  @Mock
  ContainerViewNode node;

  @Mock
  MockUpdatableContainerViewNode childContainer;

  @Mock
  MockUpdatableViewNode childValue;

  @Mock
  ViewEntityFactory entityFactory;

  @Mock
  MutableViewEntity entity;

  Deque<View.Event> events = new LinkedList<>();

  ObjectNodeUpdateMethod method;

  @Before
  public void setUp() throws Exception {
    method = new ObjectNodeUpdateMethod(node, TRIGGER_EVENT, events,
        viewContext, entityFactory);
  }

  @Test(expected = ModelEditorException.class)
  public void testToModelObjectWhenNoEndEvent() throws Exception {
    context.checking(entityFactoryExpectations());
    method.toModelValue();
  }

  @Test(expected = ModelEditorException.class)
  public void testToModelObjectWhenAnonymousEvent() throws Exception {
    context.checking(entityFactoryExpectations());
    events.add(ANONYMOUS_EVENT);
    method.toModelValue();
  }

  @Test(expected = ModelEditorException.class)
  public void testToModelObjectWhenChildNotFoundAndNotIgnored() throws Exception {
    context.checking(entityFactoryExpectations());
    context.checking(getChildExpectations(null));
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getOptions();
        will(returnValue(options));
        oneOf(options).isEnabled(ViewKeys.IGNORE_UNKNOWN_PROPERTIES);
        will(returnValue(false));
      }
    });
    events.add(VALUE_EVENT);
    method.toModelValue();
  }

  @Test(expected = ModelEditorException.class)
  public void testToModelObjectWhenChildNotFoundAndIgnored() throws Exception {
    context.checking(entityFactoryExpectations());
    context.checking(getChildExpectations(null));
    context.checking(new Expectations() {
      {
        oneOf(viewContext).getOptions();
        will(returnValue(options));
        oneOf(options).isEnabled(ViewKeys.IGNORE_UNKNOWN_PROPERTIES);
        will(returnValue(true));
      }
    });
    events.add(VALUE_EVENT);
    assertThat(method.toModelValue(), is(sameInstance((Object) entity)));
  }


  @Test
  public void testToModelObjectWhenNullUpdatableContainerNode()
      throws Exception {
    context.checking(entityFactoryExpectations());
    context.checking(getChildExpectations(childContainer));
    context.checking(entityPutExpectations(null, childContainer));
    events.add(NULL_VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(method.toModelValue(), is(sameInstance((Object) entity)));
  }

  @Test
  public void testToModelObjectWhenUndefinedValue()
      throws Exception {
    context.checking(entityFactoryExpectations());
    context.checking(getChildExpectations(childValue));
    context.checking(childModelValueExpectations(childValue, UndefinedValue.INSTANCE));
    events.add(VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(method.toModelValue(), is(sameInstance((Object) entity)));
  }

  @Test
  public void testToModelObject() throws Exception {
    context.checking(entityFactoryExpectations());
    context.checking(getChildExpectations(childValue));
    context.checking(childModelValueExpectations(childValue, MODEL_VALUE));
    context.checking(entityPutExpectations(MODEL_VALUE, childValue));
    events.add(VALUE_EVENT);
    events.add(END_EVENT);
    assertThat(method.toModelValue(), is(sameInstance((Object) entity)));
  }

  private Expectations entityFactoryExpectations() throws Exception {
    return new Expectations() {
      {
        oneOf(entityFactory).newEntity(node, events, viewContext);
        will(returnValue(entity));
      }
    };
  }

  private Expectations getChildExpectations(final AbstractViewNode child)
        throws Exception {
    return new Expectations() {
      {
        oneOf(entity).getType();
        will(returnValue(MockModel.class));
        oneOf(node).getChild(MockModel.class, NAME);
        will(returnValue(child));
        allowing(node).getName();
        will(returnValue(NAME));
      }
    };
  }

  private Expectations childModelValueExpectations(
      final UpdatableViewNode child, final Object modelValue)
      throws Exception {
    return new Expectations() {
      {
        oneOf(child).toModelValue(with(entity), with(any(View.Event.class)),
            with(events), with(viewContext));
        will(returnValue(modelValue));
      }
    };
  }

  private Expectations entityPutExpectations(final Object value,
      final MutableViewEntity.Injector injector) throws Exception {
    return new Expectations() {
      {
        oneOf(entity).put(NAME, value, injector);
      }
    };
  }

  private interface MockModel {}

  static class MockUpdatableContainerViewNode extends ContainerViewNode
      implements UpdatableViewNode {

    MockUpdatableContainerViewNode() {
      super(null, null, null);
    }

    @Override
    public Object accept(ViewNodeVisitor visitor, Object state) {
      return null;
    }

    @Override
    protected List<View.Event> onEvaluate(Object source,
        ScopedViewContext context) throws Exception {
      return null;
    }

    @Override
    public Object toModelValue(ViewEntity parentEntity,
        View.Event triggerEvent, Deque<View.Event> events,
        ScopedViewContext context) throws Exception {
      return null;
    }

    @Override
    public void inject(Object target, Object value) throws Exception {

    }

    @Override
    public void inject(Object target, Object value,
         ScopedViewContext context) throws Exception {

    }
  }

  static class MockUpdatableViewNode extends AbstractViewNode
      implements UpdatableViewNode {

    MockUpdatableViewNode() {
      super(null, null, null);
    }

    @Override
    public Object accept(ViewNodeVisitor visitor, Object state) {
      return null;
    }

    @Override
    public Accessor getAccessor() {
      return null;
    }

    @Override
    public void setAccessor(Accessor accessor) {
    }

    @Override
    protected List<View.Event> onEvaluate(Object source,
        ScopedViewContext context) throws Exception {
      return null;
    }

    @Override
    public Object toModelValue(ViewEntity parentEntity,
        View.Event triggerEvent, Deque<View.Event> events,
        ScopedViewContext context) throws Exception {
      return null;
    }

    @Override
    public void inject(Object target, Object value) throws Exception {

    }

    @Override
    public void inject(Object target, Object value,
        ScopedViewContext context) throws Exception {

    }

  }

}
