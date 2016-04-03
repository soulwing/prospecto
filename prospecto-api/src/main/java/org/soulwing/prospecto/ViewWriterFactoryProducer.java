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
package org.soulwing.prospecto;

import org.soulwing.prospecto.api.ViewWriterFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.spi.ViewWriterFactoryProvider;

/**
 * A singleton object that produces {@link ViewWriterFactory} instances.
 *
 * @author Carl Harris
 */
public class ViewWriterFactoryProducer {

  private static final Singleton<ViewWriterFactoryProducer> singleton =
      new Singleton<ViewWriterFactoryProducer>() {
        @Override
        protected ViewWriterFactoryProducer newInstance() {
          return new ViewWriterFactoryProducer();
        }
      };

  /**
   * Gets a writer factory.
   * @param providerName name of the provider; e.g. 'JSON', 'XML', etc.
   * @return writer factory
   * @throws NoSuchProviderException
   */
  public static ViewWriterFactory getFactory(String providerName)
      throws NoSuchProviderException {
    return getFactory(providerName, new OptionsMap());
  }

  /**
   * Gets a writer factory.
   * @param providerName name of the provider; e.g. 'JSON', 'XML', etc.
   * @param options configuration options
   * @return writer factory
   * @throws NoSuchProviderException
   */
  public static ViewWriterFactory getFactory(String providerName,
      Options options)
      throws NoSuchProviderException {
    return singleton.getInstance().newFactory(providerName, options);
  }

  private ViewWriterFactoryProducer() {
  }

  private ViewWriterFactory newFactory(String providerName, Options options)
      throws NoSuchProviderException {
    return findProvider(providerName).newFactory(options);
  }

  private ViewWriterFactoryProvider findProvider(final String providerName)
      throws NoSuchProviderException {
    return ServiceLocator.findService(ViewWriterFactoryProvider.class,
        new ServiceLocator.Strategy<ViewWriterFactoryProvider>() {
          @Override
          public boolean isSatisfiedBy(ViewWriterFactoryProvider service) {
            return service.getName().equals(providerName);
          }
        });
  }

}
