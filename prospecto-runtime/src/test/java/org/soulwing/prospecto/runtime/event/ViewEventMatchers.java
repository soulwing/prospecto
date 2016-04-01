/*
 * File created on Apr 1, 2016
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
package org.soulwing.prospecto.runtime.event;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.View;

/**
 * Matchers for {@link View.Event}.
 *
 * @author Carl Harris
 */
public class ViewEventMatchers {

  @SafeVarargs
  public static Matcher<View.Event> eventOfType(final View.Event.Type type,
      final Matcher<View.Event>... matchers) {
    return new BaseMatcher<View.Event>() {
      @Override
      public boolean matches(Object item) {
        return ((View.Event) item).getType() == type
            && allOf(matchers).matches(item);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("event of type ")
            .appendValue(type)
            .appendText(" and all of ")
            .appendDescriptionOf(allOf(matchers));
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        if (((View.Event) item).getType() != type) {
          description.appendText(" but was type ")
              .appendValue(((View.Event) item).getType());
        }
        else {
          description.appendText(" but ");
          allOf(matchers).describeMismatch(item, description);
        }
      }
    };
  }

  public static Matcher<View.Event> withName(String name) {
    return hasProperty("name", equalTo(name));
  }

  public static Matcher<View.Event> withNoName() {
    return hasProperty("name", nullValue(String.class));
  }

  public static Matcher<View.Event> inNamespace(String namespace) {
    return hasProperty("namespace", equalTo(namespace));
  }

  public static Matcher<View.Event> inDefaultNamespace() {
    return hasProperty("namespace", nullValue(String.class));
  }

  public static Matcher<View.Event> whereValue(Matcher<?> matcher) {
    return hasProperty("value", matcher);
  }

}
