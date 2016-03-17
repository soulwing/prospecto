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

  public static ViewWriterFactory getFactory(String providerName)
      throws NoSuchProviderException {
    return singleton.getInstance().newFactory(providerName);
  }

  private ViewWriterFactoryProducer() {
  }

  private ViewWriterFactory newFactory(String providerName)
      throws NoSuchProviderException {
    return findProvider(providerName).newFactory();
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
