/*
 * File created on Mar 21, 2016
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
package org.soulwing.prospecto.runtime.editor;

import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;

/**
 * A factory that produces {@link ModelEditor} objects.
 *
 * @author Carl Harris
 */
public interface ModelEditorFactory {

  /**
   * Constructs a new instance.
   * @param target root node of the target view template
   * @param source source view
   * @param context view context
   * @param dataKey envelope key that contains the editable view data
   *    or {@code null} if the view is not enveloped
   * @return model editor
   */
  ModelEditor newEditor(AbstractViewNode target, View source,
      ViewContext context, String dataKey);

}
