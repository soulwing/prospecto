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
package org.soulwing.prospecto.api.template;

import org.soulwing.prospecto.api.meta.MetadataHandler;

/**
 * A {@link ViewNode} that represents a metadata value.
 *
 * @author Carl Harris
 */
public interface MetaNode extends AppliableNode {

  /**
   * Gets the metadata handler associated with this node.
   * @return handler instance (never {@code null})
   */
  MetadataHandler getHandler();

  /**
   * Gets the (constant) value associated with this node.
   * @return value (which may be {@code null})
   */
  Object getValue();

}
