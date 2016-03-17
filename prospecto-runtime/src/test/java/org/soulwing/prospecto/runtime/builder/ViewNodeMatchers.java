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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.soulwing.prospecto.api.ViewNode;

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

  public static <T extends ViewNode> Matcher<T> hasAttributeOfType(
      final Class<T> nodeClass, final Class<?> attributeType) {
    return new BaseMatcher<T>() {
      @Override
      public boolean matches(Object item) {
        return ((ViewNode) item).get(attributeType) != null;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("has an attribute of type "
            + attributeType.getName());
      }
    };
  }

}
