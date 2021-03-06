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
 * Keys for view configuration options.
 *
 * @author Carl Harris
 */
public interface ViewKeys {

  /**
   * Prefix used for all built-in option names.
   */
  String PREFIX = "org.soulwing.prospecto";

  /**
   * When set to {@code true} a model editor will ignore unknown property names
   * that appear in an input view.
   */
  String IGNORE_UNKNOWN_PROPERTIES = PREFIX
      + ".applicator.ignoreUnknownProperties";

  /**
   * Subtype discriminator property name.
   */
  String DISCRIMINATOR_NAME = ViewKeys.PREFIX
      + ".view.discriminatorName";

}
