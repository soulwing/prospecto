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
import org.soulwing.prospecto.api.node.ReferenceNode;

/**
 * Unit tests for {@link ReferenceApplicator}.
 *
 * @author Carl Harris
 */
public class ReferenceApplicatorTest
    extends AbstractObjectApplicatorTest<ReferenceNode> {

  @Override
  ReferenceNode newNode() {
    return context.mock(ReferenceNode.class);
  }

  @Override
  AbstractViewEventApplicator<ReferenceNode> newApplicator(ReferenceNode node) {
    return new ReferenceApplicator(node,
        Collections.<ViewEventApplicator>emptyList(), entityFactory,
        transformationService, associationUpdater, applicatorLocator);
  }

  @Test
  public void testInject() throws Exception {
    context.checking(new Expectations() { {} });
    applicator.inject(MODEL, entity);
  }

}
