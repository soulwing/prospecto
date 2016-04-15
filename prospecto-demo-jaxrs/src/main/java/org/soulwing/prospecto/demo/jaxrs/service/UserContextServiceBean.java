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
package org.soulwing.prospecto.demo.jaxrs.service;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

/**
 * A mocked up {@link UserContextService}.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class UserContextServiceBean implements UserContextService {

  private final UserContext context = new UserContext() {

    private final String userName =
        System.getProperty("userContext.userName", "jane");

    private final List<String> roles = Arrays.asList(
        System.getProperty("userContext.roles", "").split("\\s*[,\\s]\\s*"));

    @Override
    public String getUserName() {
      return userName;
    }

    @Override
    public boolean hasRole(String role) {
      return roles.contains(role);
    }
  };

  @Override
  public UserContext currentUser() {
    return context;
  }

}
