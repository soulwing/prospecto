/*
 * File created on Mar 9, 2016
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
package org.soulwing.prospecto.api;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A base implementation of {@link ViewContext} that can be extended to
 * expose additional scopes.
 *
 * @author Carl Harris
 */
public class ViewContextBase implements ViewContext {

  static class ViewFrame {
    final String name;
    final Class<?> modelType;
    final Object model;

    public ViewFrame(String name, Class<?> modelType, Object model) {
      this.name = name;
      this.modelType = modelType;
      this.model = model;
    }
  }

  private final Deque<ViewFrame> viewStack = new LinkedList<>();

  @Override
  public List<Class<?>> getModelPath() {
    final List<Class<?>> path = new LinkedList<>();
    Iterator<ViewFrame> i = viewStack.descendingIterator();
    while (i.hasNext()) {
      final Class<?> modelType = i.next().modelType;
      if (modelType != null) {
        path.add(modelType);
      }
    }
    return path;
  }

  @Override
  public List<String> getViewPath() {
    final List<String> path = new LinkedList<>();
    Iterator<ViewFrame> i = viewStack.descendingIterator();
    while (i.hasNext()) {
      final String nodeName = i.next().name;
      path.add(nodeName);
    }
    return path;
  }

  @Override
  public void push(String name) {
    push(name, null, null);
  }

  @Override
  public void push(String name, Class<?> modelType, Object model) {
    viewStack.push(new ViewFrame(name, modelType, model));
    System.out.format("PUSH '%s' %s %s\n", name,
        modelType != null ? modelType.getSimpleName() : "",
        model != null ? model : "");
  }

  @Override
  public void pop() {
    final ViewFrame frame = viewStack.pop();
    System.out.format("POP '%s' %s %s\n", frame.name,
        frame.modelType != null ? frame.modelType.getSimpleName() : "",
        frame.model != null ? frame.model : "");
  }

  @Override
  public <T> T get(Class<T> type) {
    return null;
  }

  @Override
  public <T> T get(String name, Class<T> type) {
    return null;
  }

}
