/*
 * File created on Mar 16, 2016
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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;

import java.util.EnumSet;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.AccessMode;
import org.soulwing.prospecto.api.template.UpdatableNode;
import org.soulwing.prospecto.api.template.ViewNode;
import org.soulwing.prospecto.runtime.accessor.Accessor;

/**
 * Matchers for {@link ViewNode} objects.
 *
 * @author Carl Harris
 */
public class ViewNodeMatchers {

  public static <T extends ViewNode> Matcher<T> viewNode(
      Class<T> nodeClass, String name, String namespace) {
    return allOf(
        instanceOf(nodeClass),
        hasProperty("name", equalTo(name)),
        hasProperty("namespace", equalTo(namespace))
    );
  }

  public static <T extends ViewNode> Matcher<T> viewNode(
      Class<T> nodeClass, String name, String namespace, Class<?> modelType) {
    return allOf(
        viewNode(nodeClass, name, namespace),
        hasProperty("modelType", equalTo(modelType))
    );
  }

  public static <T extends ViewNode> Matcher<T> arrayViewNode(
      Class<T> nodeClass, String name, String elementName, String namespace) {
    return allOf(
        viewNode(nodeClass, name, namespace),
        hasProperty("elementName", equalTo(elementName))
    );
  }

  public static <T extends ViewNode> Matcher<T> arrayViewNode(
      Class<T> nodeClass, String name, String elementName, String namespace,
      Class<?> modelType) {
    return allOf(
        arrayViewNode(nodeClass, name, elementName, namespace),
        hasProperty("modelType", equalTo(modelType))
    );
  }

  @SafeVarargs
  public static Matcher<ViewNode> nodeOfType(
      final Class<? extends ViewNode> nodeType,
      final Matcher<ViewNode>... matchers) {
    return new BaseMatcher<ViewNode>() {
      @Override
      public boolean matches(Object item) {
        return nodeType.isAssignableFrom(item.getClass())
            && allOf(matchers).matches(item);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("node of type ")
            .appendValue(nodeType.getSimpleName())
            .appendText(" and all of ")
            .appendDescriptionOf(allOf(matchers));
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        if (!nodeType.isAssignableFrom(item.getClass())) {
          description.appendText("instead was type ")
              .appendValue(item.getClass().getSimpleName());
        }
        else {
          allOf(matchers).describeMismatch(item, description);
        }
      }
    };
  }

  public static <T extends ViewNode> Matcher<T> named(String name) {
    return hasProperty("name", equalTo(name));
  }

  public static <T extends ViewNode> Matcher<T> withValue(
      Matcher<Object> matcher) {
    return hasProperty("value", matcher);
  }

  public static <T extends ViewNode> Matcher<T> elementsNamed(String elementName) {
    return hasProperty("elementName", equalTo(elementName));
  }

  public static Matcher<ViewNode> inDefaultNamespace() {
    return hasProperty("namespace", nullValue(String.class));
  }

  public static Matcher<ViewNode> inNamespace(String namespace) {
    return hasProperty("namespace", equalTo(namespace));
  }

  public static Matcher<ViewNode> forModelType(Class<?> modelType) {
    return hasProperty("modelType", equalTo(modelType));
  }

  public static Matcher<ViewNode> havingAttribute(
      final Class<?> attributeType) {
    return havingAttribute(attributeType, any(attributeType));
  }

  public static Matcher<ViewNode> havingAttribute(
      final Class<?> attributeType, final Matcher<?> matcher) {
    return new BaseMatcher<ViewNode>() {
      @Override
      public boolean matches(Object item) {
        final Object attribute = ((ViewNode) item).get(attributeType);
        return attribute != null
            && matcher.matches(attribute);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("has attribute of type ")
            .appendValue(attributeType.getSimpleName())
            .appendText(" and ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        if (((ViewNode) item).get(attributeType) == null) {
          description.appendText("but has no attribute of type ")
              .appendValue(attributeType.getSimpleName());
        }
        else {
          matcher.describeMismatch(item, description);
        }
      }
    };
  }

  public static Matcher<ViewNode> havingAttribute(final String name,
      final Class<?> attributeType) {
    return havingAttribute(name, attributeType, any(attributeType));
  }

  public static Matcher<ViewNode> havingAttribute(
      final String name, final Class<?> attributeType,
      final Matcher<?> matcher) {
    return new BaseMatcher<ViewNode>() {
      @Override
      public boolean matches(Object item) {
        final Object attribute = ((ViewNode) item).get(name, attributeType);
        return attribute != null
            && matcher.matches(attribute);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("has attribute '")
            .appendValue(name)
            .appendText("' of type ")
            .appendValue(attributeType.getSimpleName())
            .appendText(" and ")
            .appendDescriptionOf(matcher);
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        if (((ViewNode) item).get(name, attributeType) == null) {
          description.appendText("but has no attribute '")
              .appendValue(name)
              .appendText("' of type ")
              .appendValue(attributeType.getSimpleName());
        }
        else {
          matcher.describeMismatch(item, description);
        }
      }
    };
  }

  @SafeVarargs
  public static Matcher<ViewNode> accessing(
      Matcher<Accessor>... matchers) {
    return hasProperty("accessor", allOf(matchers));
  }

  public static Matcher<ViewNode> accessingNothing() {
    return hasProperty("accessor", nullValue(Accessor.class));
  }

  @SafeVarargs
  public static Matcher<ViewNode> containing(
      Matcher<ViewNode>... matchers) {
    return hasProperty("children", contains(matchers));
  }

  public static Matcher<ViewNode> containingNothing() {
    return hasProperty("children", empty());
  }

  public static Matcher<ViewNode> withModes(final AccessMode first,
      final AccessMode... rest) {
    return new BaseMatcher<ViewNode>() {
      @Override
      public boolean matches(Object item) {
        return ((UpdatableNode) item).getAllowedModes().equals(EnumSet.of(first, rest));
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("having modes ")
            .appendValue(EnumSet.of(first, rest));
      }

      @Override
      public void describeMismatch(Object item, Description description) {
        description.appendText("instead has modes ")
            .appendValue(((UpdatableNode) item).getAllowedModes());
      }
    };
  }
}
