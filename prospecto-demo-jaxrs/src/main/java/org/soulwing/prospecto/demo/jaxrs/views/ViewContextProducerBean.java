/*
 * File created on Mar 13, 2016
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
package org.soulwing.prospecto.demo.jaxrs.views;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.UrlResolverProducer;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.api.MutableScope;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.converter.DateTypeConverter;
import org.soulwing.prospecto.api.converter.PropertyExtractingValueTypeConverter;
import org.soulwing.prospecto.api.handler.ViewNodeElementEvent;
import org.soulwing.prospecto.api.handler.ViewNodeElementHandler;
import org.soulwing.prospecto.api.handler.ViewNodeEvent;
import org.soulwing.prospecto.api.handler.ViewNodeHandler;
import org.soulwing.prospecto.api.handler.ViewNodeValueEvent;
import org.soulwing.prospecto.api.handler.ViewNodeValueHandler;
import org.soulwing.prospecto.demo.jaxrs.domain.Money;
import org.soulwing.prospecto.demo.jaxrs.service.UserContextService;

/**
 * A bean that produces {@link ViewContext} instances.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class ViewContextProducerBean {

  private static final Logger logger =
      LoggerFactory.getLogger(ViewContextProducerBean.class);

  @Inject
  private UserContextService userContextService;

  @Produces
  @ApplicationScoped
  public ViewContext newContext() {

    final ViewContext context = ViewContextProducer.newContext();

    final MutableScope scope = context.newScope();
    context.getScopes().add(scope);
    scope.put(UrlResolverProducer.getResolver());
    scope.put(userContextService);

    context.getValueTypeConverters().add(DateTypeConverter.Builder.with()
        .format(DateTypeConverter.Format.ISO8601_DATE)
        .supportedType(java.sql.Date.class)
        .build());

    context.getValueTypeConverters().add(DateTypeConverter.Builder.with()
        .format(DateTypeConverter.Format.ISO8601_WITH_TIME_ZONE)
        .build());

    context.getValueTypeConverters().add(
        PropertyExtractingValueTypeConverter.Builder.with()
            .modelType(Money.class)
            .propertyName("amount")
            .build());

    configureContext(context);
    return context;
  }

  private void configureContext(ViewContext context) {
    context.getViewNodeHandlers().add(new ViewNodeHandler() {
      @Override
      public boolean beforeVisit(ViewNodeEvent event) {
        String role = event.getSource().get("roleRequired", String.class);
        if (role == null) return true;
        logger.info("role required for node {}: {}",
            event.getContext().currentViewPath(), role);
        return event.getContext().get(UserContextService.class)
            .currentUser().hasRole(role);
      }

      @Override
      public void afterVisit(ViewNodeEvent event) {
        logger.debug("visited node {}",
            event.getContext().currentViewPathAsString());

      }
    });

    context.getViewNodeElementHandlers().add(new ViewNodeElementHandler() {
      @Override
      public boolean beforeVisitElement(ViewNodeElementEvent event) {
        return true;
      }

      @Override
      public Object onExtractElement(ViewNodeElementEvent event) {
        final Object elementModel = event.getElement();
        logger.debug("visited element at path {}: {}",
            event.getContext().currentViewPathAsString(),
            elementModel);
        return elementModel;
      }
    });

    context.getViewNodeValueHandlers().add(new ViewNodeValueHandler() {
      @Override
      public Object onExtractValue(ViewNodeValueEvent event) {
        logger.debug("extracted value at path {}: {}",
            event.getContext().currentViewPathAsString(),
            event.getValue());
        return event.getValue();
      }

      @Override
      public Object onInjectValue(ViewNodeValueEvent event) {
        return null;
      }
    });
  }

}
