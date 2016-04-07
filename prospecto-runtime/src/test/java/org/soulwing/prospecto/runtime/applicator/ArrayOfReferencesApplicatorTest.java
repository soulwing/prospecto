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

import org.soulwing.prospecto.api.node.ArrayOfReferencesNode;

/**
 * Unit tests for {@link ArrayOfValuesApplicator}.
 *
 * @author Carl Harris
 */
public class ArrayOfReferencesApplicatorTest
    extends AbstractArrayOfObjectsApplicatorTest<ArrayOfReferencesNode> {

  @Override
  ArrayOfReferencesNode newNode() {
    return context.mock(ArrayOfReferencesNode.class);
  }

  @Override
  AbstractViewEventApplicator<ArrayOfReferencesNode> newApplicator(
      ArrayOfReferencesNode node) {
    return new ArrayOfReferencesApplicator(node,
        Collections.singletonList(child),
        entityFactory, transformationService,
        associationUpdater);
  }

}
