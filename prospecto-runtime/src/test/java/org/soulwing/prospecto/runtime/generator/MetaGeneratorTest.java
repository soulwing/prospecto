/*
 * File created on Apr 8, 2016
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
package org.soulwing.prospecto.runtime.generator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.eventOfType;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.inNamespace;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.whereValue;
import static org.soulwing.prospecto.testing.matcher.ViewEventMatchers.withName;

import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.node.MetaNode;

/**
 * Unit tests for {@link MetaGenerator}.
 *
 * @author Carl Harris
 */
public class MetaGeneratorTest extends AbstractMetaGeneratorTest<MetaNode> {

  @Override
  MetaNode newNode() {
    return context.mock(MetaNode.class);
  }

  @Override
  AbstractViewEventGenerator<MetaNode> newGenerator(MetaNode node) {
    return new MetaGenerator(node, transformationService);
  }

  @Override
  Matcher<View.Event> expectedEvent(Object value) {
    return eventOfType(View.Event.Type.META,
        withName(NAME), inNamespace(NAMESPACE),
        whereValue(is(sameInstance(value))));
  }

}
