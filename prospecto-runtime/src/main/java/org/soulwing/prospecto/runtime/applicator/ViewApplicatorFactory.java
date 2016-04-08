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
package org.soulwing.prospecto.runtime.applicator;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewApplicator;
import org.soulwing.prospecto.api.ViewContext;

/**
 * A factory that produces {@link ViewApplicator} objects.
 *
 * @author Carl Harris
 */
public interface ViewApplicatorFactory {

  /**
   * Constructs a new instance.
   * @param modelType expected root model type
   * @param applicator root applicator
   * @param source source view
   * @param context view context
   * @param dataKey envelope key that contains the editable view data
   *    or {@code null} if the view is not enveloped
   * @return model editor
   */
  ViewApplicator newEditor(Class<?> modelType, ViewEventApplicator applicator,
      View source, ViewContext context, String dataKey);

}
