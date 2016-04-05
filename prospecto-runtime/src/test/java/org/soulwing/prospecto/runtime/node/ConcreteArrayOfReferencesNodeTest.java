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
package org.soulwing.prospecto.runtime.node;

import static org.hamcrest.Matchers.contains;

import java.util.Collection;
import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.runtime.accessor.Accessor;
import org.soulwing.prospecto.runtime.accessor.MultiValuedAccessor;
import org.soulwing.prospecto.runtime.association.ToManyAssociationUpdater;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.entity.MutableViewEntity;

/**
 * Unit tests for {@link ConcreteArrayOfReferencesNode}.
 *
 * @author Carl Harris
 */
public class ConcreteArrayOfReferencesNodeTest {

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  ScopedViewContext viewContext;

  @Mock
  ToManyAssociationUpdater associationUpdater;

  @Mock
  MutableViewEntity entity;

  @Mock
  MockModel target;

  @Mock
  Accessor accessor;

  private ConcreteArrayOfReferencesNode node;

  @Before
  public void setUp() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(accessor).getDataType();
        will(returnValue(Collection.class));
      }
    });

    node = new ConcreteArrayOfReferencesNode(null, null, null, null,
        associationUpdater);
    node.setAccessor(accessor);
  }

  @Test
  public void testInject() throws Exception {
    node.inject(new Object(), new Object());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testInjectWithContext() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associationUpdater).update(with(node), with(target),
            with(contains(entity)),
            with(any(MultiValuedAccessor.class)),
            with(viewContext));
      }
    });

    node.inject(target, Collections.singletonList(entity), viewContext);
  }

  private interface MockModel {}
}
