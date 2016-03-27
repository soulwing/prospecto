/*
 * File created on Mar 23, 2016
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
 * An interceptor that is notified as property values are visited during
 * view generation or model update.
 *
 * @author Carl Harris
 */
public interface ViewNodePropertyInterceptor extends ViewListener {

  /**
   * Notifies the recipient that a value has been extracted from a model
   * for use as the value of a view node in a view.
   * <p>
   * The recipient can pass the value unchanged or replace the value.
   * @param event the subject event
   * @return the value to use to replace of the subject value; if the handler
   *   does not wish to change the value it <em>must</em> return the
   *   {@linkplain ViewNodePropertyEvent#getValue() subject value}.
   */
  Object didExtractValue(ViewNodePropertyEvent event);

  /**
   * Notifies the recipient that a value will be injected into a model from
   * a view node associated with a view.
   * <p>
   * The recipient can pass the value unchanged or replace the value.
   * @param event the subject event
   * @return the value to use to replace of the subject value; if the handler
   *   does not wish to change the value it <em>must</em> return the
   *   {@linkplain ViewNodePropertyEvent#getValue() subject value}.
   */
  Object willInjectValue(ViewNodePropertyEvent event);

}
