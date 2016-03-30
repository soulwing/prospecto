/*
 * File created on Mar 21, 2016
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
package org.soulwing.prospecto.runtime.editor;

import java.util.Deque;
import java.util.LinkedList;

import org.soulwing.prospecto.api.ModelEditor;
import org.soulwing.prospecto.api.ModelEditorException;
import org.soulwing.prospecto.api.View;
import org.soulwing.prospecto.runtime.context.ScopedViewContext;
import org.soulwing.prospecto.runtime.node.UpdatableRootNode;

/**
 * A {@link ModelEditor} implementation.
 *
 * @author Carl Harris
 */
public class ConcreteModelEditor implements ModelEditor {

  private final UpdatableRootNode root;
  private final View source;
  private final ScopedViewContext context;

  /**
   * Constructs a new instance.
   * @param root root node of the target view template
   * @param source source view
   * @param context view context
   */
  ConcreteModelEditor(UpdatableRootNode root, View source,
      ScopedViewContext context) {
    this.root = root;
    this.source = source;
    this.context = context;
  }

  @Override
  public Object create() throws ModelEditorException {
    try {
      final Deque<View.Event> events = eventDeque(source);
      return root.create(events, context);
    }
    catch (ModelEditorException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ModelEditorException(ex);
    }
  }

  @Override
  public void update(Object model) throws ModelEditorException {
    try {
      assertHasRootModelType(model);
      final Deque<View.Event> events = eventDeque(source);
      root.update(model, events, context);
    }
    catch (ModelEditorException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new ModelEditorException(ex);
    }
  }

  private void assertHasRootModelType(Object model) {
    if (!root.getModelType().isInstance(model)) {
      throw new ModelEditorException("model is not an instance of "
          + root.getModelType().getName());
    }
  }

  private static Deque<View.Event> eventDeque(View view) {
    final Deque<View.Event> deque = new LinkedList<>();
    for (final View.Event event : view) {
      deque.addLast(event);
    }
    return deque;
  }

}
