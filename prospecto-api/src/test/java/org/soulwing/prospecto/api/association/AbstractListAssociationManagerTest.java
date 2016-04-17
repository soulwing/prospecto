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

import java.util.Iterator;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractListAssociationManager}
 * @author Carl Harris
 */
public class AbstractListAssociationManagerTest {

  private static final Object OWNER = new Object();
  private static final Object ASSOCIATE = new Object();
  private static final int SIZE = -1;
  private static final int INDEX = -2;

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private List<Object> associates;

  @Mock
  private Iterator<Object> iterator;

  private MockListAssociationManager manager;

  @Before
  public void setUp() throws Exception {
    manager = new MockListAssociationManager(associates);
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
  public void testIteratorWhenListNull() throws Exception {
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
  public void testSizeWhenListNull() throws Exception {
    manager.setAssociates(OWNER, null);
    assertThat(manager.size(OWNER), is(equalTo(0)));
  }


  @Test
  public void testGet() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).get(INDEX);
        will(returnValue(ASSOCIATE));
      }
    });

    assertThat(manager.get(OWNER, INDEX), is(sameInstance(ASSOCIATE)));
  }

  @Test
  public void testGetWhenListNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).get(INDEX);
        will(returnValue(ASSOCIATE));
      }
    });

    manager.setAssociates(OWNER, null);
    assertThat(manager.get(OWNER, INDEX), is(sameInstance(ASSOCIATE)));
  }

  @Test
  public void testSet() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).set(INDEX, ASSOCIATE);
      }
    });

    manager.set(OWNER, INDEX, ASSOCIATE);
  }

  @Test
  public void testSetWhenListNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).set(INDEX, ASSOCIATE);
      }
    });

    manager.setAssociates(OWNER, null);
    manager.set(OWNER, INDEX, ASSOCIATE);
  }

  @Test
  public void testAddAssociate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).add(ASSOCIATE);
      }
    });

    manager.add(OWNER, ASSOCIATE);
  }

  @Test
  public void testAddAssociateWhenListNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).add(ASSOCIATE);
      }
    });

    manager.setAssociates(OWNER, null);
    manager.add(OWNER, ASSOCIATE);
  }

  @Test
  public void testAddIndexAssociate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).add(INDEX, ASSOCIATE);
      }
    });

    manager.add(OWNER, INDEX, ASSOCIATE);
  }

  @Test
  public void testAddIndexAssociateWhenListNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).add(INDEX, ASSOCIATE);
      }
    });

    manager.setAssociates(OWNER, null);
    manager.add(OWNER, INDEX, ASSOCIATE);
  }

  @Test
  public void testRemoveAssociate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).remove(ASSOCIATE);
        will(returnValue(true));
      }
    });

    assertThat(manager.remove(OWNER, ASSOCIATE), is(true));
  }

  @Test
  public void testRemoveAssociateWhenListNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).remove(ASSOCIATE);
        will(returnValue(true));
      }
    });

    manager.setAssociates(OWNER, null);
    assertThat(manager.remove(OWNER, ASSOCIATE), is(true));
  }

  @Test
  public void testRemoveAtIndex() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).remove(INDEX);
        will(returnValue(true));
      }
    });

    manager.remove(OWNER, INDEX);
  }

  @Test
  public void testRemoveAtIndexWhenListNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).remove(INDEX);
        will(returnValue(true));
      }
    });

    manager.setAssociates(OWNER, null);
    manager.remove(OWNER, INDEX);
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
  public void testClearWhenListNull() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(associates).clear();
      }
    });

    manager.setAssociates(OWNER, null);
    manager.clear(OWNER);
  }


  private static class MockListAssociationManager
      extends AbstractListAssociationManager<Object, Object> {

    private final List<Object> initAssociates;
    private List<Object> associates;

    public MockListAssociationManager(List<Object> associates) {
      this.initAssociates = associates;
      this.associates = associates;
    }

    @Override
    protected List<Object> getAssociates(Object owner) {
      return associates;
    }

    @Override
    protected void setAssociates(Object owner, List<Object> associates)
        throws Exception {
      this.associates = associates;
    }

    @Override
    protected List<Object> newAssociates() {
      return initAssociates;
    }

    @Override
    public boolean supports(AssociationDescriptor descriptor) {
      return false;
    }

  }
}
