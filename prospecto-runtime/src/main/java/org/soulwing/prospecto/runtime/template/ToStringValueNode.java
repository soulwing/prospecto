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

import org.soulwing.prospecto.api.template.ViewNodeVisitor;

/**
 * A view node that represents the {@link Object#toString()} value of a model
 * instance.
 *
 * @author Carl Harris
 */
public class ToStringValueNode extends AbstractValueNode {

  /**
   * Constructs a new instance.
   * @param name node name
   * @param namespace namespace for {@code name}
   */
  public ToStringValueNode(String name, String namespace) {
    super(name, namespace);
  }

  @Override
  public Class<?> getDataType() {
    return String.class;
  }

  @Override
  public Object getValue(Object model) throws Exception {
    return model.toString();
  }

  @Override
  public Object accept(ViewNodeVisitor visitor, Object state) {
    return visitor.visitValue(this, state);
  }

}
