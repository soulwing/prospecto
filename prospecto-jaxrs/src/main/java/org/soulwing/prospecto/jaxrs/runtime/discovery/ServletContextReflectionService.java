/*
 * File created on Mar 12, 2016
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
package org.soulwing.prospecto.jaxrs.runtime.discovery;

import javax.servlet.ServletContext;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.soulwing.prospecto.jaxrs.runtime.ReflectionService;

/**
 * A {@link ReflectionService} for a {@link ServletContext}.
 *
 * @author Carl Harris
 */
public class ServletContextReflectionService
    extends ReflectionsReflectionService {

  public ServletContextReflectionService(ServletContext servletContext) {
    super(reflections(servletContext));
  }

  private static Reflections reflections(ServletContext servletContext) {
    return new Reflections(new ConfigurationBuilder()
        .addUrls(ClasspathHelper.forWebInfClasses(servletContext))
        .addUrls(ClasspathHelper.forWebInfLib(servletContext))
        .addScanners(new TypeAnnotationsScanner()));
  }

}
