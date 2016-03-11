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
package org.soulwing.prospecto.api.handler;

/**
 * A handler that is invoked as the model values are extracted to a view or
 * injected from a view (into a model).
 * <p>
 * A handler can be used to implement a variety of filtering and transformation
 * tasks.
 *
 * @author Carl Harris
 */
public interface ViewNodeValueHandler {

  /**
   * Notifies the recipient that a value has been extracted for use as the value
   * for the source value node of the event.
   * <p>
   * The recipient can pass the value unchanged or replace the value.
   * @param event the subject event
   * @return the value to use to replace of the subject value; if the handler
   *   does not wish to change the value it <em>must</em> return the
   *   {@linkplain ViewNodeValueEvent#getValue() subject value}.
   */
  Object onExtractValue(ViewNodeValueEvent event);

  /**
   * Notifies the recipient that a value is to be injected into the model
   * associated with the source value node of the event.
   * <p>
   * The recipient can pass the value unchanged or replace the value.
   * @param event the subject event
   * @return the value to use to replace of the subject value; if the handler
   *   does not wish to change the value it <em>must</em> return the
   *   {@linkplain ViewNodeValueEvent#getValue() subject value}.
   */
  Object onInjectValue(ViewNodeValueEvent event);


}
