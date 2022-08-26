/*
 * File created on Aug 26, 2022
 *
 * Copyright (c) 2022 Carl Harris, Jr
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
package org.soulwing.prospecto.runtime.url;

import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.url.UrlDecorator;
import org.soulwing.prospecto.api.url.UrlDecorators;

/**
 * A {@link UrlDecorators} implementation backed by a linked list.
 *
 * @author Carl Harris
 */
public class LinkedListUrlDecorators implements UrlDecorators {

  private final List<UrlDecorator> delegate = new LinkedList<>();

  @Override
  public void append(UrlDecorator decorator) {
    delegate.add(decorator);
  }

  @Override
  public void prepend(UrlDecorator decorator) {
    delegate.add(0, decorator);
  }

  @Override
  public boolean remove(UrlDecorator decorator) {
    return delegate.remove(decorator);
  }

  @Override
  public List<UrlDecorator> toList() {
    return delegate;
  }

  @Override
  public String decorate(String url) {
    String result = url;
    for (final UrlDecorator decorator : delegate) {
      result = decorator.decorate(result);
    }
    return result;
  }

}
