/*
 * File created on Mar 10, 2016
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
package org.soulwing.prospecto;

import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.spi.ViewContextProvider;

/**
 * A singleton object that produces {@link ViewContext} instances.
 *
 * @author Carl Harris
 */
public class ViewContextProducer {

  private static Singleton<ViewContextProducer> singleton =
      new Singleton<ViewContextProducer>() {
        @Override
        protected ViewContextProducer newInstance() {
          return new ViewContextProducer(ServiceLocator.findService(
              ViewContextProvider.class));
        }
      };

  private final ViewContextProvider provider;

  private ViewContextProducer(ViewContextProvider provider) {
    this.provider = provider;
  }

  /**
   * Creates a new view context.
   * @return view context
   */
  public static ViewContext newContext() {
    return newContext(ViewOptionsRegistry.getOptions());
  }

  /**
   * Creates a new view context.
   * @param options context options
   * @return view context
   */
  public static ViewContext newContext(Options options) {
    return singleton.getInstance().provider.newContext(options);
  }

}
