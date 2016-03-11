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
package org.soulwing.prospecto.runtime.builder;

import org.soulwing.prospecto.NoSuchProviderException;
import org.soulwing.prospecto.UrlResolverProducer;
import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.api.handler.ViewNodeElementEvent;
import org.soulwing.prospecto.api.handler.ViewNodeElementHandler;
import org.soulwing.prospecto.api.handler.ViewNodeEvent;
import org.soulwing.prospecto.api.handler.ViewNodeHandler;
import org.soulwing.prospecto.api.handler.ViewNodeValueEvent;
import org.soulwing.prospecto.api.handler.ViewNodeValueHandler;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.node.AbstractViewNode;
import org.soulwing.prospecto.runtime.view.ConcreteView;

/**
 * A {@link ViewTemplate} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplate implements ViewTemplate {

  private final AbstractViewNode root;

  public ConcreteViewTemplate(AbstractViewNode root) {
    this.root = root;
  }

  @Override
  public View generateView(Object source, ViewContext context)
      throws ViewException {
    try {
      final ScopedViewContext configuredContext = configureContext(
          (ScopedViewContext) context);
      return new ConcreteView(root.evaluate(source, configuredContext), root);
    }
    catch (Exception ex) {
      throw new ViewException(ex);
    }
  }

  @Override
  public AbstractViewNode generateSubView(String name) {
    return root.copy(name);
  }

  private ScopedViewContext configureContext(ScopedViewContext context) {
    context = context.copy();

    final ViewContext.MutableScope globalScope = context.newScope();
    context.getScopes().add(0, globalScope);

    final UrlResolver resolver = getUrlResolver();

    if (resolver != null) {
      globalScope.put(resolver);
    }

    context.getViewNodeHandlers().add(new ViewNodeHandler() {
      @Override
      public boolean beforeVisit(ViewNodeEvent event) {
        System.out.println("visiting node at path "
            + event.getContext().currentViewPathAsString());
        return true;
      }

      @Override
      public void afterVisit(ViewNodeEvent event) {

      }
    });

    context.getViewNodeElementHandlers().add(new ViewNodeElementHandler() {
      @Override
      public boolean beforeVisitElement(ViewNodeElementEvent event) {
        System.out.println("visiting element at path "
            + event.getContext().currentViewPathAsString()
            + ": " + event.getElementModel());
        return true;
      }

      @Override
      public Object onVisitElement(ViewNodeElementEvent event) {
        return event.getElementModel();
      }
    });

    context.getViewNodeValueHandlers().add(new ViewNodeValueHandler() {
      @Override
      public Object onExtractValue(ViewNodeValueEvent event) {
        return event.getValue();
      }

      @Override
      public Object onInjectValue(ViewNodeValueEvent event) {
        return null;
      }
    });

    return context;
  }

  private UrlResolver getUrlResolver() {
    try {
      return UrlResolverProducer.newResolver();
    }
    catch (NoSuchProviderException ex) {
      return null;
    }
  }


}
