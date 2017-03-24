/*
 * File created on Mar 24, 2017
 *
 * Copyright (c) 2017 Carl Harris, Jr
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

import org.soulwing.prospecto.api.splice.SpliceHandler;
import org.soulwing.prospecto.api.template.SpliceNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;

/**
 * A concrete {@link SpliceNode}.
 *
 * @author Carl Harris
 */
public class ConcreteSpliceNode extends AbstractViewNode
    implements SpliceNode {

  private final SpliceHandler accessor;

  public ConcreteSpliceNode(String name, String namespace,
      SpliceHandler accessor) {
    super(name, namespace, null);
    this.accessor = accessor;
  }

  @Override
  public SpliceHandler getHandler() {
    return accessor;
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitSplice(this, state);
  }

}
