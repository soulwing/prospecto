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
package org.soulwing.prospecto.api.template;

import org.soulwing.prospecto.api.splice.SpliceHandler;

/**
 * A {@link ViewNode} representing the splicing of a separate view
 * generation/application processing operation.
 *
 * @author Carl Harris
 */
public interface SpliceNode extends AppliableNode {

  /**
   * Gets the handler to use for this splice.
   * @return handler
   */
  SpliceHandler getHandler();

}
