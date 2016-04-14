/*
 * File created on Apr 9, 2016
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
package org.soulwing.prospecto.demo.jaxrs.service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.soulwing.prospecto.UrlResolverProducer;
import org.soulwing.prospecto.ViewContextProducer;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.converter.DateTypeConverter;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.api.scope.MutableScope;

/**
 * A bean that produces {@link ViewContext} instances.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class ViewContextProducerBean {

  @Inject
  private UserContextService userContextService;

  @Inject
  private EntityReferenceResolver referenceResolver;

  @Produces
  @ApplicationScoped
  public ViewContext newContext() {

    final ViewContext context = ViewContextProducer.newContext();

    final MutableScope scope = context.appendScope();

    scope.put(UrlResolverProducer.getResolver());
    scope.put(userContextService);

    context.getValueTypeConverters().append(DateTypeConverter.Builder.with()
        .format(DateTypeConverter.Format.ISO8601_DATE)
        .supportedType(java.sql.Date.class)
        .build());

    context.getValueTypeConverters().append(DateTypeConverter.Builder.with()
        .format(DateTypeConverter.Format.ISO8601_WITH_TIME_ZONE)
        .build());

    context.getAssociationManagers()
        .append(LeagueDivisionToManyAssociationManager.INSTANCE);

    context.getAssociationManagers()
        .append(DivisionTeamToManyAssociationManager.INSTANCE);

    context.getAssociationManagers()
        .append(DivisionPlayerToManyAssociationManager.INSTANCE);

    context.getReferenceResolvers().append(referenceResolver);

    context.getListeners().append(LoggingViewNodeListener.INSTANCE);
    context.getListeners().append(LoggingViewNodePropertyListener.INSTANCE);
    context.getListeners().append(RoleBasedViewNodeAcceptor.INSTANCE);

    context.getOptions().put(ViewKeys.IGNORE_UNKNOWN_PROPERTIES, true);
    return context;
  }

}
