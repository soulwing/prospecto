/*
 * File created on Apr 21, 2016
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
package org.soulwing.prospecto.api.meta;

import org.soulwing.prospecto.api.UndefinedValue;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.template.MetaNode;

/**
 * A handler for values associated with a {@link MetaNode}.
 * <p>
 * An implementation of this class can produce and consume a metadata value
 * associated with a view.
 *
 * @author Carl Harris
 */
public interface MetadataHandler {

  /**
   * Produces a value for the given node.
   * <p>
   * This method is invoked during view generation. If the return value is
   * {@code null} the metadata property will be included in the resulting view
   * (but may be omitted by a writer). If the
   * {@linkplain UndefinedValue#INSTANCE undefined value} is returned, the
   * metadata property will be excluded from the resulting view.
   *
   * @param node the subject node
   * @param context view context
   * @return value which may be {@code null} or {@link UndefinedValue#INSTANCE}
   *    as noted above
   * @throws Exception
   */
  Object produceValue(MetaNode node, ViewContext context) throws Exception;

  /**
   * Consumes a value for the given node.
   * <p>
   * This method is invoked during view application to allow the handler to
   * use the value that was received in the input view in some manner.
   * @param node the subject node
   * @param value input value (which may be {@code null} but will never be
   *    the {@link UndefinedValue#INSTANCE}
   * @param context view context
   * @throws Exception
   */
  void consumeValue(MetaNode node, Object value, ViewContext context)
      throws Exception;

}
