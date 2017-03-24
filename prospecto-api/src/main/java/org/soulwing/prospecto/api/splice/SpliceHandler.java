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
package org.soulwing.prospecto.api.splice;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 * An handler for a view splice.
 *
 * @author Carl Harris
 */
public interface SpliceHandler {

  /**
   * Generates the subview for a splice.
   * @param node splice node
   * @param context view context
   * @return subview or {@code null} to omit the splice node from its
   *    containing view
   */
  View generate(SpliceNode node, ViewContext context);

  /**
   * Uses the provided view to manipulate the model associated with the splice.
   * @param node splice node
   * @param view view derived for the splice
   * @param context view context
   * @throws ViewInputException to indicate that there was a problem in
   *    applying the view
   */
  void apply(SpliceNode node, View view, ViewContext context)
      throws ViewInputException;

}
