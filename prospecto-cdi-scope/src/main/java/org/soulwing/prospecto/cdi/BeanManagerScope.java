/*
 * File created on Apr 15, 2016
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
package org.soulwing.prospecto.cdi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.BeanManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.cdi.BeanManagerLocator;
import org.soulwing.cdi.DelegatingSimpleBeanManager;
import org.soulwing.cdi.SimpleBeanManager;
import org.soulwing.cdi.jndi.JndiBeanManagerLocator;
import org.soulwing.prospecto.api.scope.Scope;

/**
 * A {@link Scope} that delegates to a CDI
 * {@link javax.enterprise.inject.spi.BeanManager}.
 *
 * @author Carl Harris
 */
public class BeanManagerScope implements Scope {

  private static final Logger logger =
      LoggerFactory.getLogger(BeanManagerScope.class);

  private final SimpleBeanManager beanManager;

  private BeanManagerScope(SimpleBeanManager beanManager) {
    this.beanManager = beanManager;
  }

  @Override
  public <T> T get(Class<T> type) {
    return beanManager.getBean(type);
  }

  @Override
  public <T> T get(String name, Class<T> type) {
    return beanManager.getBean(name, type);
  }

  /**
   * Constructs a new instance.
   * <p>
   * The bean manager delegate will be located using a JNDI lookup.
   * @return scope
   */
  public static BeanManagerScope newInstance() {
    logger.info("locating bean manager via JNDI lookup");
    return newInstance(JndiBeanManagerLocator.getInstance());
  }

  /**
   * Constructs a new instance.
   * @param locator bean manager locator that will be used to find the
   *    bean manager delegate
   * @return scope
   */
  public static BeanManagerScope newInstance(BeanManagerLocator locator) {
    return newInstance(locator.getSimpleBeanManager());
  }

  /**
   * Constructs a new instance.
   * @param beanManager bean manager delegate
   * @return scope
   */
  public static BeanManagerScope newInstance(BeanManager beanManager) {
    return newInstance(new DelegatingSimpleBeanManager(beanManager));
  }

  private static BeanManagerScope newInstance(SimpleBeanManager beanManager) {
    final InvocationHandler handler =
        new BeanManagerInvocationHandler(beanManager);

    final SimpleBeanManager proxy =
        (SimpleBeanManager) Proxy.newProxyInstance(
            beanManager.getClass().getClassLoader(),
            new Class<?>[] { SimpleBeanManager.class },
            handler);

    return new BeanManagerScope(proxy);
  }

  /**
   * An invocation handler that catches {@link UnsatisfiedResolutionException}
   * and returns {@code null} instead as specified by the {@link Scope} API.
   */
  private static class BeanManagerInvocationHandler
      implements InvocationHandler {

    private final Object target;

    BeanManagerInvocationHandler(Object target) {
      this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
      final boolean byType = args[args.length - 1].getClass().isArray();
      try {
        if (logger.isTraceEnabled()) {
          logger.trace("looking for bean of type {}{}",
            byType ? args[0] : args[1],
            byType ? "" : " named " + args[0]);
        }

        return method.invoke(target, args);

      }
      catch (InvocationTargetException ex) {
        if (ex.getCause() == null) {
          throw ex;
        }
        try {
          throw ex.getCause();
        }
        catch (UnsatisfiedResolutionException urex) {
          if (logger.isDebugEnabled()) {
            logger.debug("did not find bean of type {}{}",
                byType ? args[0] : args[1],
                byType ? "" : " named " + args[0]);
          }
          return null;
        }
      }
    }

  }

}
