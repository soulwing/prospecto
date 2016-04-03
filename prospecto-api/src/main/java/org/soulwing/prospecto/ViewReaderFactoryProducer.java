/*
 * File created on Mar 18, 2016
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

import org.soulwing.prospecto.api.ViewReaderFactory;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.OptionsMap;
import org.soulwing.prospecto.spi.ViewReaderFactoryProvider;

/**
 * A singleton object that produces {@link ViewReaderFactory} instances.
 *
 * @author Carl Harris
 */
public class ViewReaderFactoryProducer {

  private static final Singleton<ViewReaderFactoryProducer> singleton =
      new Singleton<ViewReaderFactoryProducer>() {
        @Override
        protected ViewReaderFactoryProducer newInstance() {
          return new ViewReaderFactoryProducer();
        }
      };


  /**
   * Gets a reader factory.
   * @param providerName name of the provider; e.g. 'JSON', 'XML', etc.
   * @return reader factory
   * @throws NoSuchProviderException
   */
  public static ViewReaderFactory getFactory(String providerName)
      throws NoSuchProviderException {
    return getFactory(providerName, new OptionsMap());
  }

  /**
   * Gets a reader factory.
   * @param providerName name of the provider; e.g. 'JSON', 'XML', etc.
   * @param options configuration options
   * @return reader factory
   * @throws NoSuchProviderException
   */
  public static ViewReaderFactory getFactory(String providerName,
      Options options)
      throws NoSuchProviderException {
    return singleton.getInstance().newFactory(providerName, options);
  }

  private ViewReaderFactoryProducer() {
  }

  private ViewReaderFactory newFactory(String providerName, Options options)
      throws NoSuchProviderException {
    return findProvider(providerName).newFactory(options);
  }

  private ViewReaderFactoryProvider findProvider(final String providerName)
      throws NoSuchProviderException {
    return ServiceLocator.findService(ViewReaderFactoryProvider.class,
        new ServiceLocator.Strategy<ViewReaderFactoryProvider>() {
          @Override
          public boolean isSatisfiedBy(ViewReaderFactoryProvider service) {
            return service.getName().equals(providerName);
          }
        });
  }

}
