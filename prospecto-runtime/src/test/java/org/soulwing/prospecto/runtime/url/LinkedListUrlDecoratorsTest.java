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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.url.UrlDecorator;

/**
 * Unit tests for {@link LinkedListUrlDecorators}.
 *
 * @author Carl Harris
 */
public class LinkedListUrlDecoratorsTest {

  private static final String URL_BEFORE = "url before";
  private static final String URL_AFTER = "url after";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private UrlDecorator decorator0, decorator1;

  private LinkedListUrlDecorators decorators = new LinkedListUrlDecorators();

  @Test
  public void testCrud() throws Exception {
    decorators.append(decorator1);
    assertThat(decorators.toList(), contains(decorator1));
    decorators.prepend(decorator0);
    assertThat(decorators.toList(), contains(decorator0, decorator1));
    assertThat(decorators.remove(decorator0), is(true));
    assertThat(decorators.toList(), contains(decorator1));
    assertThat(decorators.remove(decorator1), is(true));
    assertThat(decorators.toList(), is(empty()));
  }

  @Test
  public void testDecorate() throws Exception {
    assertThat(decorators.decorate(URL_BEFORE), is(URL_BEFORE));

    decorators.append(decorator0);
    context.checking(new Expectations() {
      {
        oneOf(decorator0).decorate(URL_BEFORE);
        will(returnValue(URL_AFTER));
      }
    });

    assertThat(decorators.decorate(URL_BEFORE), is(URL_AFTER));
  }

}
