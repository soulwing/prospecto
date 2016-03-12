/*
 * File created on Mar 11, 2016
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
package org.soulwing.prospecto.demo;

import java.util.Map;

import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewNode;
import org.soulwing.prospecto.spi.UrlResolverProvider;

/**
 * DESCRIBE THE TYPE HERE
 * @author Carl Harris
 */
public class MockUrlResolverProvider implements UrlResolverProvider {

  @Override
  public void init(Map<String, Object> properties) {

  }

  @Override
  public void destroy() {

  }

  @Override
  public UrlResolver getResolver() {
    return new MockUrlResolver();
  }

  static class MockUrlResolver implements UrlResolver {

    @Override
    public String resolve(ViewNode node, ViewContext context) {
      return "MY URL";
    }
  }

}
