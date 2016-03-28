/*
 * File created on Mar 28, 2016
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
package org.soulwing.prospecto.runtime.accessor;

import java.util.EnumSet;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.AccessType;

/**
 * Matchers for {@link Accessor} instances.
 *
 * @author Carl Harris
 */
public class AccessorMatchers {

  public static Matcher<Accessor> propertyNamed(final String propertyName) {
    return new BaseMatcher<Accessor>() {
      @Override
      public boolean matches(Object item) {
        return ((Accessor) item).getName().equals(propertyName);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("accesses ")
            .appendValue(propertyName);
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        description.appendText("instead accesses ")
            .appendValue(((Accessor) item).getName());
      }
    };
  }

  public static Matcher<Accessor> onModelType(final Class<?> modelType) {
    return new BaseMatcher<Accessor>() {
      @Override
      public boolean matches(Object item) {
        return ((Accessor) item).getModelType().equals(modelType);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("on model type ")
            .appendValue(modelType.getSimpleName());
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        description.appendText("is instead on ")
            .appendValue(((Accessor) item).getModelType().getSimpleName());
      }
    };
  }

  public static Matcher<Accessor> usingAccessType(final AccessType accessType) {
    return new BaseMatcher<Accessor>() {
      @Override
      public boolean matches(Object item) {
        return ((Accessor) item).getAccessType().equals(accessType);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("using ")
            .appendValue(accessType)
            .appendText(" access");
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        description.appendText("instead uses ")
            .appendValue(((Accessor) item).getAccessType())
            .appendText(" access");
      }
    };
  }

  public static Matcher<Accessor> withModes(final AccessMode first,
      final AccessMode... rest) {
    return new BaseMatcher<Accessor>() {
      @Override
      public boolean matches(Object item) {
        return ((Accessor) item).getAccessModes().equals(EnumSet.of(first, rest));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("having modes ")
            .appendValue(EnumSet.of(first, rest));
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        description.appendText("instead has modes ")
            .appendValue(((Accessor) item).getAccessModes());
      }
    };
  }

}
