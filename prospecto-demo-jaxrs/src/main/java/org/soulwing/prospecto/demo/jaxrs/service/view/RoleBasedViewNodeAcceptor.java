/*
 * File created on Apr 14, 2016
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
package org.soulwing.prospecto.demo.jaxrs.service.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.listener.ViewNodeAcceptor;
import org.soulwing.prospecto.api.listener.ViewNodeEvent;
import org.soulwing.prospecto.demo.jaxrs.service.UserContext;
import org.soulwing.prospecto.demo.jaxrs.service.UserContextService;

/**
 * A {@link ViewNodeAcceptor} that applies role constraints.
 *
 * @author Carl Harris
 */
class RoleBasedViewNodeAcceptor implements ViewNodeAcceptor {

  private static final String ROLE_ATTRIBUTE = "roleRequired";

  private static final Logger logger =
      LoggerFactory.getLogger(RoleBasedViewNodeAcceptor.class);

  static final RoleBasedViewNodeAcceptor INSTANCE =
      new RoleBasedViewNodeAcceptor();

  private RoleBasedViewNodeAcceptor() {}

  @Override
  public boolean shouldVisitNode(ViewNodeEvent event) {
    String role = event.getSource().get(ROLE_ATTRIBUTE, String.class);
    if (role == null) return true;

    ViewContext context = event.getContext();
    UserContextService userContextService = context.get(UserContextService.class);
    UserContext user = userContextService.currentUser();
    boolean shouldVisit = user.hasRole(role);

    if (logger.isDebugEnabled()) {
      String parentPath = context.currentViewPathAsString();
      if (parentPath.charAt(parentPath.length() - 1)
          != ViewContext.PATH_DELIMITER) {
        parentPath = parentPath + ViewContext.PATH_DELIMITER;
      }
      final String path = parentPath + event.getSource().getName();
      if (!shouldVisit) {
        logger.debug("user {} does not have role needed for node {}",
            user.getUserName(), path);
      }
      else {
        logger.debug("user {} is authorized for node {}",
            user.getUserName(), path);
      }
    }

    return shouldVisit;
  }

}
