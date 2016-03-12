/*
 * File created on Mar 11, 2016
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

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.spi.UrlResolverProvider;

/**
 * An object that produces {@link UrlResolver} instances.
 *
 * @author Carl Harris
 */
public class UrlResolverProducer {

  private static Singleton<UrlResolverProducer> singleton =
      new Singleton<UrlResolverProducer>() {
        @Override
        protected UrlResolverProducer newInstance() {
          final ServiceLoader<UrlResolverProvider> providers =
              ServiceLoader.load(UrlResolverProvider.class);
          final Iterator<UrlResolverProvider> i = providers.iterator();
          if (!i.hasNext()) {
            throw new NoSuchProviderException(
                UrlResolverProvider.class.getSimpleName());
          }

          return new UrlResolverProducer(i.next());
        }
      };

  private final UrlResolverProvider provider;

  private UrlResolverProducer(UrlResolverProvider provider) {
    this.provider = provider;
  }

  /**
   * Initializes the URL resolver provider.
   * @param properties
   */
  public static void init(Map<String, Object> properties) {
    singleton.getInstance().provider.init(properties);
  }

  /**
   * Prepares the URL resolver provider to be discarded.
   */
  public static void destroy() {
    singleton.getInstance().provider.destroy();
  }

  /**
   * Creates a new URL resolver.
   * @return URL resolver
   */
  public static UrlResolver newResolver() {
    return singleton.getInstance().provider.newResolver();
  }
  
}
