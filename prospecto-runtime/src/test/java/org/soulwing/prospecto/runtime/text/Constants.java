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
package org.soulwing.prospecto.runtime.text;

import java.math.BigDecimal;

/**
 * Constants that match the keys and values used in test resources.
 * s
 * @author Carl Harris
 */
public interface Constants {

  String OBJECT_NAME = "object";
  String STRING_NAME = "string";
  String INTEGRAL_NAME = "integral";
  String DECIMAL_NAME = "decimal";
  String BOOLEAN_NAME = "boolean";
  String NULL_NAME = "null";
  String CUSTOM_NAME = "custom";

  String STRING_VALUE = "string";
  long INTEGRAL_VALUE = -1L;
  BigDecimal DECIMAL_VALUE = BigDecimal.valueOf(2.71828);
  boolean BOOLEAN_VALUE = true;
  String URL_VALUE = "url";
  String DISCRIMINATOR_VALUE = "discriminator";

  String NAMESPACE = "urn:prospecto.soulwing.org:test";

}
