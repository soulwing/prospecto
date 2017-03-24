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
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.api.template.SpliceNode;

/**
 * A splice handler that generates a view using a {@link ViewTemplate}
 * provided as a node attribute.
 *
 * @author Carl Harris
 */
public abstract class ViewGeneratingSpliceHandler implements SpliceHandler {

  /**
   * Generates a {@link View} using a {@link ViewTemplate} specified as
   * an attribute of the given {@code node}.
   * @param node splice node
   * @param context view context
   * @return splice view
   */
  @Override
  public View generate(SpliceNode node, ViewContext context) {
    final Object root = getRoot(node, context);
    if (root == null) return null;
    final ViewTemplate template = node.get(ViewTemplate.class);
    if (template == null) {
      throw new ViewTemplateException("node must specify a "
          + ViewTemplate.class.getSimpleName() + " attribute");
    }
    return template.generateView(root, context);
  }

  /**
   * Allows a subtype to provide an implementation for applying the subview
   * associated with a splice to a model.
   * <p>
   * The default implementation does nothing.
   *
   * @param node splice node
   * @param view view derived for the splice
   * @param context view context
   * @throws ViewInputException
   */
  @Override
  public void apply(SpliceNode node, View view, ViewContext context)
      throws ViewInputException {
  }

  /**
   * Gets the root model object to use when generating a view.
   * <p>
   * The returned object must be consistent with the requirements of the
   * view template specified as an attribute to the splice node.
   *
   * @return root model object or {@code null} to indicate that the splice
   *    node should be omitted from its containing view
   * @param node splice node
   * @param context view context
   */
  protected abstract Object getRoot(SpliceNode node, ViewContext context);

}
