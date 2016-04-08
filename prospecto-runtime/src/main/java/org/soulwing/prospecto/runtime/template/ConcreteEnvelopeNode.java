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
package org.soulwing.prospecto.runtime.template;

import org.soulwing.prospecto.api.template.EnvelopeNode;
import org.soulwing.prospecto.api.template.ViewNodeVisitor;

/**
 * A view node that represents an envelope.
 * <p>
 * An envelope inserts another object node into the textual representation of
 * a view without a corresponding change in model context.
 *
 * @author Carl Harris
 */
public class ConcreteEnvelopeNode extends ConcreteObjectNode
    implements EnvelopeNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   * @param modelType model type
   */
  public ConcreteEnvelopeNode(String name, String namespace, Class<?> modelType) {
    super(name, namespace, modelType);
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitEnvelope(this, state);
  }

}