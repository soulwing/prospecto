/*
 * File created on Mar 19, 2016
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
package org.soulwing.prospecto.runtime.text;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.api.ViewException;
import org.soulwing.prospecto.api.ViewInputException;
import org.soulwing.prospecto.api.ViewReader;
import org.soulwing.prospecto.api.options.Options;
import org.soulwing.prospecto.api.options.ViewDefaults;
import org.soulwing.prospecto.runtime.event.ConcreteViewEventFactory;
import org.soulwing.prospecto.runtime.event.ViewEventFactory;
import org.soulwing.prospecto.runtime.view.ConcreteView;

/**
 * An abstract base for {@link ViewReader} implementations.
 *
 * @author Carl Harris
 */
public abstract class AbstractViewReader implements ViewReader {

  private final Deque<StackFrame> stack = new LinkedList<>();
  private final List<View.Event> events = new LinkedList<>();

  private final Options options;
  private final ViewEventFactory eventFactory;

  /**
   * Constructs a new reader
   * @param options configuration options
   */
  protected AbstractViewReader(Options options) {
    this(options, new ConcreteViewEventFactory());
  }

  AbstractViewReader(Options options, ViewEventFactory eventFactory) {
    this.options = options;
    this.eventFactory = eventFactory;
  }

  public Options getOptions() {
    return options;
  }

  @Override
  public View readView() throws ViewException {
    try {
      onReadView();
      if (!stack.isEmpty()) {
        throw new AssertionError("event stack should be empty");
      }
      return new ConcreteView(events);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ViewException(ex);
    }
  }

  /**
   * Notifies the recipient that it should read the textual representation,
   * invoking callbacks as necessary to create the event stream for the view.
   * @throws Exception
   */
  protected abstract void onReadView() throws Exception;

  /**
   * Adds a {@link View.Event.Type#BEGIN_OBJECT} event to the view.
   * @param name name of the object
   *
   */
  protected final void beginObject(String name) {
    beginObject(name, null);
  }

  /**
   * Adds a {@link View.Event.Type#BEGIN_OBJECT} event to the view.
   * @param name name of the object
   *
   */
  protected final void beginObject(String name, String namespace) {
    events.add(newEvent(View.Event.Type.BEGIN_OBJECT, name, namespace, null));
    stack.push(new StackFrame(events.size(),
        newEvent(View.Event.Type.END_OBJECT, name, namespace, null)));
  }

  /**
   * Adds a {@link View.Event.Type#BEGIN_ARRAY} event to the view.
   * @param name name of the object
   *
   */
  protected final void beginArray(String name) {
    beginArray(name, null);
  }

  /**
   * Adds a {@link View.Event.Type#BEGIN_ARRAY} event to the view.
   * @param name name of the object
   *
   */
  protected final void beginArray(String name, String namespace) {
    events.add(newEvent(View.Event.Type.BEGIN_ARRAY, name, namespace, null));
    stack.push(new StackFrame(events.size(),
        newEvent(View.Event.Type.END_ARRAY, name, namespace, null)));
  }

  /**
   * Adds a {@link View.Event.Type#END_OBJECT} or {@link View.Event.Type#END_ARRAY}
   * event to the view, depending on what was the last {@code begin}<em>Type</em>
   * invocation.
   */
  protected final void end() {
    if (stack.isEmpty()) {
      throw new AssertionError("stack underflow");
    }
    events.add(stack.pop().event);
  }

  /**
   * Adds a subtype discriminator to the view.
   * @param value discriminator value
   */
  protected final void discriminator(Object value) {
    if (stack.isEmpty()) {
      throw new AssertionError("stack underflow");
    }
    final StackFrame frame = stack.peek();
    if (events.size() > frame.index) {
      final View.Event event = events.get(frame.index);
      if (event.getType() == View.Event.Type.DISCRIMINATOR) {
        throw new ViewInputException(
            "an object may have only one discriminator");
      }
    }
    events.add(frame.index, newEvent(View.Event.Type.DISCRIMINATOR,
        ViewDefaults.DISCRIMINATOR_NODE_NAME, null, value));
  }

  /**
   * Adds a metadata value to a view.
   * @param name metadata name
   * @param value metadata value
   */
  protected final void meta(String name, Object value) {
    events.add(newEvent(View.Event.Type.META, name, null, value));
  }

  /**
   * Adds a string value to the view.
   * @param name name of the value
   * @param value string value (not {@code null})
   */
  protected final void value(String name, String value) {
    addValue(name, value);
  }

  /**
   * Adds a numeric value to the view.
   * @param name name of the value
   * @param value numeric value (not {@code null})
   */
  protected final void value(String name, Number value) {
    addValue(name, value);
  }

  /**
   * Adds a boolean value to the view.
   * @param name name of the value
   * @param value boolean value (not {@code null})
   */
  protected final void value(String name, Boolean value) {
    addValue(name, value);
  }

  /**
   * Adds a null value to the view.
   * @param name name of the value
   */
  protected final void nullValue(String name) {
    events.add(newEvent(View.Event.Type.VALUE, name, null, null));
  }

  private void addValue(String name, Object value) {
    events.add(newEvent(View.Event.Type.VALUE, name, null, value));
  }

  private View.Event newEvent(View.Event.Type type, String name,
      String namespace, Object value) {
    return eventFactory.newEvent(type, name, namespace, value);
  }

  private class StackFrame {
    final int index;
    final View.Event event;

    public StackFrame(int index, View.Event event) {
      this.index = index;
      this.event = event;
    }

  }

}
