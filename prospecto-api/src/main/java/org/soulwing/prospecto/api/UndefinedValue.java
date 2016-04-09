/*
 * File created on Mar 26, 2016
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
package org.soulwing.prospecto.api;

/**
 * A value that is used to represent a missing or disallowed value during model
 * update.
 * <p>
 * Since {@code null} is used a view to mean that a property value should be
 * removed during view application, we need another value to represent
 * situations in which a model property should not be updated. For example, if
 * an interceptor does not wish to allow a particular subtree of the model to be
 * visited during update, or for a calculated property value that cannot be
 * produced during update.
 *
 * @author Carl Harris
 */
public class UndefinedValue {

  public static final UndefinedValue INSTANCE = new UndefinedValue();

  private UndefinedValue() {
  }

}
