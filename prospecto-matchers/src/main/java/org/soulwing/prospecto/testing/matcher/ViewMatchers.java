/*
 * File created on Apr 4, 2016
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
package org.soulwing.prospecto.testing.matcher;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.converter.Coerce;

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
            && Matchers.allOf(matchers).matches(item);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("event of type ")
            .appendValue(type)
            .appendText(" and all of ")
            .appendDescriptionOf(Matchers.allOf(matchers));
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        if (((View.Event) item).getType() != type) {
          description.appendText(" but was type ")
              .appendValue(((View.Event) item).getType());
        }
        else {
          description.appendText(" but ");
          Matchers.allOf(matchers).describeMismatch(item, description);
        }
      }
    };
  }

  public static Matcher<View.Event> withName(String name) {
    return Matchers.hasProperty("name", Matchers.equalTo(name));
  }

  public static Matcher<View.Event> withNoName() {
    return Matchers.hasProperty("name", Matchers.nullValue(String.class));
  }

  public static Matcher<View.Event> inNamespace(String namespace) {
    return Matchers.hasProperty("namespace", Matchers.equalTo(namespace));
  }

  public static Matcher<View.Event> inDefaultNamespace() {
    return Matchers.hasProperty("namespace", Matchers.nullValue(String.class));
  }

  public static Matcher<View.Event> whereValue(Matcher<?> matcher) {
    return Matchers.hasProperty("value", matcher);
  }

  public static Matcher<View> sameView(View expectedView) {
    return new SameViewMatcher(expectedView);
  }

  private static class SameViewMatcher extends BaseMatcher<View> {

    private final View expectedView;

    private int index;
    private boolean matches = true;
    private boolean moreActual;
    private boolean moreExpected;
    private View.Event actualEvent;
    private View.Event expectedEvent;

    public SameViewMatcher(View expectedView) {
      this.expectedView = expectedView;
    }

    @Override
    public boolean matches(Object item) {
      final Iterator<View.Event> actual = ((View) item).iterator();
      final Iterator<View.Event> expected = expectedView.iterator();
      moreActual = actual.hasNext();
      moreExpected = expected.hasNext();
      while (matches && moreActual && moreExpected) {
        actualEvent = actual.next();
        expectedEvent = expected.next();
        matches = equals(actualEvent, expectedEvent);
        if (matches) {
          index++;
          moreActual = actual.hasNext();
          moreExpected = expected.hasNext();
        }
      }
      return matches && !moreActual && !moreExpected;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("view containing event sequence [");
      final Iterator<View.Event> i = expectedView.iterator();
      while (i.hasNext()) {
        description.appendValue(i.next());
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
            .appendValue(expectedEvent)
            .appendText(", but was ")
            .appendValue(actualEvent);
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

    private boolean equals(View.Event actual, View.Event expected) {
      return sameType(actual.getType(), expected.getType())
          && (index == 0 || Objects.equals(actual.getName(), expected.getName()))
          && (index == 0 || Objects.equals(actual.getNamespace(), expected.getNamespace()))
          && equals(actual.getValue(), expected.getValue());
    }

    private boolean equals(Object actual, Object expected) {
      if (expected == null && actual == null) return true;
      if (expected == null ^ actual == null) return false;
      Class<?> expectedType = expected.getClass();
      actual = Coerce.toValueOfType(expectedType, actual);
      if (expected instanceof Calendar) {
        return ((Calendar) expected).getTime().equals(
            ((Calendar) actual).getTime());
      }
      return expected.equals(actual);
    }

    private boolean sameType(View.Event.Type actual, View.Event.Type expected) {
      if (expected == null && actual == null) return true;
      if (expected == null ^ actual == null) return false;
      if ((expected == View.Event.Type.META ||
            expected == View.Event.Type.VALUE)
          && (actual == View.Event.Type.META
              || actual == View.Event.Type.VALUE)) {
        return true;
      }
      return expected == actual;
    }

  }

}
