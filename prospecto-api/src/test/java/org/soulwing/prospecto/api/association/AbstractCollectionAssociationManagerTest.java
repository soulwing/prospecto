/*
 * File created on Apr 17, 2016
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
package org.soulwing.prospecto.api.association;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collection;
import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractCollectionAssociationManager}
 * @author Carl Harris
 */
public class AbstractCollectionAssociationManagerTest {

  private static final Object OWNER = new Object();
  private static final Object ASSOCIATE = new Object();
  private static final int SIZE = -1;

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private Collection<Object> associates;

  @Mock
  private Iterator<Object> iterator;

  private MockCollectionAssociationManager manager;

  @Before
  public void setUp() throws Exception {
    manager = new MockCollectionAssociationManager(associates);
  }

  @Test
  public void testIterator() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).iterator();
        will(returnValue(iterator));
      }
    });

    assertThat(manager.iterator(OWNER), is(sameInstance(iterator)));
  }

  @Test
  public void testIteratorWhenCollectionNull() throws Exception {
    manager.setAssociates(OWNER, null);
    assertThat(manager.iterator(OWNER), is(nullValue()));
  }

  @Test
  public void testSize() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).size();
        will(returnValue(SIZE));
      }
    });

    assertThat(manager.size(OWNER), is(equalTo(SIZE)));
  }

  @Test
  public void testSizeWhenCollectionNull() throws Exception {
    manager.setAssociates(OWNER, null);
    assertThat(manager.size(OWNER), is(0));
  }

  @Test
  public void testAdd() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).add(ASSOCIATE);
      }
    });

    manager.add(OWNER, ASSOCIATE);
  }

  @Test
  public void testAddWhenCollectionNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).add(ASSOCIATE);
      }
    });

    manager.setAssociates(OWNER, null);
    manager.add(OWNER, ASSOCIATE);
  }

  @Test
  public void testRemove() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).remove(ASSOCIATE);
        will(returnValue(true));
      }
    });
    assertThat(manager.remove(OWNER, ASSOCIATE), is(true));
  }

  @Test
  public void testRemoveWhenCollectionNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).remove(ASSOCIATE);
        will(returnValue(true));
      }
    });

    manager.setAssociates(OWNER, null);
    manager.remove(OWNER, ASSOCIATE);
  }

  @Test
  public void testClear() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).clear();
      }
    });

    manager.clear(OWNER);
  }

  @Test
  public void testClearWhenCollectionNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).clear();
      }
    });

    manager.setAssociates(OWNER, null);
    manager.clear(OWNER);
  }

  private static class MockCollectionAssociationManager
      extends AbstractCollectionAssociationManager<Object, Object> {

    private Collection<Object> initAssociates;
    private Collection<Object> associates;

    public MockCollectionAssociationManager(Collection<Object> associates) {
      this.initAssociates = associates;
      this.associates = associates;
    }

    @Override
    protected Collection<Object> getAssociates(Object owner) {
      return associates;
    }

    @Override
    protected void setAssociates(Object owner, Collection<Object> associates)
        throws Exception {
      this.associates = associates;
    }

    @Override
    protected Collection<Object> newAssociates() {
      return initAssociates;
    }

    @Override
    public boolean supports(AssociationDescriptor descriptor) {
      return false;
    }

  }
}
