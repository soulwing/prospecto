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
package org.soulwing.prospecto.api.options;

/**
 * Default values for various view options.
 *
 * @author Carl Harris
 */
public interface ViewDefaults {

  /**
   * Default name for a URL node.
   */
  String URL_NODE_NAME = "href";

  /**
   * Default name for a discriminator node.
   */
  String DISCRIMINATOR_NODE_NAME = "objectType";

  /**
   * Default name for an {@code enum} {@link Enum#name() name} value node.
   */
  String ENUM_NODE_NAME = "name";

  /**
   * Default name for a {@link Object#toString()} value node.
   */
  String TO_STRING_NODE_NAME = "displayString";

}
