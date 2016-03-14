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
package org.soulwing.prospecto.runtime.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Iterator;

import org.junit.Test;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewContext;
import org.soulwing.prospecto.api.ViewTemplate;
import org.soulwing.prospecto.runtime.context.ConcreteViewContext;

/**
 * Tests for {@link ConcreteViewTemplateBuilderProvider}.
 *
 * @author Carl Harris
 */
public class ConcreteViewTemplateBuilderProviderTest {

  private static final Object MODEL = new Object();
  private static final String NAME = "name";
  private static final String ELEMENT_NAME = "elementName";
  private static final String NAMESPACE = "namespace";

  private ViewContext viewContext = new ConcreteViewContext();

  private ConcreteViewTemplateBuilderProvider provider =
      new ConcreteViewTemplateBuilderProvider();

  @Test
  public void testObjectTemplate() throws Exception {
    final ViewTemplate template =
        provider.object(NAME, NAMESPACE, Object.class).build();
    final View view = template.generateView(new Object(), viewContext);
    final Iterator<View.Event> events = view.iterator();
    assertThat(events.hasNext(), is(true));
    View.Event event = events.next();
    assertThat(event.getType(), is(equalTo(View.Event.Type.BEGIN_OBJECT)));
    assertThat(event.getName(), is(equalTo(NAME)));
    assertThat(event.getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(event.getValue(), is(nullValue()));
  }

  @Test
  public void testArrayOfObjectsTemplate() throws Exception {
    final ViewTemplate template = provider.arrayOfObjects(NAME, ELEMENT_NAME,
        NAMESPACE, Object.class).build();
    final View view = template.generateView(new Object[0], viewContext);
    final Iterator<View.Event> events = view.iterator();
    assertThat(events.hasNext(), is(true));
    View.Event event = events.next();
    assertThat(event.getType(), is(equalTo(View.Event.Type.BEGIN_ARRAY)));
    assertThat(event.getName(), is(equalTo(NAME)));
    assertThat(event.getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(event.getValue(), is(nullValue()));
  }

  @Test
  public void testArrayOfValuesTemplate() throws Exception {
    final ViewTemplate template = provider.arrayOfValues(NAME, ELEMENT_NAME,
        NAMESPACE);
    final View view = template.generateView(new Object[0], viewContext);
    final Iterator<View.Event> events = view.iterator();
    assertThat(events.hasNext(), is(true));
    View.Event event = events.next();
    assertThat(event.getType(), is(equalTo(View.Event.Type.BEGIN_ARRAY)));
    assertThat(event.getName(), is(equalTo(NAME)));
    assertThat(event.getNamespace(), is(equalTo(NAMESPACE)));
    assertThat(event.getValue(), is(nullValue()));
  }

}
