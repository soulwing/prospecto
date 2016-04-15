/*
 * File created on Apr 3, 2016
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
package org.soulwing.prospecto.api.options;

/**
 * Keys used for configuration options of a
 * {@link org.soulwing.prospecto.api.ViewWriter}.
 *
 * @author Carl Harris
 */
public interface WriterKeys {

  /**
   * When set to {@code true} a writer will include properties from the view
   * whose value is null.
   */
  String INCLUDE_NULL_PROPERTIES = "writer.includeNullProperties";

  /**
   * When set to {@code true} a view whose root is of object type will be
   * wrapped in an envelope if the root element is named.
   */
  String WRAP_OBJECT_IN_ENVELOPE = "writer.wrapObject";

  /**
   * When set to {@code false} a view whose root is of array type will not be
   * wrapped in an envelope.
   */
  String WRAP_ARRAY_IN_ENVELOPE = "writer.wrapArray";
  
}
