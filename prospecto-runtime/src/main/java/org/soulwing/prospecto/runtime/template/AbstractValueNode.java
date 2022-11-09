/*
 * File created on May 10, 2016
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
package org.soulwing.prospecto.runtime.template;

import org.soulwing.prospecto.api.template.ValueNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;

/**
 * An abstract base for {@link ValueNode} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractValueNode extends AbstractViewNode
    implements ValueNode {

  protected AbstractValueNode(String name, String namespace) {
    super(name, namespace, null, null);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitValue(this, state);
  }

}
