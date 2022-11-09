/*
 * File created on Jun 13, 2021
 *
 * Copyright (c) 2021 Carl Harris, Jr
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

import org.soulwing.prospecto.api.template.ViewNodeVisitor;
import org.soulwing.prospecto.runtime.template.AbstractContainerNode;

/**
 * A no-op mock container node for testing.
 *
 * @author Carl Harris
 */
class MockContainerNode extends AbstractContainerNode {

  public MockContainerNode() {
    super("some name", "some namespace", null, Object.class);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return null;
  }
}
