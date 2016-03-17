/*
 * File created on Mar 17, 2016
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

import java.util.ServiceLoader;

/**
 * A simple service locator utility.
 *
 * @author Carl Harris
 */
class ServiceLocator {

  /**
   * A strategy for service location.
   * @param <T> service type
   */
  public interface Strategy<T> {

    /**
     * Tests whether the given service satisfies this strategy.
     * @param service the subject service
     * @return {@code true} if strategy is satisfied by {@code service}
     */
    boolean isSatisfiedBy(T service);

  }

  /**
   * Finds the first loaded service of a given service class.
   * @param serviceClass service class
   * @param <T> service type
   * @return first loaded service of type {@code T}
   * @throws NoSuchProviderException if no service can be found that satisfies
   *    {@code strategy}
   */
  public static <T> T findService(Class<T> serviceClass)
      throws NoSuchProviderException {
    return findService(serviceClass, new Strategy<T>() {
      @Override
      public boolean isSatisfiedBy(T service) {
        return true;
      }
    });
  }

  /**
   * Finds a suitable service using the given service class and strategy.
   * @param serviceClass service class
   * @param strategy strategy to use for service selection
   * @param <T> service type
   * @return first loaded service that satisfies {@code strategy}
   * @throws NoSuchProviderException if no service can be found that satisfies
   *    {@code strategy}
   */
  public static <T> T findService(Class<T> serviceClass,
      Strategy<T> strategy) throws NoSuchProviderException {
    for (final T service : ServiceLoader.load(serviceClass)) {
      if (strategy.isSatisfiedBy(service)) {
        return service;
      }
    }
    throw new NoSuchProviderException("cannot find a suitable instance of "
        + serviceClass.getName());
  }

}
