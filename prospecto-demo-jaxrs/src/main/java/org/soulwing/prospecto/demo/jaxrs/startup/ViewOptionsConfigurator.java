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
package org.soulwing.prospecto.demo.jaxrs.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.soulwing.prospecto.ViewOptionsRegistry;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.ViewKeys;
import org.soulwing.prospecto.api.options.WriterKeys;

/**
 * A startup bean that configures view options.
 *
 * @author Carl Harris
 */
@Startup
@Singleton
public class ViewOptionsConfigurator {

  @PostConstruct
  public void init() {
    final Options options = ViewOptionsRegistry.getOptions();
    options.put(WriterKeys.OMIT_NULL_PROPERTIES, true);
    options.put(ViewKeys.IGNORE_UNKNOWN_PROPERTIES, true);
  }

}
