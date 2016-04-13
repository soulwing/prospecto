/*
 * File created on Apr 12, 2016
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
import javax.inject.Inject;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.demo.jaxrs.views.RootView;

/**
 * A {@link RootService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class RootServiceBean implements RootService {

  private static final RootView.Root ROOT = new RootView.Root() {

    @Override
    public RootView.Leagues getLeagues() {
      return new RootView.Leagues() {};
    }

    @Override
    public RootView.Contacts getContacts() {
      return new RootView.Contacts() {};
    }

  };

  @Inject
  private ViewContext viewContext;

  @Override
  public View getRootView() {
    return RootView.INSTANCE.generateView(ROOT, viewContext);
  }

}
