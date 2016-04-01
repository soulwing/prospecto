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
package org.soulwing.prospecto.tests.view;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.Iterator;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.View;

/**
 * Matchers for {@link View}.
 *
 * @author Carl Harris
 */
public class ViewMatchers {

  @SafeVarargs
  public static Matcher<View> hasEventSequence(Matcher<View.Event>... matchers) {
    return new ViewMatcher(Arrays.asList(matchers));
  }

  static class ViewMatcher extends BaseMatcher<View> {

    private final Iterable<Matcher<View.Event>> matchers;

    private int index;
    private Matcher<View.Event> expectedEventMatcher;
    private View.Event actualEvent;
    private boolean matches = true;
    private boolean moreExpected;
    private boolean moreActual;

    public ViewMatcher(Iterable<Matcher<View.Event>> matchers) {
      this.matchers = matchers;
    }

    @Override
    public boolean matches(Object item) {
      return matches((View) item);
    }

    private boolean matches(View view) {
      final Iterator<Matcher<View.Event>> expected = matchers.iterator();
      final Iterator<View.Event> actual = view.iterator();
      moreExpected = expected.hasNext();
      moreActual = actual.hasNext();
      while (matches && moreExpected && moreActual) {
        expectedEventMatcher = expected.next();
        actualEvent = actual.next();
        matches = expectedEventMatcher.matches(actualEvent);
        if (matches) {
          index++;
          moreExpected = expected.hasNext();
          moreActual = actual.hasNext();
        }
      }
      return matches && !moreExpected && !moreActual;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("view containing event sequence [");
      final Iterator<Matcher<View.Event>> i = matchers.iterator();
      while (i.hasNext()) {
        description.appendDescriptionOf(i.next());
        if (i.hasNext()) {
          description.appendText(", ");
        }
      }
      description.appendText("]");
    }

    @Override
    public void describeMismatch(Object item, Description description) {
      if (!matches) {
        description.appendText("at index ")
            .appendValue(index)
            .appendText(", expected ")
            .appendDescriptionOf(expectedEventMatcher);
        expectedEventMatcher.describeMismatch(actualEvent, description);
      }
      else if (moreExpected) {
        description.appendText("after matching ")
            .appendValue(index)
            .appendText(" events; there are fewer events than expected");
      }
      else if (moreActual) {
        description.appendText("after matching ")
            .appendValue(index)
            .appendText(" events; there are more unmatched events");
      }
      else {
        description.appendText("huh?");
      }
    }

  }

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
