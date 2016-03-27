/*
 * File created on Mar 17, 2016
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
package org.soulwing.prospecto.runtime.context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.Test;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.discriminator.DiscriminatorStrategy;
import org.soulwing.prospecto.api.discriminator.SimpleClassNameDiscriminatorStrategy;
import org.soulwing.prospecto.api.scope.MutableScope;

/**
 * Unit tests for {@link ConcreteScopedViewContextFactory}.
 *
 * @author Carl Harris
 */
public class ConcreteScopedViewContextFactoryTest {

  private ViewContext source = new ConcreteViewContext();

  private ConcreteScopedViewContextFactory factory =
      new ConcreteScopedViewContextFactory();

  @Test
  public void testDefaultDiscriminatorStrategy() throws Exception {
    final ScopedViewContext viewContext = factory.newContext(source);
    viewContext.get(DiscriminatorStrategy.class);
  }

  @Test
  public void testExplicitDiscriminatorStrategy() throws Exception {
    DiscriminatorStrategy strategy = new SimpleClassNameDiscriminatorStrategy();
    MutableScope scope = source.appendScope();
    scope.put(strategy);
    final ScopedViewContext viewContext = factory.newContext(source);
    assertThat(viewContext.get(DiscriminatorStrategy.class),
        is(sameInstance(strategy)));
  }

}
