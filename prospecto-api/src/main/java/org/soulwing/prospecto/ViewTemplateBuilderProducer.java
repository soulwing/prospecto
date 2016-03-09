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

import java.util.Iterator;
import java.util.ServiceLoader;

import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.ViewTemplateBuilder;
import org.soulwing.prospecto.api.ViewTemplateException;
import org.soulwing.prospecto.spi.ViewTemplateBuilderProvider;

/**
 * An object that produces {@link ViewTemplateBuilder} instances.
 *
 * @author Carl Harris
 */
public class ViewTemplateBuilderProducer {

  private static Singleton<ViewTemplateBuilderProducer> singleton =
      new Singleton<ViewTemplateBuilderProducer>() {
        @Override
        protected ViewTemplateBuilderProducer newInstance() {
          final ServiceLoader<ViewTemplateBuilderProvider> providers =
              ServiceLoader.load(ViewTemplateBuilderProvider.class);
          final Iterator<ViewTemplateBuilderProvider> i = providers.iterator();
          if (!i.hasNext()) {
            throw new NoSuchProviderException(
                ViewTemplateBuilderProvider.class.getSimpleName());
          }

          return new ViewTemplateBuilderProducer(i.next());
        }
      };

  private final ViewTemplateBuilderProvider provider;

  private ViewTemplateBuilderProducer(ViewTemplateBuilderProvider provider) {
    this.provider = provider;
  }

  public static ViewTemplateBuilderProducer getInstance() {
    return singleton.getInstance();
  }

  public ViewTemplateBuilder object(Class<?> sourceType)
      throws ViewTemplateException {
    return object(null, null, sourceType);
  }

  public ViewTemplateBuilder object(String name, Class<?> sourceType)
      throws ViewTemplateException {
    return object(name, null, sourceType);
  }

  public ViewTemplateBuilder object(String name, String namespace,
      Class<?> sourceType) throws ViewTemplateException {
    return provider.object(name, namespace, sourceType);
  }

  public ViewTemplateBuilder arrayOfObjects(Class<?> sourceType)
      throws ViewTemplateException {
    return arrayOfObjects(null, null, null, sourceType);
  }

  public ViewTemplateBuilder arrayOfObjects(String name,
      Class<?> sourceType) throws ViewTemplateException {
    return arrayOfObjects(name, null, null, sourceType);
  }

  public ViewTemplateBuilder arrayOfObjects(String name,
      String elementName, String namespace, Class<?> sourceType)
      throws ViewTemplateException {
    return provider.arrayOfObjects(name, elementName, namespace, sourceType);
  }

  public ViewTemplate arrayOfValues() throws ViewTemplateException {
    return arrayOfValues(null, null, null);
  }

  public ViewTemplate arrayOfValues(String name) throws ViewTemplateException {
    return arrayOfValues(name, null, null);
  }

  public ViewTemplate arrayOfValues(String name, String elementName,
      String namespace) throws ViewTemplateException {
    return provider.arrayOfValues(name, elementName, namespace);
  }

}
