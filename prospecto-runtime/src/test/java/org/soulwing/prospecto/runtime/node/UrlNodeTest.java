/*
 * File created on Mar 14, 2016
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
package org.soulwing.prospecto.runtime.node;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.prospecto.api.UrlResolver;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;

/**
 * Unit tests for {@linK UrlNode}.
 *
 * @author Carl Harris
 */
public class UrlNodeTest {

  private static final String NAMESPACE = "namespace";
  private static final Object URL = "url";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private ScopedViewContext viewContext;

  @Mock
  private UrlResolver urlResolver;

  private UrlNode node = new UrlNode(UrlNode.DEFAULT_NAME, NAMESPACE);

  @Test
  public void testGetModelValue() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(viewContext).get(UrlResolver.class);
        will(returnValue(urlResolver));
        oneOf(urlResolver).resolve(node, viewContext);
        will(returnValue(URL));
      }
    });

    assertThat(node.getModelValue(null, viewContext), is(sameInstance(URL)));
  }

  @Test
  public void testToViewValue() throws Exception {
    assertThat(node.toViewValue(URL, viewContext), is(sameInstance(URL)));
  }

}
