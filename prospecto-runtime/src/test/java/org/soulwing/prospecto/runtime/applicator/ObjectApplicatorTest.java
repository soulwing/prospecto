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

import java.util.Collections;

import org.jmock.Expectations;
import org.junit.Test;
import org.soulwing.prospecto.api.node.ObjectNode;

/**
 * Unit tests for {@link ObjectApplicator}.
 *
 * @author Carl Harris
 */
public class ObjectApplicatorTest
    extends AbstractObjectApplicatorTest<ObjectNode> {

  @Override
  ObjectNode newNode() {
    return context.mock(ObjectNode.class);
  }

  @Override
  AbstractViewEventApplicator<ObjectNode> newApplicator(ObjectNode node) {
    return new ObjectApplicator(node, children, entityFactory,
        transformationService, associationUpdater);
  }

  @Test
  public void testInject() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(entity).nameSet();
        will(returnValue(Collections.singleton(NAME)));
        oneOf(entity).getType();
        will(returnValue(MockModel.class));
        allowing(child).getNode();
        will(returnValue(childNode));
        oneOf(childNode).getName();
        will(returnValue(NAME));
      }
    });

    children.add(child);
    applicator.inject(MODEL, entity);
  }

  @Test
  public void testInjectWhenPropertyNotFound() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(entity).nameSet();
        will(returnValue(Collections.singleton(NAME)));
        oneOf(entity).getType();
        will(returnValue(MockModel.class));
      }
    });

    applicator.inject(MODEL, entity);
  }

  private interface MockModel {}

}
