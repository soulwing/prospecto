/*
 * File created on Mar 9, 2016
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
 * An exception thrown when building a {@link ViewTemplate}.
 *
 * @author Carl Harris
 */
public class ViewTemplateException extends RuntimeException {

  public ViewTemplateException(String message) {
    super(message);
  }

  public ViewTemplateException(String message, Throwable cause) {
    super(message, cause);
  }

  public ViewTemplateException(Throwable cause) {
    super(cause);
  }

}
